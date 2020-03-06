package com.popseven.hidefile.model;

import java.util.ArrayList;

public class ImageModel {
    String strFolder;
    ArrayList<String> imagepathList;

    public String getStrFolder() {
        return strFolder;
    }

    public void setStrFolder(String strFolder) {
        this.strFolder = strFolder;
    }

    public ArrayList<String> getImagepathList() {
        return imagepathList;
    }

    public void setImagepathList(ArrayList<String> imagepathList) {
        this.imagepathList = imagepathList;
    }
}
