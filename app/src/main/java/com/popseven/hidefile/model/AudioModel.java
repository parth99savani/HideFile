package com.popseven.hidefile.model;

import java.util.ArrayList;

public class AudioModel {
    String strFolder;
    ArrayList<String> audiopathList;

    public String getStrFolder() {
        return strFolder;
    }

    public void setStrFolder(String strFolder) {
        this.strFolder = strFolder;
    }

    public ArrayList<String> getAudiopathList() {
        return audiopathList;
    }

    public void setAudiopathList(ArrayList<String> audiopathList) {
        this.audiopathList = audiopathList;
    }
}
