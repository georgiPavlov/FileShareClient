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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileEntry fileEntry = (FileEntry) o;

        if (!name.equals(fileEntry.name)) return false;
        if (!path.equals(fileEntry.path)) return false;
        return categorie.equals(fileEntry.categorie);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + path.hashCode();
        result = 31 * result + categorie.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "FileEntry{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", categorie='" + categorie + '\'' +
                '}';
    }
}
