package com.jdl.ljc.joyworkprogress.util;

public class MarkdownTextUtils {
    public static String createLink(String title,String url){
        return String.format("[%s](%s)",title,url);
    }
}
