package com.ljc.workprogress.domain;

import com.intellij.openapi.project.Project;
import com.ljc.workprogress.util.ProjectUtils;

public class WpsConfig {
    public static final String dateFormatPattern = "yyyy-MM-dd HH:mm:ss";
    private static WpsConfig config = new WpsConfig();
    private String currentUserCode;

    private WpsConfig() {

    }

    public void init(Project project) {
        currentUserCode = ProjectUtils.getCurrentUserCode(project);
    }

    public String getCurrentUserCode() {
        return currentUserCode;
    }

    public static WpsConfig getInstance() {
        return config;
    }
}
