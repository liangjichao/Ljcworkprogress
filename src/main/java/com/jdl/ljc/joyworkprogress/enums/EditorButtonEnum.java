package com.jdl.ljc.joyworkprogress.enums;

/**
 * @author liangjichao
 * @date 2023/10/18 9:25 AM
 */
public enum EditorButtonEnum {
    FULL_SCREENT,
    LINK,
    EDITOR,
    EDITOR_AND_PREVIEW,
    PREVIEW;

    public static boolean spliterBtn(String uid) {
        if (EDITOR.name().equals(uid)||EDITOR_AND_PREVIEW.name().equals(uid)||PREVIEW.name().equals(uid)) {
            return true;
        }
        return false;
    }
}
