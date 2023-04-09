public class Philosopher implements Runnable {
    private int id;
    private Object lock= Chopstic.lock;
    private Object terminated= dining.terminated;
    Chopstic chopstics= new Chopstic();
    public Philosopher(int id) {
        this.id=id;
    }

    @Override
    public void run() {
        try{
            while(miscsubs.TotalEats<miscsubs.MAX_EATS){
                miscsubs.RandomDelay();
                boolean isStarve=false;
                for(int i=0 ; i<5; i++){
                    if(miscsubs.StarveCount[i]>10) isStarve=true;
                }
                if(isStarve==true){
                    chopstics.putDown(id);
                }else{
                    chopstics.canChopstics(id);

                    miscsubs.DoneEating(id);
                    chopstics.putDown(id);
                }
            }
            synchronized (terminated){
                terminated.notify();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
