package com.example.una_test.HBCameraTest.Camera;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FNCamera implements Camera {
//    private final FNRemoteCamera mFNRemoteCamera;

    public FNCamera() {
//        mFNRemoteCamera = new FNRemoteCamera();
    }

    @Override
    public CameraBean connect(String ip) {
//        HashMap<String, Object> fnResponseMap = mFNRemoteCamera.connect(ip, true);
//        return resultToBean(fnResponseMap);

        CameraBean result = new CameraBean();
        result.setErrorCode(-1);
        return result;
    }

    @Override
    public boolean disconnect() {
//        HashMap<String, Object> response = mFNRemoteCamera.disconnect();
//        Integer errorCode = (Integer) response.get(FNConstants.KEY_ERROR_CODE);
//        return errorCode != null && errorCode == 0;
        return false;
    }

    private CameraBean resultToBean(@NonNull HashMap<String, Object> fnResponseMap) {
        CameraBean cameraBean = new CameraBean();
        Integer errorCode = null;//(Integer) fnResponseMap.get(FNConstants.KEY_ERROR_CODE);
        cameraBean.setErrorCode(errorCode != null ? errorCode : -1);
        if (cameraBean.getErrorCode() != 0) {
            return cameraBean;
        }
        cameraBean.setPlatform((String) fnResponseMap.get("platform"));
        cameraBean.setChip((String) fnResponseMap.get("chip"));
        cameraBean.setApiVer((String) fnResponseMap.get("api_ver"));
        cameraBean.setFwVer((String) fnResponseMap.get("fw_ver"));
        cameraBean.setModel((String) fnResponseMap.get("model"));
        cameraBean.setBrand((String) fnResponseMap.get("brand"));
        cameraBean.setSdkVer((String) fnResponseMap.get("sdk_ver"));
        cameraBean.setRootPath((String) fnResponseMap.get("root_path"));
//        List<?> settings = (List<?>) fnResponseMap.get(FNConstants.KEY_SETTINGINFOS);
//        cameraBean.setSettinginfos(setSettingInfo(settings));
        return cameraBean;
    }

    private List<CameraSettingInfoBean> setSettingInfo(List<?> settings) {
        if (settings == null) {
            return null;
        }
        List<CameraSettingInfoBean> settingInfoBeans = new ArrayList<>();
        for (int i = 0; i < settings.size(); i++) {
            HashMap<?, ?> setting = (HashMap<?, ?>) settings.get(i);
            CameraSettingInfoBean infoBean = new CameraSettingInfoBean();
//            infoBean.setType((String) setting.get(FNConstants.KEY_TYPE));
//            infoBean.setParam((String) setting.get(FNConstants.KEY_PARAMETER));
//            infoBean.setOptions((String) setting.get(FNConstants.KEY_OPTIONS));
//            infoBean.setPermission((String) setting.get(FNConstants.KEY_PERMISSION));
//            infoBean.setLimitLength((String) setting.get(FNConstants.KEY_LIMIT_LENGTH));
//            infoBean.setLimitCharacter((String) setting.get(FNConstants.KEY_LIMIT_CHARACTER));
//            infoBean.setAllowEmptyPassword((Boolean) setting.get(FNConstants.KEY_ALLOW_EMPTY_PASSWORD));
            settingInfoBeans.add(infoBean);
        }
        return settingInfoBeans;
    }
}
