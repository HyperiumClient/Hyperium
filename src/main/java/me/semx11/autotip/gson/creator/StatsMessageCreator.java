package me.semx11.autotip.gson.creator;

import com.google.gson.InstanceCreator;
import java.lang.reflect.Type;
import me.semx11.autotip.message.StatsMessage;

public class StatsMessageCreator implements InstanceCreator<StatsMessage> {

    @Override
    public StatsMessage createInstance(Type type) {
        return new StatsMessage();
    }

}
