package com.ljc.workprogress.domain.vo;

import java.util.List;

public class HolidayResultVo {
    private Integer code;
    private String msg;

    private List<HolidayVo> data;

    public HolidayResultVo() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<HolidayVo> getData() {
        return data;
    }

    public void setData(List<HolidayVo> data) {
        this.data = data;
    }
}
