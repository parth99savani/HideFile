package com.popseven.hidefile.model;

import java.util.ArrayList;

public class DocModel {
    String strFolder;
    ArrayList<String> docpathList;

    public String getStrFolder() {
        return strFolder;
    }

    public void setStrFolder(String strFolder) {
        this.strFolder = strFolder;
    }

    public ArrayList<String> getDocpathList() {
        return docpathList;
    }

    public void setDocpathList(ArrayList<String> docpathList) {
        this.docpathList = docpathList;
    }
}
