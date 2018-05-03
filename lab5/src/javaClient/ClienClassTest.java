package javaClient;

public class ClienClassTest {
    public static  void main(String [] args){
        try {
            ClientClass cc = new ClientClass();
            for (int i = 0; i < 10; i++) {
                System.err.println("Sending request "+i+": ");
                cc.send_request("localhost", 9000, "SIGN UP user" + i+"\n");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
