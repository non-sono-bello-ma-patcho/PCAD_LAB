package javaClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import myPool.PoolClass;

public class ClienClassTest {
    public static  void main(String [] args){
        PoolClass pool = new PoolClass();
        List<Future<String>> threads = new ArrayList<>();
        ClientClass [] cc = new ClientClass[10];
        try {
            for (int i = 0; i < 10; i++) {
                System.err.println("Sending request "+i+": ");
                cc[i]= new ClientClass("localhost", 9000, "SIGN UP user" + i+"\n");
                threads.add(pool.submit(cc[i]));
            }

            for (Future<String> f : threads) f.get();
            pool.StopPool();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
