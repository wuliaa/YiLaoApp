package com.example.yilaoapp.ui.bulletin;

public class Share {
    private String objectName;
    private String content;
    private String time;
    private int imageId;

    public Share(String objectName, String content, String time, int imageId) {
        this.objectName = objectName;
        this.content = content;
        this.time = time;
        this.imageId = imageId;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
