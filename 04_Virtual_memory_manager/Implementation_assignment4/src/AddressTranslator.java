// This implementation is for Lab07 in the Operating System courses in SeoulTech
// The original version of this implementation came from UGA

import java.io.*;
import java.util.*;


public class AddressTranslator {


        public static void main(String[] args){
                //String inputFile = args[0];
                String inputFile = "InputFile.txt";
                /**
                 * variable of logical address
                 */
                int addr;

                /**
                 * variable of page number
                 */
                int p_num;


                /**
                 * variable of offset
                 */
                int offset;


                /**
                 * variable of frame number
                 */
                int f_num;
                /**
                 * variable of value stored in address
                 */
                int value;
                /**
                 * variable of physics address
                 */
                int phy_addr;
                /**
                 * variable of count of tlb miss
                 */
                int tlb_miss = 0;
                /**
                 * variable of count of page fault
                 */
                int page_fault = 0;
                int cnt=0;
                try{
                	Scanner sc = new Scanner(new File(inputFile));
                	
                	TLB tlb = new TLB();
                	PageTable pt = new PageTable();
                	PhysicalMemory pm = new PhysicalMemory();
                	BackStore bs = new BackStore();
                	Queue<Integer> replacedPage = new LinkedList<>();
                	int method=0;
                	System.out.println("You can implement program by entering input");
                	System.out.println("Input 0: Virtual memory without page replacement");
                	System.out.println("Input 1: Virtual memory with FIFO alogorithm");
                	System.out.println("Input 2: Virtual memory with LRU algorithm");
                	while(true) {
                		Scanner input= new Scanner(System.in);
                		try {
                			method=input.nextInt();
                		}catch(InputMismatchException e) {
                			System.out.println("Input shoud be 0, 1, and 2");
							System.out.println("Please take input again");
                			continue;
                		}
						
						if(method<=2 && method>=0) {
							break;
						}else {
							System.out.println("Input shoud be 0, 1, and 2");
							System.out.println("Please take input again");
							continue;
						}
                	}
                	
                	while(sc.hasNextInt()){
                		cnt++;
                		addr = sc.nextInt();
                		addr = addr % 65536;
                		offset = addr % 256;
                		p_num = addr / 256;
                		f_num = -1; //
                		f_num = tlb.get(p_num);
                		//tlb에 해당하는 memory가 없을 경우
                		if(f_num == -1){
                			tlb_miss++;
                			//select frame based on algorithm
                			f_num = pt.get(p_num);
        
                			if(f_num == -1){
                				page_fault++;
                				//origin
                				
                				if(method==0) {
                					Frame f= new Frame(bs.getData(p_num));
                    				f_num=pm.addFrame(f);
                    				pt.add(p_num, f_num);
                    				tlb.put(p_num, f_num);
                				}else if(method==1) {
                					if(pm.size>=128) {
                						f_num=pt.FIFO_victim();
                						Frame f= new Frame(bs.getData(p_num));
                    					pm.frames[f_num] = new Frame(f.data);
                						pt.FIFO_add(p_num,f_num);
                						tlb.put(p_num, f_num);
                						replacedPage.add(p_num);
                						System.out.println(f_num +"frame is reallocated to page "+ p_num );
                					}else{
                						Frame f= new Frame(bs.getData(p_num));
                        				f_num=pm.addFrame(f);
                        				pt.FIFO_add(p_num, f_num);
                        				tlb.put(p_num, f_num);
                					}                				
                				//LRU
                				}else if(method==2) {
                					if(pm.size>=128) {
                    					f_num= pt.LRU_victim();
                    					Frame f= new Frame(bs.getData(p_num));
                    					pm.frames[f_num] = new Frame(f.data);
                    					pt.LRU_add(p_num,f_num);
                    					tlb.put(p_num, f_num);
                    					replacedPage.add(p_num);
                    					System.out.println(f_num +" frame is reallocated to page "+ p_num );
                    				}else {
                    					Frame f= new Frame(bs.getData(p_num));
                        				f_num=pm.addFrame(f);
                        				pt.LRU_add(p_num,f_num);
                        				tlb.put(p_num, f_num);
                    				}
                				}
                			}else {
                				if(method==2)
                					pt.LRU_update(p_num);
                			}
                		}else {
                			if(method==2)
                				pt.LRU_update(p_num);
                		}

		                phy_addr = f_num * 256 + offset;
		                value = pm.getValue(f_num, offset);

                       System.out.println(
                    		   String.format("Virtual address: %s Physical address: %s Value: %s", addr, phy_addr , value)
                    	);
                	}
                	System.out.print("The replaced page are: ");
                	System.out.println(replacedPage);
                	System.out.println(String.format("TLB hit rate: %f, Page Fault rate: %f", (cnt-tlb_miss)/(double)cnt, page_fault/(double)cnt));
                	System.out.println(String.format("TLB miss: %s, Page Fault: %s", tlb_miss, page_fault));
                } catch(Exception e){
	                e.printStackTrace();
	                System.exit(0);
                }
        	}
}

