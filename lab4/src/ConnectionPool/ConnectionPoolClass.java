package ConnectionPool;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.*;

public class ConnectionPoolClass {
    private ExecutorService pool;


    // maybe I should define a constructor?
    public ConnectionPoolClass(int poolSize){
        pool = Executors.newFixedThreadPool(poolSize);
    }

    // create a callable and submit it to pool:
    public Future<Integer> OpenConnection(String myUrl) throws Exception{
        class connectionCaller implements Callable<Integer>{
            public Integer call() throws Exception {
                URL url = new URL(myUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader read = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = read.readLine();
                String html = "";
                while(line!=null) {
                    html += line;
                    html+='\n';
                    line = read.readLine();
                }
                System.out.println(html);
                return connection.getResponseCode();
            }
        }
        System.err.print("creating callable:");
        connectionCaller mycallable = new connectionCaller();
        System.err.println("DONE");
        return pool.submit(mycallable);
    }
    void StopPool(){
        System.err.println("Stopping pool");
        pool.shutdown();
    }
}
