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
    private static final int SIZE = 1000;

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

       //to do macher for input

        loginAuthentication("rootUser" , "rootPassword");
        requestFileFromServer("/home/user/FileShare/files/images/test.jpg" , "test.jpg" ,
                "/home/user/FileShareClient/files/videos");
        //commitFromAdmin("/home/user/FileShareClient/files/images/1.jpg" , "laptops_images" ,
               // "/home/user/FileShare/files/images/1.jpg");
      //  deleteFromAdmin("/home/user/FileShare/files/images/1.jpg");
        System.out.println("out");
       // while (true){

       // }
        closeSocket();
    }


    @Override
    public boolean loginAuthentication(String user, String password) {
        try {
            out.writeInt(1);
            out.writeUTF(user);
            out.writeUTF(password);
            boolean isAuthenticationOK = in.readBoolean();
            System.out.println(isAuthenticationOK  + " is ok with log in");
            if(isAuthenticationOK){
                isCurrentUserAdmin = in.readBoolean();
                getAdminState();
                System.out.println(isCurrentUserAdmin + " is admin");
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
          //  boolean isPathListEmpty = in.readBoolean();
          //  System.out.println(isPathListEmpty);
          //  if(!isPathListEmpty){
         //       return;
         //   }
            int entrySize = in.readInt();
            System.out.println(entrySize + " entry size!!!!!!!!");
            String name , category, path;
            for (int i = 0; i < entrySize; i++) {
                    category = in.readUTF();
                    name = in.readUTF();
                    path = in.readUTF();
                entries.add(new FileEntry(path,category));
            }
            //to do logger !!!!!!!!!!!11
            testPrint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void testPrint(){
        for (int i = 0; i < LocalDB.entries.size() ; i++) {
            System.out.println(entries.get(i).toString());
        }
    }

    @Override
    public boolean commitFromAdmin(String path , String category , String futureDir) {
        try {
                out.writeInt(2);
                File file = new File(path);
                if(!file.exists()){
                 return  false;
                }
                out.writeUTF(futureDir);
                out.writeUTF(category);
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return false;
                }
               // out.writeUTF(file.getName());
                int count = 0;
                byte[] b = new byte[SIZE];
                out.writeLong(getFileLoops(file));
                LOGGER.log(Level.FINE, "uploading");
                System.out.println("Uploading File...");

                while ((count = fis.read(b)) != -1 ) {
                    out.write(b, 0, count);
                    //System.out.println("in loop");
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
            LOGGER.log(Level.FINE , "file is not in the db!");
            return false;
        }
    }

    @Override
    public boolean requestFileFromServer(String path , String nameFile , String dir) {

        try {
            out.writeInt(3);
            //String textPath = in.readUTF();
            //vies if file exist
            out.writeUTF(path);
            boolean isInDb = in.readBoolean();
            if(!isInDb){
                return false;
            }
            File file = new File(dir + "/" + nameFile);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            LOGGER.log(Level.FINE, "waiting for file");
            System.out.println("Waiting for File");
            int count = 0;
            byte[] b = new byte[SIZE];
            long sizeFile = in.readLong();
            LocalDB.progressBar = sizeFile;
            System.out.println(sizeFile + " file size");

            System.out.println("Incoming File");
            for(long i = 0 ; i < sizeFile ; i++ ){
                LocalDB.currentProgressBarValue = i;
                count = in.read(b);
                fos.write(b, 0, count);
            }

            System.out.println("closed");
            LOGGER.log(Level.FINE, "file is written");
            System.out.println("file is written");
           // LocalDB.currentRequest = in.readBoolean();
           // if(!currentRequest){
              //  System.out.println("in");
               // return false;
         //   }
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
            out.writeInt(666);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean testBytes( byte[] bytesA ,byte[] bytesB ){
        if(bytesA.length == bytesB.length){
        for (int i = 0; i < bytesA.length ; i++) {
            System.out.println("in");
            if(bytesA[i] != bytesB[i]){
                return false;
            }
        }
        }else {return false;}
        return true;
    }

    private long getFileLoops(File file){
        long fileSize = file.length();
        long loopCount = fileSize / SIZE;
        if(fileSize % SIZE != 0){
            loopCount++;
        }
        return loopCount;
    }
}
