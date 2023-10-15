package com.jdl.ljc.joyworkprogress.enums;

public enum WorkProgressStatusEnum {
    DEFAULT(0, "待审批"),
    UN_DEV(1, "待开发"),
    DEV_ING(2, "开发中"),
    DEV_FINISH(3, "开发完成"),
    TEST_ING(4, "测试中"),
    TEST_FINISH(5, "测试完成"),
    UAT_ING(6, "UAT中"),
    UAT_FINISH(7, "待上线"),
    ON_LINE_ING(8, "上线中"),
    ON_LINE_FINISH(9, "上线完成");
    private int code;
    private String name;

    WorkProgressStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static WorkProgressStatusEnum queryStatusEnum(int code) {
        for (WorkProgressStatusEnum value : values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return DEFAULT;
    }

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
