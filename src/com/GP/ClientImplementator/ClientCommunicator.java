package com.GP.ClientImplementator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Created by user on 10/23/16.
 */
public class ClientCommunicator {
    private BufferedWriter out;
    private BufferedReader in;

    public ClientCommunicator(BufferedWriter out, BufferedReader in) {
        this.out = out;
        this.in = in;
    }

    public void startCommucateWithServer(){
        try {
            System.out.println(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
