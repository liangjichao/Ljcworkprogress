package com.jdl.ljc.joyworkprogress.config;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.jdl.ljc.joyworkprogress.domain.WpsState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author liangjichao
 * @date 2023/10/18 5:20 PM
 */
@State(name = "WpsPluginSetting",storages = {@Storage("WpsPluginSetting.xml")})
public class WpsPluginSetting implements PersistentStateComponent<WpsState> {
    private WpsState state = new WpsState();

    public static WpsPluginSetting getInstance() {
        return ApplicationManager.getApplication().getService(WpsPluginSetting.class);
    }

    @Override
    public @Nullable WpsState getState() {
        return state;
    }

    @Override
    public void loadState(@NotNull WpsState state) {
        this.state=state;
    }
}
