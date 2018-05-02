package ConnectionPool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.*;

public class ConnectionPoolClass {

    protected class connectionCaller implements Callable<Integer>{
        private URL url;
        public connectionCaller(String myURL) throws Exception{
            url = new URL(myURL);
        }
        public Integer call() throws Exception {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            //printHtml(connection.getInputStream());
            return connection.getResponseCode();
        }
    }

    private ExecutorService pool;
    private ConcurrentHashMap<String, connectionCaller> cache;

    // maybe I should define a constructor?
    public ConnectionPoolClass(int poolSize){
        pool = Executors.newFixedThreadPool(poolSize);
        cache = new ConcurrentHashMap<>();
    }

    private void printHtml(InputStream in){
        BufferedReader read = new BufferedReader(new InputStreamReader(in));
        String line = null;
        String html = "";
        try {
            line = read.readLine();
            while(line!=null) {
                html += line;
                html+='\n';
                line = read.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(html);
    }

    public void resetWith(int n){
        StopPool();
        pool = Executors.newFixedThreadPool(n);
    }

    // create a callable and submit it to pool:
    public Future<Integer> OpenConnection(String myUrl) throws Exception{
        connectionCaller mycallable = (cache.containsKey(myUrl)? cache.get(myUrl) : new connectionCaller(myUrl));
        System.out.println((cache.containsKey(myUrl)? "Connection to "+myUrl+" already exists" : "A new connection to "+myUrl+" has been created"));
        cache.putIfAbsent(myUrl, mycallable);
        return pool.submit(mycallable);
    }


    void StopPool(){
        pool.shutdown(); // Asking gently to stop the fuckinfg operation ypu bitch.
        try {
            // Wait a while for the bitch to exit (I know where you live...)
            if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Killing the bitch
                // Wait a while for the bitch
                if (!pool.awaitTermination(10, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
        finally {
            pool.shutdownNow();
        }
    }
}
