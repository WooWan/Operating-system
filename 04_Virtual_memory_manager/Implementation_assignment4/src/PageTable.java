import java.util.*;
/**
 * Class to emulate a page table
 */
public class PageTable{
    PageTableItem[] table;
    public int size;
    Queue<Integer> queue = new LinkedList<>();
    ArrayList<Integer> LRU = new ArrayList<Integer>();
    /**
     * Constructor
     *
     * initialize PageTable with 256 items
     */
    public PageTable(){
        table = new PageTableItem[256];
        for(int i=0;i<256;i++){
            this.table[i] = new PageTableItem(-1, false);
        }
    }


    /**
     * function to get frame number using page number
     *
     * @param int p_num Page Number to be queried
     * @return int fraame number if it's in table, -1 otherwise
     */
    public int get(int p_num){
        int frameNumber = this.table[p_num].getFrameNumber();
        if(frameNumber == -1){
            // frame number not in page table
            return -1;
        }
        return frameNumber;
    }
    

    /**
     * function to add page number, frame number to PageTable
     *
     * @param p_num int Page number
     * @param f_num int Frame number
     */
    public void add(int p_num, int f_num){
        this.table[p_num] = new PageTableItem(f_num, true);
        size++;
    }
 
    
    public void FIFO_add(int p_num, int f_num) {
    	this.table[p_num] = new PageTableItem(f_num, true);
    	this.queue.add(p_num);
    }
    
    public int FIFO_victim() {
    	int victim= queue.poll();
    	int frame= this.table[victim].frameNumber;
    	this.table[victim].frameNumber=-1;
    	this.table[victim].valid=false;
    	return frame;
    }
    
  
    public void LRU_add(int p_num, int f_num) {
    	this.table[p_num] = new PageTableItem(f_num, true);
    	this.LRU.add(p_num);
    	size++;
    }
    
    public void LRU_update(int p_num) {
    	int updated_value=this.LRU.remove(LRU.indexOf(p_num));
    	this.LRU.add(updated_value);
    }
    
    public int LRU_victim() {
    	int victim=this.LRU.remove(0);
    	int frame= this.table[victim].frameNumber;
    	this.table[victim].frameNumber=-1;
    	this.table[victim].valid=false;
    	return frame;
    }
}


/**
 * class for storing items of PageTable
 *
 * Because Java doesn't have struct, we use this class
 * to emulate struct in C.
 */
class PageTableItem{
    /**
     * variable to store frame number information
     */
    int frameNumber;
    /**
     * variable to store if the frame is in memory
     *
     * not used in this project
     */
    boolean valid;


    /**
     * Constructor
     *
     * @param i int frame number
     * @param b boolean whether it's valid frame
     */
    public PageTableItem(int i, boolean b){
        this.frameNumber = i;
        this.valid = b;
    }


    /**
     * Get the frame number of this PageTable item
     *
     * @return this.frameNumber
     */
    public int getFrameNumber(){
        return this.frameNumber;
    }
}

