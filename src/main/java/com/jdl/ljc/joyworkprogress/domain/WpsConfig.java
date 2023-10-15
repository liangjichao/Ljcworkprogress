package com.jdl.ljc.joyworkprogress.domain;

import com.intellij.openapi.project.Project;
import com.jdl.ljc.joyworkprogress.util.ProjectUtils;

public class WpsConfig {
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
