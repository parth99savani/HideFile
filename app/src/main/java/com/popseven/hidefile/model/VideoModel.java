package com.popseven.hidefile.model;

import java.util.ArrayList;

public class VideoModel {
    String strFolder;
    ArrayList<String> videopathList;

    public String getStrFolder() {
        return strFolder;
    }

    public void setStrFolder(String strFolder) {
        this.strFolder = strFolder;
    }

    public ArrayList<String> getVideopathList() {
        return videopathList;
    }

    public void setVideopathList(ArrayList<String> videopathList) {
        this.videopathList = videopathList;
    }
}
