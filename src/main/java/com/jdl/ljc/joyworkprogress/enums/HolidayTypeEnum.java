package com.jdl.ljc.joyworkprogress.enums;

public enum HolidayTypeEnum {
    WORK_DAY(0,"工作日"),
    REST_DAY(1,"休息日"),
    HOLIDAY(2, "节假日");

    HolidayTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private int code;
    private String name;

    public int getCode() {
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
