package com.rbhagat.admincg;

public class DustbinModel {

    String DustbinID,DustbinLocation,Status,level,lightStatus,temp,humi,latitude,longitude;


    public DustbinModel(String dustbinID, String dustbinLocation, String status, String level, String lightStatus, String temp, String humi, String latitude, String longitude) {
        DustbinID = dustbinID;
        DustbinLocation = dustbinLocation;
        Status = status;
        this.level = level;
        this.lightStatus = lightStatus;
        this.temp = temp;
        this.humi = humi;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public DustbinModel() {
    }

    public String getDustbinID() {
        return DustbinID;
    }

    public void setDustbinID(String dustbinID) {
        DustbinID = dustbinID;
    }

    public String getDustbinLocation() {
        return DustbinLocation;
    }

    public void setDustbinLocation(String dustbinLocation) {
        DustbinLocation = dustbinLocation;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLightStatus() {
        return lightStatus;
    }

    public void setLightStatus(String lightStatus) {
        this.lightStatus = lightStatus;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getHumi() {
        return humi;
    }

    public void setHumi(String humi) {
        this.humi = humi;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
