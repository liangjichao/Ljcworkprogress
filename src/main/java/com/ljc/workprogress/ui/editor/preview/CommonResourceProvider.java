package com.ljc.workprogress.ui.editor.preview;

import com.intellij.openapi.util.io.StreamUtil;

import java.io.IOException;
import java.io.InputStream;

public class CommonResourceProvider {

    public static Resource loadInternalResource(Class cls, String path, String contentType) {
        byte[] content = readFile(path, cls);
        return new Resource(content, contentType);
    }

    private static byte[] readFile(String path, Class cls) {
        path = "/html/" + path;
        InputStream inputStream = cls.getResourceAsStream(path);
        try {
            return StreamUtil.readBytes(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
