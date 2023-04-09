// Use java threads to simulate the Dining Philosophers Problem
// YOUR NAME HERE.  Programming assignment 2 (from ece.gatech.edu) */

class dining {
    static Object terminated= new Object();
    public static void main(String args[])
    {
        System.out.println("Starting the Dining Philosophers Simulation\n");
        //initialize eating log as False
        miscsubs.InitializeChecking();
        // Your code here...
        Chopstic chopstics= new Chopstic();
        Philosopher[] philosophers= new Philosopher[5];
        for(int i=0; i< philosophers.length; i++){
            philosophers[i]=new Philosopher(i);
        }

        for(int i=0; i< philosophers.length; i++){
            Thread thread= new Thread(philosophers[i]);
            thread.start();
        }
        synchronized (terminated){
            try {
                terminated.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // End of your code
        System.out.println("Simulation Ends..");       
        miscsubs.LogResults();
    }
};


