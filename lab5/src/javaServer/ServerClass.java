package javaServer;

import myPool.PoolClass;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.*;

public class ServerClass {
    private class connectionHandler implements Callable {
        Socket client;
        public connectionHandler(Socket connection){
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
                // parse token, should I creater a parser?
                switch(st.nextToken()){
                    case "SIGN":
                        switch(st.nextToken()) {
                            case "UP":
                                if (addUser(st.nextToken())) {
                                    response ="SIGN UP SUCCESS\n";
                                } else {
                                    response = "SIGN UP FAIL\n";
                                }
                                break;
                            case "OFF":
                                if (delUser(st.nextToken())) {
                                    response = "SIGN OFF SUCCESS\n";
                                } else {
                                    response = "SIGN OFF FAIL\n";
                                }
                                break;
                            default:
                                response = "BAD REQUEST. CLOSING CONNECTION\n";
                        }
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

    private ServerSocket listener;
    private PoolClass pool;
    private List<String> clientList;

    protected synchronized boolean addUser(String uID){
        if(!clientList.contains(uID)) clientList.add(uID);
        else return false;
        return true;
    }

    protected synchronized boolean delUser(String uID){
        if(clientList.contains(uID)) clientList.remove(uID);
        else return false;
        return true;
    }

    public ServerClass() throws IOException {
        pool = new PoolClass(5);
        listener = new ServerSocket(9000);
        clientList = new ArrayList<>();
        listener.setReuseAddress(true);
    }

    public ServerClass(int timeout) throws IOException {
        this();
        listener.setSoTimeout(timeout*1000);
    }

    private void printUsers(){
        if (!clientList.isEmpty()) {
            System.out.println("REGISTERED USERS:");
            for (String u : clientList) System.out.println(u);
        } else System.err.println("No users registered");
    }

    public Future<String> openConnection(Socket accepted){
        connectionHandler ch = new connectionHandler(accepted);
        return pool.submit(ch);
    }

    public void start() throws IOException, ExecutionException, InterruptedException {
        List<Future<String>> threads = new ArrayList<>();
        System.err.println("SERVER:\tStarting to listen on port 9000...");
        while(true) try {
            threads.add(openConnection(listener.accept()));
        } catch (SocketTimeoutException e) {
            System.err.println("SERVER:\tTime expired... Closing connections...");
            printUsers();
            for (Future<String> f : threads) {
                f.get();
            }
            pool.StopPool();
            listener.close();
            break;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
