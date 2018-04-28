package es1easy;

import es1.ParkingLotClass;

import java.util.Random;
// Adding a comment to test discord webhook;
public class CarRunnable extends Thread{
    private ParkingLotClass myPark;
    private static int identifier=0;
    private int id;
    public CarRunnable(ParkingLotClass p){
        if (p==null) throw new IllegalStateException("Parking Lot must not be null"); // ottimo
        myPark = p;
        id = ++identifier;
    }

    public void run(){
        Random rand = new Random();
        try {
            while (true) {

                myPark.enter(id);
                sleep(rand.nextInt(5000));
                myPark.exit(id);
                sleep(rand.nextInt(5000));
            }
        }catch(Throwable t){
            System.err.println("Error occurred in thread: "+t.getMessage());
        }
    }

}
