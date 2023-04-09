import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.PriorityQueue;

//In ThreadWorker class, thread is assigned pid number and manage its life cycle create, sleep, terminate.
class ThreadWorker implements Runnable{
    Thread t;
    private int PID;
    private int lifeTime;
    private long endTime;
    private int lifeTimeProgram;
    private int threadEndTime;
    private int createTime;
    private long startTime;
    private long createMs;
    Manager manager= ThreadExample.manager;
    ThreadExample mainThread=new ThreadExample();
    //ThreadWorker constructor initiates starttime, createTime, PID, lifetime, endTime
    //By calling start(), it execute run() method implictly.
    public ThreadWorker(int PID, int lifeTime) {
        this.startTime= mainThread.startTime;
        this.PID=PID;
        this.lifeTime=lifeTime;
        this.endTime= mainThread.endTime;
        this.lifeTimeProgram=mainThread.lifetimeProgram;
        this.createMs=System.currentTimeMillis();
        this.createTime= (int)((System.currentTimeMillis()-startTime)/1000);
        this.threadEndTime= ((createTime+lifeTime)<=lifeTimeProgram)? createTime+lifeTime : lifeTimeProgram;
        t= new Thread(this,"target");
        t.start();
    }

    public void run() {
        BufferedWriter bw= new BufferedWriter(new OutputStreamWriter(System.out));
        try{
            bw.write("Thread "+PID+" created at "+ (int)((System.currentTimeMillis()-startTime)/1000)+" second\r\n");
            bw.flush();
        }catch(Exception e){
            System.out.println('a');
            e.printStackTrace();
        }
        //There are two cases which it should be terminated.
        //1. If time reaches end time, exits the while loop.
        //2. If thread lifetime is over, exits the while loop.
        //I use busy waiting rather than Thread.sleep() because it should be terminaed case 1.
        while((endTime>System.currentTimeMillis())&&(lifeTime>((int)(System.currentTimeMillis()-createMs)/1000))) { }
        // If program exit while loop, execute releasePID.
        manager.releasePID(PID);
        try{
            bw.write("Thread " + PID + " terminate thread at "+(int)((System.currentTimeMillis()-startTime)/1000)+" second\r\n");
            bw.flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

//Class threadMake is responsible for making new Thread.
//I design my architecture to execute getPID() and getPIDWait() concurrently. To make this situation,
//I made one more class, threadMake which implements Runnable. In this class, thread sleep 0~lifetimeProgram seconds randomly and
//once thread wakeup from thread.sleep(), execute getPID or getPIDWait to create new threads.
class threadMake implements Runnable{
    Thread thr;
    private long lifetimeProgram;
    Manager manager= ThreadExample.manager;
    private int flag;
    //The threadMake constructor iniates endTime and executes thr.start().
    public threadMake(int flag){
        this.flag=flag;
        this.lifetimeProgram=ThreadExample.lifetimeProgram;
        thr=new Thread(this);
        thr.start();
    }
    @Override
    public void run() {
        try{
            //Sleep random time 0~lifetimeProgram.
            Thread.sleep(((int)(Math.random()*(lifetimeProgram*1000))));
            //If thread exit from sleep(), executes manager.getPID() to assign availalbe pid.
            if(flag==1){
                manager.getPID();
            }else{
                manager.getPIDWait();
            }
        }catch(Exception e){
            //catch error and print error trace.
            e.printStackTrace();
        }

    }
}
public class ThreadExample {
    //Set start time and endTime.
    static public long startTime;
    static public long endTime;
    static public int lifetimeProgram;
    static Manager manager;
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        int number, lifetime,flag;
        lifetime=0;
        number=0;
        //Take the thread number and lifetime of thread.
        do{
            flag=1;
            try{
                System.out.println("Enter the thread number");
                number = sc.nextInt();
                System.out.println("Enter the life time of prgram");
                lifetimeProgram=sc.nextInt();
                System.out.println("Enter the thread life time");
                lifetime = sc.nextInt();
                if(number<=0 || lifetimeProgram<=0 || lifetime<=0){
                    System.out.println("Inputs must greater than 0.");
                    System.out.println("Please take input again");
                    flag=0;
                }
            }catch(InputMismatchException e){
                flag=0;
                sc.nextLine();
                System.out.println("Inputs are invalid, take input again");
            }
        }while(flag==0);
        startTime=System.currentTimeMillis();
        endTime=startTime+lifetimeProgram*1000;
        //Make instance of Manager and execute fillQueue in Manger class.
        manager = new Manager(lifetime);
        manager.fillQueue();

        //Make number of n Threads.
        for (int i = 0; i < number; i++) {
            new threadMake(1);
        }
        // Make busy wait to check whether endTime has come.
        while (System.currentTimeMillis() < endTime) {}
        //Wait 1 second to release all thread and initial state.
        try{
            Thread.sleep(1000);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("--------------------------------------------");
        System.out.println("System for getPID is finished at "+lifetimeProgram + " seconds");
        System.out.println("From now on, getPidWait test starts");
        System.out.println("--------------------------------------------");
        //Make new Thread for test getPIDWait.
        for(int i=0; i<number; i++){
            new threadMake(0);
        }
        //Set initial startTime and endTime again to test getPIDWait.
        startTime=System.currentTimeMillis();
        endTime=startTime+lifetimeProgram*1000;
        //Make program busy wait to check whether endTime has come.
        while (System.currentTimeMillis() < endTime) {}
        try{
            Thread.sleep(1000);
        }catch(Exception e){
            e.printStackTrace();
        }
        //If busy waiting is finished, terminate the system.
        System.exit(0);

    }
}
//Manager class implements PIDManger which predefined getPID, getPIDWait, releasePID
//Also, PID queue is managed in Manager class.
class Manager implements PIDManager{
    //In Manager, PIDpq is managed, because it is close to other function which need priority queue.
    //I used priority queue because lower number PID is assigned first, priority queue is internally restructured itself
    //so that small number located to [0]. It is more efficient rather than array.
    static PriorityQueue<Integer> PIDpq=new PriorityQueue<>();
    private int lifetime;

    //Initiates lifetime.
    public Manager(int lifetime){
        this.lifetime=lifetime;
    }
    //Initially, fill priority queue range from MIN_PID,4 to MAX_PID, 128.
    public static void fillQueue() {
        for (int i = MIN_PID; i <= MAX_PID; i++) PIDpq.add(i);
    }
    //getPID returns -1 if there is no available pid, and returns PID if there is available pid
    //Also, I added synchronized keyword, because when more than 2 threads can access to getPID(), synchronized makes
    //them synchronized.
    @Override
    public synchronized int getPID() {
        if(PIDpq.isEmpty()){
            System.out.println("There is no thread to assign");
            System.out.println("Return -1....");
            return -1;
        }else{
            new ThreadWorker((int)PIDpq.peek(), lifetime);
            int peek= PIDpq.poll();
            return peek;
        }
    }
    //Unlike getPID(), getPIDWait() waits if there is no available PID with busy wait.
    //If pid is released, then PID is assigned to waiting threads.Also, I used synchronized to synchorizied if more than
    //two threads access to getPIDWait()
    @Override
    public synchronized int getPIDWait() {
        //If queue is empty, print out error message.
        if(PIDpq.isEmpty()) System.out.println("***There is no available pid in queue waiting...***");
        while(PIDpq.isEmpty()){
            try{
                //Java supports wait() in synchronized method. It is waked up when other thread execute notify().
                wait();
            }catch(InterruptedException ie){
                ie.printStackTrace();
            }
        }
        new ThreadWorker((int)PIDpq.peek(), lifetime);
        int peek= PIDpq.poll();
        return peek;
    }
    //The releasePID adds pid to priorityqueue, PIDpq. Likewise getPID and getPIDWait i used synchronized to synchorize
    // if more than two threads access to relasePID()
    //When releasePID is called, add pid in the queue and execute notify() to wakeup thread in wait().
    @Override
    public synchronized void releasePID(int pid) {
         PIDpq.add(pid);
         notify();
    }
}
interface PIDManager {
    public static final int MIN_PID=4;
    public static final int MAX_PID=127;
    public int getPID();
    public int getPIDWait();
    public void releasePID(int pid);
}