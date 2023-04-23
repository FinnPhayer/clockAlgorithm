import java.util.*;

public class SwapSpace {
    ArrayList<Page> swapSpace;

    SwapSpace(ArrayList<Page> swapSpace){
        this.swapSpace = new ArrayList<>();
        this.swapSpace = swapSpace;
    }

    public void removePage(Page page){
        for (int i = 0; i < swapSpace.size(); i++){
            if (page.getPageNum() == swapSpace.get(i).getPageNum()){
                swapSpace.remove(i);
                break;
            }
        }
    }

    public void addPage(Page page){
        swapSpace.add(page);
    }
}
