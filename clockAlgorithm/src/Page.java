public class Page {
    String readOrWrite;
    int pageNum;
    int referenced;
    boolean empty;
    int modified;

    Page(){
        this.empty = true;
        this.pageNum = -1000;
    }
    Page(int pageNum){
        this.pageNum = pageNum;
        this.referenced = 0;
        this.empty = false;
        this.modified = 0;
    }

    public String getReadOrWrite(){
        return this.readOrWrite;
    }

    public int getPageNum(){
        return this.pageNum;
    }

    public int getReferenced(){
        return this.referenced;
    }

    public void setReferenced(int reference){
        this.referenced = reference;
    }

    public boolean getEmpty(){
        return this.empty;
    }

    public int getModified(){
        return this.modified;
    }

    public void setModified(int i){
        this.modified = i;
    }
}
