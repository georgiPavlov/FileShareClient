package com.GP.ClientImplementator;

import java.io.*;
import java.net.Socket;

/**
 * Created by user on 10/23/16.
 */
public class ClientImplementor {
    private int port;
    private BufferedWriter out;
    private BufferedReader in;
    private Socket socket;

    public ClientImplementor(int port){
        this.port = port;
        try {
            socket = new Socket("localhost",port);
            createStreams();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private  void createStreams() throws IOException{

        in =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
        out =
                    new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream()));

        System.out.println(in.readLine());



    }

    private void closeStreams(){
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startCommunication(){
        ClientCommunicator communicator = new ClientCommunicator(out,in);
        communicator.startCommucateWithServer();

        closeStreams();
    }


}