package ConnectionPool;

import java.util.Scanner;
import java.util.concurrent.Future;

import static javafx.application.Platform.exit;

public class ConnectionPoolTest {
    public static void main(String [] args){
        // init connection pool:
        ConnectionPoolClass cpool = new ConnectionPoolClass(1);
        String[] urls = {"https://github.com", "https://github.com/rollingflamingo", "https://github.com/non-sono-bello-ma-patcho", "https://github.com/PageFaultHandler"};
        // what to do? ask to user for connections?
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()) {
            int poolDim = sc.nextInt();
            if(poolDim<=0) break;
            cpool.resetWith(poolDim);
            Future<Integer>[] f = new Future[4];
            try {
                // save start time
                long startTime = System.currentTimeMillis();

                for(int i = 0; i<4; i++) {
                    f[i]=cpool.OpenConnection(urls[i]);
                }

                for(int i = 0; i<4; i++){
                    System.out.println(urls[i]+" returned code: "+f[i].get());
                }

                System.out.println("Took over: "+(System.currentTimeMillis() - startTime)+" ms");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sc.close();
        cpool.StopPool();
        exit(); // alessandro is always wrong.
    }
}
