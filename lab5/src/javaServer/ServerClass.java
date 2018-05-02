package javaServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        public String call() {
            String response = null;
            try {
                // open streams so client and server can communicate:
                BufferedReader bIn = new BufferedReader(new InputStreamReader(client.getInputStream())); // buffered inpustream was too difficult to manage for this purpose;
                BufferedWriter bOut = new BufferedWriter(new OutputStreamWriter(client.getOutputStream())); // have method similar to BufferedReader...
                // tell client Server is ready to receive userID:
                bOut.write("ACC\n");
                // try to retrive request:
                String req = bIn.readLine(); // get the request line:
                StringTokenizer st = new StringTokenizer(req); // creates token out of a string:
                // parse token:
                switch(st.nextToken()){
                    case "SIGN UP":
                        if(addUser(st.nextToken())){
                            // send acknowledgement message:
                            bOut.write("Signed up successfully\n");
                        }
                        else{
                            // send acknowledgement message:
                            bOut.write("Impossible to be signed up\n");
                        }
                        break;
                    case "SIGN OFF":
                        if(delUser(st.nextToken())){
                            // send acknowledgement message:
                            bOut.write("Signed off successfully\n");
                        }
                        else{
                            // send acknowledgement message:
                            bOut.write("Impossible to sign off, no such user in server\n");
                        }
                        break;
                    case "CONNECT":
                        // poi lo faccio, giuro;
                        break;
                    default:
                        bOut.write("BAD REQUEST. CLOSING CONNECTION\n");
                        client.close();
                }
                bOut.flush();
                client.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return response;
        }
    }

    private boolean timeExpired(){
        return false;
    }

    public ServerClass() throws IOException {
        pool = Executors.newFixedThreadPool(5);
        listener = new ServerSocket(9000);
        listener.setSoTimeout(10000);
    }

    public void start(){
        try{
            connection = listener.accept();
            while(!timeExpired()){
                // pass connection to a thread:

                connectionManager cm = new connectionManager(connection);
                pool.submit(cm);
            }
        }
        catch (SocketTimeoutException e){
            System.err.println("Time expired... Closing connections...");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
