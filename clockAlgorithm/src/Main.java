import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.PrintStream;
import java.io.FileOutputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        ArrayList<String> commands = new ArrayList<>();
        int memorySize = Integer.parseInt(args[0]);
        int swapInCost = Integer.parseInt(args[1]);
        int swapOutCost = Integer.parseInt(args[2]);
        PrintStream fileOut = new PrintStream(new FileOutputStream(args[4]));
        System.setOut(fileOut);

        try {
            File file = new File(args[3]);
            Scanner scanner = new Scanner(file);
            
            while (scanner.hasNextLine()) {
                commands.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }

        // Populate pages array
        ArrayList<Page> pages = new ArrayList<>();
        for (int i = 0; i < commands.size(); i++){
            String [] parts;
            parts = commands.get(i).split(" ");
            String readOrWrite = parts[0];
            int pageNum = Integer.parseInt(parts[1]);
            if (pages.size() == 0){
                pages.add(new Page(pageNum));
            } else {
                for(int j = 0; j < pages.size(); j++){
                    if (pages.get(j).getPageNum() == pageNum){
                        break;
                    } else {
                        if (j == pages.size() - 1){
                            pages.add(new Page(pageNum));
                        }         
                    }
                } 
            }
        }

        //Create Page Table and swap space
        PageTable pageTable = new PageTable(memorySize);
        SwapSpace swapSpace = new SwapSpace(pages);
        // Create total variables
        int pageRefCount = 0;
        int pageFaultOnRead = 0;
        int pageFaultOnWrite = 0;
        int totalSwapInCost = 0;
        int totalSwapOutCost = 0;

        //Go through the command list
        for (int i = 0; i < commands.size(); i++){

            // Set default values
            String hitOrFault = "F";
            int victimPage = -1;
            // These are multipliers
            int swapIn = 0;
            int swapOut = 0;

            // get read or write and page number
            String [] parts;
            parts = commands.get(i).split(" ");
            String readOrWrite = parts[0];
            int pageNum = Integer.parseInt(parts[1]);

            // Get the current page from the swap space
            // Will be an empty page if page is not in the swap space
            Page currentPage = new Page();
            for (int k = 0; k < swapSpace.swapSpace.size(); k++){
                if (pages.get(k).getPageNum() == pageNum)
                    currentPage = pages.get(k);
            }

            // Check against other pages in pagetable
            for(int j = 0; j < memorySize; j++){
                // If its a hit
                if (pageTable.getPage(j).getPageNum() == pageNum){
                    swapIn = 0;
                    swapOut = 0;
                    hitOrFault = "H";
                    victimPage = -1;
                    pageTable.getPage(j).setReferenced(1);
                    if (readOrWrite.compareTo("W") == 0)
                        pageTable.getPage(j).setModified(1);
                    break;
                } else if (j == memorySize - 1){
                    // if its a fault 
                    hitOrFault = "F";
                    if (readOrWrite.compareTo("W") == 0){
                        pageFaultOnWrite ++;
                        currentPage.setModified(1);
                    } else {
                        pageFaultOnRead ++;
                    }
                    // set referenced bit to 1
                    currentPage.setReferenced(1);

                    // if the page has an empty space
                    if (pageTable.getFull() == false){
                        victimPage = -1;
                        swapIn = 1;
                        swapOut = 0;
                        pageTable.addPage(currentPage, pageTable.getFirstEmpty());
                        swapSpace.removePage(currentPage);
                        break;
                    } else {
                        // no empty space
                        swapIn = 1;
                        Page victim = pageTable.findVictim();
                        swapOut = 1 * victim.getModified();

                        if (victim != null){
                            victimPage = victim.getPageNum();
                        }
                        pageTable.addPage(currentPage, pageTable.getIndex(victim));
                        pageTable.removePage(victim);
                        swapSpace.addPage(victim);
                    }
                }
            }

            // Print the results
            System.out.printf("%d: %s %2d  %s %3d %3d %3d\n", 
                    pageRefCount + 1, 
                    readOrWrite, 
                    pageNum, 
                    hitOrFault, 
                    victimPage, 
                    swapInCost * swapIn, 
                    swapOutCost * swapOut);
            
            pageRefCount ++;
            totalSwapInCost = totalSwapInCost + swapInCost * swapIn;
            totalSwapOutCost = totalSwapOutCost + swapOutCost * swapOut;
        }
        System.out.println("Number of Page References: " + pageRefCount);
        System.out.println("Number of Page Faults On Read: " + pageFaultOnRead);
        System.out.println("Number of Page Faults On Write: " + pageFaultOnWrite);
        System.out.println("Total Time Units For Swapping In: " + totalSwapInCost);
        System.out.println("Total TIme Units For Swapping Out: " + totalSwapOutCost);
    }
}
