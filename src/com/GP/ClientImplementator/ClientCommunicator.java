package com.GP.ClientImplementator;

import com.GP.LocalDB_implementator.FileEntry;
import com.GP.LocalDB_implementator.LocalDB;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.GP.LocalDB_implementator.LocalDB.currentRequest;
import static com.GP.LocalDB_implementator.LocalDB.entries;

/**
 * Created by user on 10/23/16.
 */
public class ClientCommunicator implements IClientCommunicator{
    private DataOutputStream out;
    private DataInputStream in;
    private boolean isCurrentUserAdmin = false;
    private static final Logger LOGGER = Logger.getLogger( ClientImplementor.class.getName() );

    public ClientCommunicator(DataOutputStream out, DataInputStream in) {
        this.out = out;
        this.in = in;
    }

    public boolean getAdminState(){
        LocalDB.isAdmin = isCurrentUserAdmin;
        return  isCurrentUserAdmin;
    }

    public void startCommunicateWithServer() throws IOException {
       /* String fileName = in.readUTF();

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
        fos.close();*/
    }


    @Override
    public boolean loginAuthentication(String user, String password) {
        try {
            out.write(1);
            out.writeUTF(user);
            out.writeUTF(password);
            boolean isAuthenticationOK = in.readBoolean();
            if(isAuthenticationOK){
                isCurrentUserAdmin = in.readBoolean();
                getAdminState();
                getEntriesFromServerAPP();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void getEntriesFromServerAPP() {
        try {
            boolean isPathListEmpty = in.readBoolean();
            if(!isPathListEmpty){
                return;
            }
            int entrySize = in.readInt();
            String name , category, path;
            for (int i = 0; i < entrySize; i++) {
                    category = in.readUTF();
                    name = in.readUTF();
                    path = in.readUTF();
                entries.add(new FileEntry(path,category));
            }
            //to do logger !!!!!!!!!!!11
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean commitFromAdmin(String path , String category) {
        try {
                out.writeInt(2);
                out.writeUTF(path);
                out.writeUTF(category);
                File file = new File(path);
                if(!file.exists()){
                 return  false;
                }
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return false;
                }
               // out.writeUTF(file.getName());
                int count = 0;
                byte[] b = new byte[1000];
                LOGGER.log(Level.FINE, "uploading");
                System.out.println("Uploading File...");

                while ((count = fis.read(b)) != -1) {
                    out.write(b, 0, count);
                }
                LOGGER.log(Level.FINE, "upload finished");
                System.out.println("upload finished");
                LocalDB.currentRequest = in.readBoolean();
                if(!currentRequest){
                    return false;
                }
                fis.close();

                //unlock writer
                return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        //LOGGER.log(Level.FINE , "file is not in the db!");

       // return false;
    }

    @Override
    public boolean requestFileFromServer(String path) {

        try {
            out.writeInt(3);
            String textPath = in.readUTF();
            //vies if file exist
            boolean isInDb = in.readBoolean();
            if(!isInDb){
                return false;
            }
            File file = new File(textPath);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            LOGGER.log(Level.FINE, "waiting for file");
            System.out.println("Waiting for File");
            int count = 0;
            byte[] b = new byte[1000];
            System.out.println("Incoming File");
            while((count = in.read(b)) != -1){
                fos.write(b, 0, count);
            }
            LOGGER.log(Level.FINE, "file is written");
            LocalDB.currentRequest = in.readBoolean();
            if(!currentRequest){
                return false;
            }
            fos.close();
            System.out.println("fos closed");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
        //custom unlock
    }

    @Override
    public boolean deleteFromAdmin(String textPath) {
        try {
            out.writeInt(4);
            out.writeUTF(textPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            LocalDB.currentRequest =  in.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentRequest;
    }


    @Override
    public void closeSocket() {
        try {
            out.write(666);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
