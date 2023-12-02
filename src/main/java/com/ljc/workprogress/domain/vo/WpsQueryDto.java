package com.ljc.workprogress.domain.vo;

public class WpsQueryDto {

    private String userCode;
    private String projectName;
    private String devBranchName;
    private String appVersion;
    private Integer progressStatus;

    private Long cpage;
    private Integer pageSize;

    public WpsQueryDto() {

    }

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

    public Long getCpage() {
        return cpage;
    }

    public void setCpage(Long cpage) {
        this.cpage = cpage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
