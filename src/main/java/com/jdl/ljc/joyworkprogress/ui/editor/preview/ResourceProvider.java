package com.jdl.ljc.joyworkprogress.ui.editor.preview;

public interface ResourceProvider {
    Boolean canProvide(String resourceName);

    Resource loadResource(String resourceName);
}
