package javaServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.*;

public class ServerClass {
    private ServerSocket listener;
    private Socket connection;
    private ExecutorService pool;
    private List<String> clientList;

    private synchronized boolean addUser(String uID){
        if(!clientList.contains(uID)) clientList.add(uID);
        else return false;
        return true;
    }

    private synchronized boolean delUser(String uID){
        if(clientList.contains(uID)) clientList.remove(uID);
        else return false;
        return true;
    }

    private class connectionManager implements Callable {
        Socket client;
        public connectionManager(Socket connection){
            client = connection;
        }

        @Override
        public String call() throws IOException {
            String response = null;
            try {
                // open streams so client and server can communicate:
                BufferedReader bIn = new BufferedReader(new InputStreamReader(client.getInputStream())); // buffered inpustream was too difficult to manage for this purpose;
                PrintWriter bOut = new PrintWriter(client.getOutputStream(), true); // have method similar to BufferedReader...
                // tell client Server is ready to receive userID:
                bOut.write("ACC\n");
                bOut.flush();
                String req = bIn.readLine(); // get the request line:
                StringTokenizer st = new StringTokenizer(req); // creates token out of a string:
                // parse token:
                switch(st.nextToken()){
                    case "SIGN":
                        switch(st.nextToken()) {
                            case "UP":
                                if (addUser(st.nextToken())) {
                                    // send acknowledgement message:
                                    response ="SIGN UP SUCCESS\n";
                                } else {
                                    // send acknowledgement message:
                                    response = "SIGN UP FAIL\n";
                                }
                                break;
                            case "OFF":
                                if (delUser(st.nextToken())) {
                                    // send acknowledgement message:
                                    response = "SIGN OFF SUCCESS\n";
                                } else {
                                    // send acknowledgement message:
                                    response = "SIGN OFF FAIL\n";
                                }
                                break;
                            default:
                                response = "BAD REQUEST. CLOSING CONNECTION\n";
                        }
                    case "CONNECT":
                        // poi lo faccio, giuro;
                        break;
                    default:
                        response = "BAD REQUEST. CLOSING CONNECTION\n";
                }
                bOut.write(response);
                bOut.flush();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            client.close();
            System.err.println("DONE");
            return response;
        }
    }

    public ServerClass() throws IOException {
        pool = Executors.newFixedThreadPool(5);
        listener = new ServerSocket(9000);
        clientList = new ArrayList<>();
        listener.setReuseAddress(true);
    }

    public ServerClass(int timeout) throws IOException {
        this();
        listener.setSoTimeout(timeout*1000);
    }

    public Future<String> openConnection(Socket accepted) throws IOException {
        // pass connection to a thread:
        connectionManager cm = new connectionManager(accepted);
        return pool.submit(cm);
    }

    public void start() throws IOException, ExecutionException, InterruptedException {
        List<Future<String>> threads = new ArrayList<>();
        try{
            System.err.println("SERVER:\tStarting to listen on port 9000...");
            while(true){
                Socket conn = listener.accept();
                threads.add(openConnection(conn));
            }
        }
        catch (SocketTimeoutException e) {
            System.err.println("SERVER:\tTime expired... Closing connections...");
            if (!clientList.isEmpty()) {
                System.out.println("REGISTERED USERS:");
                for (String u : clientList) System.out.println(u);
            }
            else System.err.println("No users registered");
            for (Future<String> f: threads) {
                f.get();
            }
            StopPool();
            listener.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void StopPool(){
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
