import java.util.*;

/**
 * Class to emulate the physical memory
 */
public class PhysicalMemory{
    /**
     * variable to emulate frames in memory
     */
    Frame[] frames;
    public int size=0;
    Queue<Integer> queue = new LinkedList<>();
    ArrayList<Integer> LRU = new ArrayList<Integer>();
    /**
     * we need a variable to store how many
     * frames are used
     */
    int currentFreeFrame;


    /**
     * Constructor
     */
    public PhysicalMemory(){
        this.frames = new Frame[256];
        this.currentFreeFrame = 0;
    }


    /**
     * function to add a new frame to memory
     *
     * @param f Frame to be added
     * @return int the frame number just added
     */
    public int addFrame(Frame f){
    	size++;
        this.frames[this.currentFreeFrame] = new Frame(f.data);
        this.currentFreeFrame++;
        return this.currentFreeFrame-1;
    }
    
    
//    public int FIFO_add(Frame f) {
//    	if(queue.size()<128) {
//    		this.queue.add(this.currentFreeFrame);
//    		this.frames[this.currentFreeFrame] = new Frame(f.data);
//    		this.currentFreeFrame++;
//            return this.currentFreeFrame-1;
//		}else {
//			//만약 할당할 수 가 없다면,
//			int victim=queue.poll();
//			this.queue.add(victim);
//	    	this.frames[victim]=new Frame(f.data);
//	    	return victim;
//		}
//    }
//    
    public int LRU_add(Frame f, int f_num) {
    	if(LRU.size()<128) {
    		this.LRU.add(this.currentFreeFrame);
    		this.frames[this.currentFreeFrame] = new Frame(f.data);
    		this.currentFreeFrame++;
            return this.currentFreeFrame-1;
		}else {
			//만약 할당할 수 가 없다면,
//			int victim=this.LRU.get(this.LRU.size()-1);
//			this.LRU.add(e)\
			int victim= this.LRU.remove(this.LRU.size()-1);
			this.frames[victim]=new Frame(f.data);
	    	return victim;
		}
    }
    public void LRU_update(int f_num) {
    	if(this.LRU.contains(f_num)) {
    		int victim_index= LRU.indexOf(f_num);
    		this.LRU.remove(victim_index);
    		this.LRU.add(f_num);
    	}
    }

    /**
     * function to get value in memory
     *
     * @param f_num int frame number
     * @param offset int offset
     * @return int content in specified location
     */
    public int getValue(int f_num, int offset){
        Frame frame = this.frames[f_num];
        return frame.data[offset];
    }


}


/**
 * wrapper class to group all frame related logics
 */
class Frame {
    /**
     * variable to store datas of this frame
     */
    int[] data;


    /**
     * Constructor
     *
     * @param d int[256] for initializing frame
     */
    public Frame(int[] d){
        this.data = new int[256];
        for(int i=0;i<256;i++){
            this.data[i] = d[i];
        }
    }


    /**
     * Copy Constructor
     *
     * @param f Frame to be copied
     */
    public Frame(Frame f){
        this.data = new int[256];
        System.arraycopy(f.data, 0, this.data, 0, 256);
    }
}

