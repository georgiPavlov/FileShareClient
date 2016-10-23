package com.GP.ClientImplementator;

import java.io.*;

/**
 * Created by user on 10/23/16.
 */
public class ClientCommunicator {
    private DataOutputStream out;
    private DataInputStream in;

    public ClientCommunicator(DataOutputStream out, DataInputStream in) {
        this.out = out;
        this.in = in;
    }

    public void startCommucateWithServer() throws IOException {
        String fileName = in.readUTF();

           File file = new File("/home/user/FileShareClient/files/videos/" + fileName);
        FileOutputStream fos = null;
        try {
           fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Waiting for File");
        int count = 0;
        byte[] b = new byte[1000];
        System.out.println("Incoming File");
        while((count = in.read(b)) != -1){
            fos.write(b, 0, count);
        }
        fos.close();





    }


}
