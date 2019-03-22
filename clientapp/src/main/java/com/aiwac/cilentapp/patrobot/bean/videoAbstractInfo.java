package com.aiwac.cilentapp.patrobot.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class videoAbstractInfo implements Serializable {
    private String clientId;    //客户端id
    private String businessType;    //事物类型
    private String clientType;  // 客户端类型 1表示app  2 表示后台  3表示机器人
    private String uuid;    //一次事务

    private String errorCode;
    private String errorDesc;

    private List<videoInfo> LectureCourseAllInfo = new ArrayList<>();   // 一次获取到的json里的讲座摘要信息组



    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getBusinessType() {
        return businessType;
    }
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getClientType() {
        return clientType;
    }
    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String geterrorCode() {
        return errorCode;
    }
    public void seterrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String geterrorDesc() {
        return errorDesc;
    }
    public void seterrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public List<videoInfo> getLectureCourseAbstracts() {
        return LectureCourseAllInfo;
    }
    public void setLectureCourseAbstracts(List<videoInfo> LectureCourseAbstracts) {
        this. LectureCourseAllInfo = LectureCourseAbstracts;
    }
}
