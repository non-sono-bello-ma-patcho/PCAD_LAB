package RMIShared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SharedClass extends Remote {
    String saySomething(String something)throws RemoteException; // easy right?
}
