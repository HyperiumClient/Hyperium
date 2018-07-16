package cc.hyperium.mods.playtime;

import cc.hyperium.Hyperium;
import cc.hyperium.event.*;
import cc.hyperium.internal.addons.IAddon;
import cc.hyperium.mods.AbstractMod;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class PlayTime extends AbstractMod  {

    @Override
    public AbstractMod init() {
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new PlayTimeCommand());
        startSysTime = System.currentTimeMillis();
        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return new Metadata(this, "Play Time", "1.0", "SHARDCoder");
    }

    static long startSysTime;

}
