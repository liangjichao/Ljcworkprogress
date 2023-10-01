package com.jdl.ljc.joyworkprogress.enums;

public enum AppVersionEnum {
    PANGU("app_01","融合(盘古)"),
    RF("app_02","融合(RF)"),
    PANGU_5("app_03","5.0(盘古)"),
    RF_5("app_04","5.0(RF)"),
    KA_5("app_05","KA(RF)")
    ;

    AppVersionEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return name;
    }
}
