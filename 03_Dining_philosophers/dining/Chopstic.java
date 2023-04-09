public class Chopstic {
    static Object lock = new Object();
    static private boolean []chopstics;
    public Chopstic(){
        chopstics= new boolean[5];
        for(int i=0; i< chopstics.length; i++){
            chopstics[i]=true;
        }
    }
    public void canChopstics(int id){
        synchronized (lock){
            int leftChopstic=id;
            int rightChopstic= (id+1)%5;
            while(chopstics[leftChopstic]==false || chopstics[rightChopstic]==false){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            chopstics[leftChopstic]=false;
            chopstics[rightChopstic]=false;
            if(miscsubs.TotalEats<miscsubs.MAX_EATS){
                miscsubs.StartEating(id);
                System.out.println(miscsubs.TotalEats);
            }
            miscsubs.RandomDelay();
        }
    }

    public void putDown(int id) {
        synchronized (lock){
            int leftChopstic=id;
            int rightChopstic= (id+1)%5;
            chopstics[leftChopstic]=true;
            chopstics[rightChopstic]=true;
            lock.notify();
        }
    }
}
