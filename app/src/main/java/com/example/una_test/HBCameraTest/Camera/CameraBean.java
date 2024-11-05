package com.example.una_test.HBCameraTest.Camera;

import java.util.List;

public class CameraBean {
    private int errorCode;
    private String platform;
    private String chip;
    private String apiVer;
    private String fwVer;
    private String model;
    private String brand;
    private String rootPath;
    private String sdkVer;
    private List<CameraSettingInfoBean> settinginfos;

    public CameraBean() {

    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getPlatform() {
        return platform;
    }

    public String getChip() {
        return chip;
    }

    public String getApiVer() {
        return apiVer;
    }

    public String getFwVer() {
        return fwVer;
    }

    public String getModel() {
        return model;
    }

    public String getBrand() {
        return brand;
    }

    public String getRootPath() {
        return rootPath;
    }

    public String getSdkVer() {
        return sdkVer;
    }

    public List<CameraSettingInfoBean> getSettinginfos() {
        return settinginfos;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setChip(String chip) {
        this.chip = chip;
    }

    public void setApiVer(String apiVer) {
        this.apiVer = apiVer;
    }

    public void setFwVer(String fwVer) {
        this.fwVer = fwVer;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public void setSdkVer(String sdkVer) {
        this.sdkVer = sdkVer;
    }

    public void setSettinginfos(List<CameraSettingInfoBean> settinginfos) {
        this.settinginfos = settinginfos;
    }
}
