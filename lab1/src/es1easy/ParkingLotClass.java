package es1easy;

public class ParkingLotClass {
    private final int MAXP;
    private int freeLot;

    public ParkingLotClass(int p){
        MAXP = p;
        freeLot = p;
    }

    public ParkingLotClass(){
        this(10);
    }

    public synchronized void enter(int car) throws InterruptedException {
        while(freeLot<=0) ;
        freeLot--;
            System.out.println("Guess whos in? Motherfucking car: "+car);
    }
    public synchronized void exit(int car){
        freeLot++;
        System.out.println("Bitch car: "+car+" is out");
    }
}
