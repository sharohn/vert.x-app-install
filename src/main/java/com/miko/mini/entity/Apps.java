package com.miko.mini.entity;

public class Apps {

    private int id;

    private String appName;

    private String version;

    public Apps() {
    }

    public Apps(int id, String appName, String version) {
        this.id = id;
        this.appName = appName;
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Apps{" +
            "id=" + id +
            ", appName='" + appName + '\'' +
            ", version='" + version + '\'' +
            '}';
    }
}
