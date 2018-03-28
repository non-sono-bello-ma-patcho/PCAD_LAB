package es1;

import static java.lang.System.exit;

public class ParkingLotTest {

    public static void main(String[] args){
        if(args.length == 0){
            System.err.println("USAGE <n-threads>");
            exit(1);
        }
        ParkingLotClass pl = new ParkingLotClass(Integer.parseInt(args[0]));
        Thread[] th = new CarRunnable[50];
        System.out.println("Starting the machines, stay ready");
        for(Thread t : th){
            t = new CarRunnable(pl);
            t.start();
        }
    }
}
