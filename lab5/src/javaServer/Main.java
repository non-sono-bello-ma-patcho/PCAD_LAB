package javaServer;


import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public  static void main(String[]args)
    {
        if(args.length!=1)
        {
            System.out.println("USAGE ./program <port>");
            return;
        }
        try {
            ServerJava server = new ServerJava(Integer.parseInt(args[0]));
            ExecutorService threadPool= Executors.newFixedThreadPool(5);
            ServerSocket socket =server.getServerSocket();
            Socket temp;

            while(true)
            {
                temp=socket.accept();
                threadPool.submit(new ConnectionManager(server,temp));

            }


        }catch(Exception exc)
        {
            exc.printStackTrace();
            return;
        }
    }



}
