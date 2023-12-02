package com.ljc.workprogress.domain.vo;

public class WorkProgressGridData {
    private String title;
    private String progressStatus;

    private String planWorkHours;

    private String productManager;

    private String userCode;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProgressStatus() {
        return progressStatus;
    }

    public void setProgressStatus(String progressStatus) {
        this.progressStatus = progressStatus;
    }

    public String getPlanWorkHours() {
        return planWorkHours;
    }

    public void setPlanWorkHours(String planWorkHours) {
        this.planWorkHours = planWorkHours;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getProductManager() {
        return productManager;
    }

    public void setProductManager(String productManager) {
        this.productManager = productManager;
    }
}
