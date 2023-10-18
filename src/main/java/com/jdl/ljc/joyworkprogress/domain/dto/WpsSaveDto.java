package com.jdl.ljc.joyworkprogress.domain.dto;

public class WpsSaveDto {
    private Long id;
    private String userCode;
    private String cardUrl;
    private Integer progressStatus;
    private String startTime;
    private String endTime;
    private String projectName;
    private String prd;
    private String productManager;
    private String devBranchName;
    private String appVersion;
    private String devInfo;

    private Integer forcedDependency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getCardUrl() {
        return cardUrl;
    }

    public void setCardUrl(String cardUrl) {
        this.cardUrl = cardUrl;
    }

    public Integer getProgressStatus() {
        return progressStatus;
    }

    public void setProgressStatus(Integer progressStatus) {
        this.progressStatus = progressStatus;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPrd() {
        return prd;
    }

    public void setPrd(String prd) {
        this.prd = prd;
    }

    public String getProductManager() {
        return productManager;
    }

    public void setProductManager(String productManager) {
        this.productManager = productManager;
    }

    public String getDevBranchName() {
        return devBranchName;
    }

    public void setDevBranchName(String devBranchName) {
        this.devBranchName = devBranchName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDevInfo() {
        return devInfo;
    }

    public void setDevInfo(String devInfo) {
        this.devInfo = devInfo;
    }

    public Integer getForcedDependency() {
        return forcedDependency;
    }

    public void setForcedDependency(Integer forcedDependency) {
        this.forcedDependency = forcedDependency;
    }
}
