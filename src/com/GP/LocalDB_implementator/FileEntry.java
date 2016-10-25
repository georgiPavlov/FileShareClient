package com.GP.LocalDB_implementator;

/**
 * Created by user on 10/26/16.
 */
public class FileEntry {
    private String name;
    private String path;
    private String categorie;

    public FileEntry(String path, String categorie) {
        this.path = path;
        this.categorie = categorie;
        setName(path);

    }

    public void setName(String path) {
        String[] nameArray = path.split("\\/");
        this.name = nameArray[nameArray.length-1];
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getCategorie() {
        return categorie;
    }
}
