package ConnectionPool;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.*;

public class ConnectionPoolClass {
    private ExecutorService pool;

    Future<Integer> OpenConnetcion(String myUrl) throws Exception{
        class connectionCaller implements Callable<Integer>{
            public Integer call() throws Exception {
                URL url = new URL(myUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                return connection.getResponseCode();
            }
        }
        connectionCaller mycallable = new connectionCaller();
        return pool.submit(mycallable);
    }

    void StopPool(){
        pool.shutdown();
    }



}
