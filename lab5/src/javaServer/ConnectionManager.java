package javaServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;


public class ConnectionManager implements Runnable{

    private StringTokenizer tokenizer;
    private String userid;
    private String protocoltype;
    private ServerJava server;
    private Socket socket;

    public  ConnectionManager(ServerJava server,Socket socket)
    {
        if(server==null||socket==null)
            throw new NullPointerException();
        this.server=server;
        this.socket=socket;
    }

    @Override
    public void run()
    {
        try {
            this.manageConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void manageConnection()throws Exception//deve essere passata una copia del messagio
    {
        PrintWriter outBuff =
                new PrintWriter(
                        socket.getOutputStream(), true);
        BufferedReader inbuff =
                new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String message=inbuff.readLine();
        tokenizer=new StringTokenizer(message);



        protocoltype=tokenizer.nextToken();
        switch(protocoltype)
        {
            case "REQUEST_USER_REGISTRATION":
                userid=tokenizer.nextToken();
                if(server.isMemberOfUserList(userid))
                {
                    //refuse
                    outBuff.println("REFUSED_ALREADY_PRESENT");
                }
                server.addUserToUsersList(userid);
                server.associateUsertoSocket(userid,socket);
                //OK
                outBuff.println("OKE");
                break;
            case"CONNECT":
                userid=tokenizer.nextToken();
                if(server.isMemberOfUserList(userid))
                {
                    server.associateUsertoSocket(userid,socket);
                    outBuff.println("OKE");
                }
                else
                {
                    //REFUSE
                    outBuff.println("REFUSED_NOT_PRESENT_IN_THE_USERS_LIST");
                }

                break;
            default:


        }

    }

}
