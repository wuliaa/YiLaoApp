package com.example.yilaoapp.ui.bulletin;

public class Team {
    private String ActivityName;
    private String content;
    private String time;
    private int imageId;

    public Team(String activityName, String content, String time, int imageId) {
        ActivityName = activityName;
        this.content = content;
        this.time = time;
        this.imageId = imageId;
    }

    public String getActivityName() {
        return ActivityName;
    }

    public void setActivityName(String activityName) {
        ActivityName = activityName;
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
