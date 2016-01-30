package com.example.zhenmin.min_note.model;

import java.io.Serializable;

/**
 * Created by zhenmin on 2015/11/4.
 */
public class Note implements Serializable {

    private int id;
    private String title;
    private String content;
    private String imgPath;
    private String vedioPath;
    private String buildTime;
    private boolean checked;

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getVedioPath() {
        return vedioPath;
    }

    public void setVedioPath(String vedioPath) {
        this.vedioPath = vedioPath;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Note() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }
}
