package com.jdl.ljc.joyworkprogress.util;

public class ObjectUtils {
    public static boolean hasClazz(String className) {
        try {
            Class.forName(className);
        } catch (Exception e) {
            return false;
        } catch (Error error) {
            return false;
        }
        return true;
    }
}
