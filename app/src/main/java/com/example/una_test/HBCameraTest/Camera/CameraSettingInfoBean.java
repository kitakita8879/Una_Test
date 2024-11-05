package com.example.una_test.HBCameraTest.Camera;

public class CameraSettingInfoBean {
    private String type;
    private String param;
    private String options;
    private String permission;
    private String limitLength;
    private String limitCharacter;
    private Boolean allowEmptyPassword;

    public String getType() {
        return type;
    }

    public String getParam() {
        return param;
    }

    public String getOptions() {
        return options;
    }

    public String getPermission() {
        return permission;
    }

    public String getLimitLength() {
        return limitLength;
    }

    public String getLimitCharacter() {
        return limitCharacter;
    }

    public Boolean getAllowEmptyPassword() {
        return allowEmptyPassword;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setLimitLength(String limitLength) {
        this.limitLength = limitLength;
    }

    public void setLimitCharacter(String limitCharacter) {
        this.limitCharacter = limitCharacter;
    }

    public void setAllowEmptyPassword(Boolean allowEmptyPassword) {
        this.allowEmptyPassword = allowEmptyPassword;
    }
}
