package javaClient;

import java.io.*;
import java.net.*;

public class ClientClass {
    public void send_request(String serverAdress, int port, String request) throws IOException {
        Socket server = new Socket(InetAddress.getByName(serverAdress), port);
        System.err.println("CLIENT:\t" + "Sending request to " + server.toString() + "...");
        BufferedReader bIn = new BufferedReader(new InputStreamReader(server.getInputStream())); // buffered inpustream was too difficult to manage for this purpose;
        PrintWriter bOut = new PrintWriter(server.getOutputStream(), true); // have method similar to BufferedReader...);
        String response;
        while(!bIn.ready());
        response = bIn.readLine();
        if (response.equals("ACC")) {
            bOut.write(request);
            bOut.flush();
            String resp="", tmp;
            while((tmp = bIn.readLine())!=null){
                resp += tmp;
                resp+=" ";
            }
            System.err.println("Got response:\t" + resp);
        }
    }
}
