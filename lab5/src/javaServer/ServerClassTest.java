package javaServer;

import javaClient.ClientClass;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class ServerClassTest {
    public static void main(String [] args){
        try {
            ServerClass sc = new ServerClass(30); // inverti questa riga con quella di sopra MERDACCIA simpaticissima. adesso vediamo se vedrai questi commenti
            // sei un pirla perchè nel costruttore del client prova a connettersi ad un socket che non esiste.....
            sc.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
