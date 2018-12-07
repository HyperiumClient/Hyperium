package me.semx11.autotip.gson.creator;

import com.google.gson.InstanceCreator;
import java.lang.reflect.Type;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.stats.StatsDaily;

public class StatsDailyCreator implements InstanceCreator<StatsDaily> {

    private final Autotip autotip;

    public StatsDailyCreator(Autotip autotip) {
        this.autotip = autotip;
    }

    @Override
    public StatsDaily createInstance(Type type) {
        return new StatsDaily(autotip);
    }

}
