package com.jdl.ljc.joyworkprogress.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class FileUtils {
    public static String getResource(String name) {
        InputStream inputStream=null;
        ByteArrayOutputStream outputStream=null;
        String content=null;
        try {
            inputStream=FileUtils.class.getResourceAsStream(name);
            outputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            byte[] contentBytes = outputStream.toByteArray();
            content = new String(contentBytes, "UTF-8");
        } catch (IOException e) {
        }finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
            }
        }
        return content;
    }
}
