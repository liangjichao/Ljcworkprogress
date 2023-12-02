package com.ljc.workprogress.ui.editor.preview;

public interface ResourceProvider {
    Boolean canProvide(String resourceName);

    Resource loadResource(String resourceName);
}
