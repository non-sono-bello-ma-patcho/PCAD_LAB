package lab7_rmi;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class Client_RMI {

	public static void main(String[] args) {
		String host = (args.length < 1) ? null : args[0];
		 try {
		 Registry registry = LocateRegistry.getRegistry(host);
		 ServerSays stub = (ServerSays) registry.lookup("Message");
		 String response = stub.Say(args[1],args[2]);
		 System.out.println(response);
		 } catch (Exception e) {
		 e.printStackTrace();
		 }


	}

}
