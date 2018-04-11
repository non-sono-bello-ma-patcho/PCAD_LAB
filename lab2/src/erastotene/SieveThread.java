package erastotene;

import java.util.concurrent.ArrayBlockingQueue;

public class SieveThread extends Thread {
    private SieveThread next;
    private int primeNumber;
    protected ArrayBlockingQueue<Integer> buffer;

    public int getPrimeNumber(){
        return primeNumber;
    }

    public boolean getNumber(int n){
        try{
            return buffer.add(n); // add item to blocking queue
        }
        catch(IllegalStateException ise){
            // ??
            return false;
        }
    }

    public SieveThread(int n){
        primeNumber = n;
    }

    private boolean sendNumbers(int n){
        if(next==null) return false;
        while(!next.getNumber(n));
        return true;
    }

    private void strain(int n){
        if(n%primeNumber!=0)
            if (!sendNumbers(n)) {
                next = new SieveThread(n);
                next.run();
            }
    }


    public void run(){
        while(true){
            strain(buffer.poll());
        }
    }
}
