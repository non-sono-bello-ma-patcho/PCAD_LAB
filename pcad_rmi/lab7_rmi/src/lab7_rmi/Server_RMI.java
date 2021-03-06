package lab7_rmi;


import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Server_RMI   implements ServerSays {


    /**
	 * 
	 */
	/*private static final long serialVersionUID = 1L;*/

	

	public String Say(String msg, String name)throws RemoteException {
        return "Hello " + name + " your message is " + msg;
    }

    public static void main(String[] args){
        try{
            Server_RMI myServer = new Server_RMI();
            ServerSays Stub = (ServerSays) UnicastRemoteObject.exportObject(myServer,0);
            LocateRegistry.createRegistry(1099);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("Message", Stub); /* first parameter must be changed */
            System.err.println("Now the server is ready...");
            
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}

