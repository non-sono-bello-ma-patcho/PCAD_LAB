package ConnectionPool;

import com.sun.corba.se.pept.transport.Connection;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Future;

public class ConnectionPoolTest {
    public static void main(String [] args){
        // init connection pool:
        ConnectionPoolClass cpool = new ConnectionPoolClass(25);
        String[] urls = {"https://github.com", "https://github.com/rollingflamingo", "https://github.com/non-sono-bello-ma-patcho", "https://github.com/PageFaultHandler"};
        // what to do? ask to user for connections?
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()) {
            String myUrl = sc.next();
            if(myUrl.equals("exit")) break;
            System.out.println("Ok, sending requests:");
            Future<Integer>[] f = new Future[4];
            try {
                for(int i = 0; i<4; i++) {
                    f[i]= cpool.OpenConnection(urls[i]);

                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("an error occured");
            }
        }
        sc.close();
        cpool.StopPool();
    }
}
