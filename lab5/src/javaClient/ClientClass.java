package javaClient;

import java.io.*;
import java.net.*;
import java.util.concurrent.Callable;

public class ClientClass implements Callable<String> {
    private Socket server;
    private String request;
    public ClientClass(String serverAdress, int port, String req) throws IOException {
        server = new Socket(InetAddress.getByName(serverAdress), port);
        request = req;
    }

    public ClientClass(String serverAdress, int port, String req, int timeout) throws IOException {
        this(serverAdress, port, req);
        server.setSoTimeout(timeout*1000);
    }

    public String call() throws IOException {
        System.err.println("CLIENT:\t" + "Sending request to " + server.toString());
        BufferedReader bIn = new BufferedReader(new InputStreamReader(server.getInputStream())); // buffered inpustream was too difficult to manage for this purpose;
        PrintWriter bOut = new PrintWriter(server.getOutputStream(), true); // have method similar to BufferedReader...);
        String response;
        while(!bIn.ready());
        response = bIn.readLine();
        if (response.equals("ACC")) {
            bOut.write(request);
            bOut.flush();
            response = bIn.readLine();
        }
        server.close();
        return response;
    }
}
