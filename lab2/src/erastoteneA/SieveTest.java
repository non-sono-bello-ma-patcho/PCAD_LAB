package erastoteneA;

public class SieveTest {
    static public void printPrime(Sieve s){
        Sieve tmp = s;
        do{
            System.out.println(tmp.getAssociatedPrimeNumber());
            tmp = s.getNext();
        }while(tmp!= null);
    }
    public static void main(String [] args){
        Sieve s = new Sieve(2); // init first thread;
        SieveFeeder sf = new SieveFeeder(100000, s);

        sf.start();
        s.start();
    }
}
