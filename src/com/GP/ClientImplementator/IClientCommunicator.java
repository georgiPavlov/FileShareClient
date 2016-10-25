package com.GP.ClientImplementator;

/**
 * Created by user on 10/26/16.
 */
public interface IClientCommunicator {
    boolean loginAuthentication(String user , String password);
    boolean commitFromAdmin(String path , String category);
    boolean requestFileFromServer(String path);
    boolean deleteFromAdmin(String textPath);
    void getEntriesFromServerAPP();
    void closeSocket();
}
