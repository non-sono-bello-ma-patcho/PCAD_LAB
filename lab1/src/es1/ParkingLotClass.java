package es1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ParkingLotClass {
    private final int MAXP;
    private int freeLot;
    final Lock lock = new ReentrantLock();
    final Condition notFull = lock.newCondition();

    public ParkingLotClass(int p){
        MAXP = p;
        freeLot = p;
    }

    public ParkingLotClass(){
        this(10);
    }

    public void enter(int car) throws InterruptedException {
        // acquire lock:
        lock.lock(); //ottimo
        // check condition (p>0)
        try {
            while (freeLot == 0) notFull.await();
            freeLot--;
            System.out.println("Guess whos in? Motherfucking car: "+car);
        }finally{
            lock.unlock();
        }
    }
    public void exit(int car){
        // acquire lock:
        lock.lock(); //ottimo
        freeLot++;
        notFull.signal();
        System.out.println("Bitch car: "+car+" is out");
        lock.unlock();

    }
}
