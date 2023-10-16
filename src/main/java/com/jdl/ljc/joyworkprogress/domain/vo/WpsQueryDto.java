package com.jdl.ljc.joyworkprogress.domain.vo;

public class WpsQueryDto {
    private String userCode;
    private String projectName;
    private String devBranchName;
    private String appVersion;
    private Integer progressStatus;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    public Integer getProgressStatus() {
        return progressStatus;
    }

    public void setProgressStatus(Integer progressStatus) {
        this.progressStatus = progressStatus;
    }
}
