package javaServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerClass {
    private ServerSocket listener;
    private Socket connection;
    private ExecutorService pool;
    private List<String> clientList;

    private class connectionManager implements Runnable{
        Socket client;
        InputStream in;
        OutputStream out;
        public connectionManager(Socket connection){
            client = connection;
        }

        @Override
        public void run() {
            try {
                PrintWriter outgoing; // Stream for sending data.
                Scanner incomingData = new Scanner(in);
                outgoing = new PrintWriter(out);
                outgoing.println("ACC");
                while(incomingData.hasNext()){
                    String userID = incomingData.next();
                    // put user id in list;
                }
                in = client.getInputStream();
                out = client.getOutputStream();
                // controllo id

                outgoing.flush(); // Make sure the data is actually sent!
                client.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private boolean isInList(String id){
        return false;
    }

    private boolean timeOutExpired(){
        return false;
    }

    public ServerClass() throws IOException {
        pool = Executors.newFixedThreadPool(5);
        listener = new ServerSocket(9000);
    }

    public void start(){
        try{
            while(!timeOutExpired()){
                // pass connection to a thread:
                connection = listener.accept();
                connectionManager cm = new connectionManager(connection);
                cm.run();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
