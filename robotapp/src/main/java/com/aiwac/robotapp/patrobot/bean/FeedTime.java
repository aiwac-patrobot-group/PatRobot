package com.aiwac.robotapp.patrobot.bean;

public class FeedTime {
    protected String clientID;
    protected String businessType;
    protected String clientType;
    protected String uniqueID;
    protected String time;
    protected String timePoints[];
    protected String autoType;


    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
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

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String[] getTimePoints() {
        return timePoints;
    }

    public void setTimePoints(String timePoints[]) {
        this.timePoints = timePoints;
    }
    public String getAutoType() {
        return autoType;
    }

    public void setAutoType(String clientID) {
        this.autoType = autoType;
    }
}
