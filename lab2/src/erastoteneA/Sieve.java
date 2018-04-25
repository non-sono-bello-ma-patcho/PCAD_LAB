package erastoteneA;

import java.util.concurrent.ArrayBlockingQueue;

public class Sieve extends Thread{
    protected ArrayBlockingQueue<Integer> numQueue; // coda degli elementi da filtrare
    private int associatedPrimeNumber;
    private Sieve next;

    // constructor
    public Sieve(int n){
        associatedPrimeNumber = n;
        if(associatedPrimeNumber!=-1) System.err.println(associatedPrimeNumber);
        numQueue = new ArrayBlockingQueue<>(10);
    }

    // getter
    public int getAssociatedPrimeNumber(){
        return associatedPrimeNumber;
    }

    public Sieve getNext(){
        return next;
    }

    // add method which consume another buffer:
    public boolean strain(){
        int n;
        try {
            n = numQueue.take(); // keep on fetching head until success;
            if (n % associatedPrimeNumber != 0) {
                if (next == null) {
                    next = new Sieve(n);
                    next.start(); // dynamically created thread...
                }
                next.numQueue.put(n);  // keep on trying to add element to queue until success;
            }
            if(n==-1) return false; // stop straining
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    // add elements to queue (useful for the starter thread)
    public void add(int n){
        try {
            numQueue.put(n);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // run method:
    public void run(){
        while(strain()); // strain down baby;
    }
}
