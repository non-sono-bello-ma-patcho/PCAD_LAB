package RMIServer;

import RMIShared.SharedClass;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServerClass extends UnicastRemoteObject implements SharedClass {
    int RMIPortNum;

    protected RMIServerClass() throws RemoteException {
        super();
    }
     // retrieve or create registry handler:
    void getRegistry() throws RemoteException {
        try{
            Registry reg = LocateRegistry.getRegistry(RMIPortNum);
        }
        catch (RemoteException re){
            Registry reg = LocateRegistry.createRegistry(RMIPortNum);
        }
    }

    void publishObject(){
    }

    @Override
    public String saySomething(String something) {
        return "Server said: "+something;
    }

    public static void main(String [] args){

    }
}
