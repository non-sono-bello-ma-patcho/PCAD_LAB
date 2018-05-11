package lab7_rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/* that interface must be shared for both server and client classes */

public interface ServerSays extends Remote {
    public String Say(String msg, String name )  throws RemoteException;

}
