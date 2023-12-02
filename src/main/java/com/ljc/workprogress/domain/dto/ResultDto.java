package com.ljc.workprogress.domain.dto;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;

import java.util.List;

public class ResultDto<T> {
    public static final int DEFAULT_RESULT_CODE_FAILD = 999;
    private boolean success;
    private Integer resultCode;
    private T resultValue;
    private String resultMessage;

    public ResultDto() {

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public T getResultValue() {
        return resultValue;
    }

    public void setResultValue(T resultValue) {
        this.resultValue = resultValue;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }


    public static <T> ResultDto<T> toResult(String responseText,Class<T> resultValueClazz) {
        return JSON.parseObject(responseText, new TypeReference<ResultDto<T>>(resultValueClazz) {
        });
    }
    public static <T> ResultDto<List<T>> toResultList(String responseText,Class<T> resultValueClazz) {
        return JSON.parseObject(responseText, new TypeReference<ResultDto<List<T>>>(resultValueClazz) {
        });
    }

    public static <T> ResultDto<T> fail(String resultMessage) {
        ResultDto<T> resultDto = new ResultDto<>();
        resultDto.setSuccess(false);
        resultDto.setResultCode(DEFAULT_RESULT_CODE_FAILD);
        resultDto.setResultMessage(resultMessage);
        return resultDto;
    }
}
