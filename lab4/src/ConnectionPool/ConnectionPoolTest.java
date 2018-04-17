package ConnectionPool;

import com.sun.corba.se.pept.transport.Connection;

import java.util.Scanner;
import java.util.concurrent.Future;

public class ConnectionPoolTest {
    public static void main(String [] args){
        // init connection pool:
        ConnectionPoolClass cpool = new ConnectionPoolClass(25);
        // what to do? ask to user for connections?
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()) {
            String myUrl = sc.next();
            if(myUrl.equals("exit")) break;
            try {
                cpool.OpenConnection(myUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sc.close();
        cpool.StopPool();
    }
}
