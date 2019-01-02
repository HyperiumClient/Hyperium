package me.semx11.autotip.gson.creator;

import com.google.gson.InstanceCreator;
import java.lang.reflect.Type;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.config.Config;

public class ConfigCreator implements InstanceCreator<Config> {

    private final Autotip autotip;

    public ConfigCreator(Autotip autotip) {
        this.autotip = autotip;
    }

    @Override
    public Config createInstance(Type type) {
        return new Config(autotip);
    }

}
