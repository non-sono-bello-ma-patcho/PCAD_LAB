package erastoteneA;

public class SieveFeeder extends Thread{
    private int toStrain;
    private Sieve toFeed;
    public SieveFeeder(int n, Sieve s){
        toStrain = n;
        toFeed = s;
    }

    public void run(){
        for (int i=2; i<toStrain; i++) toFeed.add(i);
        toFeed.add(-1);
    }
}

