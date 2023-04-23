import java.util.*;

public class PageTable {
    int memorySize;
    ArrayList<Page> pageTable;
    int clockHand = 0;
    boolean full;

    PageTable(int memorySize){
        this.pageTable = new ArrayList<>();
        this.memorySize = memorySize;
        for (int i = 0; i < memorySize; i++){
            pageTable.add(new Page());
        }
        this.full = false;
    }

    public Page getPage(int index){
        return pageTable.get(index);
    }

    public void checkFull(){
        for(int i = 0; i < this.memorySize; i ++){
            if (pageTable.get(i).getEmpty()){
                this.full = false;
                return;
            }
        }
        this.full = true;
    }

    public boolean getFull(){
        checkFull();
        return this.full;
    }

    public void addPage(Page page, int index){
        pageTable.add(index, page);
    }

    public int getFirstEmpty(){
        if (full == true){
            return -1000;
        } else {
            for (int i = 0; i < this.memorySize; i++){
                if (pageTable.get(i).getEmpty())
                    return i;
            }
            return -1000;
        }
    }

    public Page findVictim(){
        Page victim = new Page();
        while (victim.getEmpty() == true){
            if (pageTable.get(this.clockHand).getReferenced() == 1){
                pageTable.get(this.clockHand).setReferenced(0);
                clockHand ++;
            } else {
                victim = pageTable.get(this.clockHand);
                clockHand ++;
            }
        if (clockHand >= memorySize)
            clockHand = 0;
        }   
        // System.out.println(victim.getPageNum());
        return victim; 
    }

    public int getIndex(Page page){
        for(int i = 0; i < memorySize; i++){
            if (page.getPageNum() == pageTable.get(i).getPageNum())
                return i;
        }
        return -1000;
    }

    public void removePage(Page page){
        for (int i = 0; i < pageTable.size(); i++){
            if (page.getPageNum() == pageTable.get(i).getPageNum()){
                pageTable.remove(i);
                break;
            }
        }
    }
}
