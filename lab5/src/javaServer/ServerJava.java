package javaServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerJava
{
    private ConcurrentLinkedQueue<String>listaUsers;
    private ConcurrentHashMap<String,Socket> activeConnessions;
    private ServerSocket serverSocket;
    public ServerJava(int port) throws IOException
    {
        serverSocket=new ServerSocket(port);
        listaUsers=new ConcurrentLinkedQueue<>();
        activeConnessions=new ConcurrentHashMap<>();
    }
    public synchronized void addUserToUsersList(String userid) throws NullPointerException
    {
        if(userid==null) throw new NullPointerException();
        if(listaUsers.contains(userid))
        {
            return;
        }
        listaUsers.add(userid);
    }

    public synchronized void removeUserToUsersList(String userid)
    {
        if(listaUsers.contains(userid))
        {
            listaUsers.remove(userid);
        }
        return;
    }
    public boolean isMemberOfUserList(String userid)
    {
     return listaUsers.contains(userid)  ;
    }
    public boolean isMemberOfActiveConnectionsList(String userid)
    {
        return activeConnessions.containsKey(userid);
    }
    public synchronized void associateUsertoSocket(String userid,Socket socket)
    {
        if(!listaUsers.contains(userid))
        {
            this.addUserToUsersList(userid);
        }
        this.activeConnessions.putIfAbsent(userid,socket);

    }
    public synchronized void removeAssociation(String userid)
    {
        Socket socket=activeConnessions.get(userid);
        if(socket==null)return;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        activeConnessions.remove(userid);
    }
    public synchronized String usersListToString()
    {
        return listaUsers.toString();
    }
    public synchronized String  activeConnectionListToString()
    {
        return activeConnessions.toString();
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }
}
