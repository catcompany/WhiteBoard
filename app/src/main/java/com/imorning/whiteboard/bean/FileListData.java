package com.imorning.whiteboard.bean;


public class FileListData {
    private String title;
    private String filePath;

    public FileListData(String fileName, String filePath) {
        this.filePath = filePath;
        this.title = fileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}