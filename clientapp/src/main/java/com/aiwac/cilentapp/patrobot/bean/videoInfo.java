package com.aiwac.cilentapp.patrobot.bean;


import android.graphics.Bitmap;

import java.io.Serializable;

// 存放视频 音频等摘要信息
public class videoInfo implements Serializable {


    protected String type;
    protected String infoID;
    protected String title;
    protected String description;
    protected String cover;
    protected String link;

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }


    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getInfoID() {
        return infoID;
    }
    public void setLectureID(String lectureID) {
        this.infoID = lectureID;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover() {
        return cover;
    }
    public void setCover(String cover) {
        this.cover = cover;
    }

}
