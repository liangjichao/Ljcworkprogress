package com.ljc.workprogress.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vfs.VirtualFile;
import git4idea.GitUtil;
import git4idea.config.GitConfigUtil;
import git4idea.repo.GitRepository;

public class ProjectUtils {
    public static String getCurrentBranchName(Project project) {
        VirtualFile baseDir = project.getBaseDir();
        GitRepository res = GitUtil.getRepositoryManager(project).getRepositoryForRootQuick(baseDir);
        if (res != null) {
            return res.getCurrentBranchName();
        }
        return "";
    }
    public static String getCurrentUserCode(Project project) {
        VirtualFile root = project.getBaseDir();
        String userName = null;
        try {
            userName = GitConfigUtil.getValue(project, root, GitConfigUtil.USER_NAME);
        } catch (VcsException e) {
            throw new RuntimeException(e);
        }

        return userName;
    }
}
