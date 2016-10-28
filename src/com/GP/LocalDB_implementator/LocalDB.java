package com.GP.LocalDB_implementator;

import java.util.ArrayList;

/**
 * Created by user on 10/26/16.
 */
public class LocalDB {
    public static ArrayList<FileEntry> entries = new ArrayList<>();
    public static boolean isAdmin = false;
    public static boolean currentRequest = false;
    public static long progressBar=0;
    public static long currentProgressBarValue=0;
}
