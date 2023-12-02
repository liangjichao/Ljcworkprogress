package com.ljc.workprogress.ui.editor.preview;

public class DefaultResourceProvider implements ResourceProvider{
    @Override
    public Boolean canProvide(String resourceName) {
        return false;
    }

    @Override
    public Resource loadResource(String resourceName) {
        return null;
    }
}
