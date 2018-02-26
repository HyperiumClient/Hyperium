package cc.hyperium.mods.timechanger;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.timechanger.commands.CommandTimeChangerDay;
import cc.hyperium.mods.timechanger.commands.CommandTimeChangerFastTime;
import cc.hyperium.mods.timechanger.commands.CommandTimeChangerNight;
import cc.hyperium.mods.timechanger.commands.CommandTimeChangerReset;
import cc.hyperium.mods.timechanger.commands.CommandTimeChangerSunset;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;

public class TimeChanger extends AbstractMod {
    
    private final Metadata metadata = new Metadata(this, "timechanger", "1.0", "fyu");
    private final Minecraft mc = Minecraft.getMinecraft();
    
    private double fastTimeMultiplier = 1.0D;
    private TimeType timeType = TimeType.VANILLA;
    
    public TimeChanger() {
        this.metadata.setDisplayName(ChatColor.BLUE + "TimeChanger");
    }
    
    @Override
    public AbstractMod init() {
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandTimeChangerDay(this));
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandTimeChangerNight(this));
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandTimeChangerSunset(this));
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandTimeChangerReset(this));
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(new CommandTimeChangerFastTime(this));
    
        EventBus.INSTANCE.register(this);
        
        return this;
    }
    
    @Override
    public Metadata getModMetadata() {
        return this.metadata;
    }
    
    public TimeType getTimeType() {
        return this.timeType;
    }
    
    public void setTimeType(TimeType timeType) {
        this.timeType = timeType;
    }
    
    public double getFastTimeMutliplier() {
        return this.fastTimeMultiplier;
    }
    
    public void setFastTimeMultiplier(double fastTimeMultiplier) {
        this.fastTimeMultiplier = fastTimeMultiplier;
    }
    
    @InvokeEvent
    public void onTick(TickEvent event) {
        if (this.mc.theWorld != null && this.timeType == TimeType.FAST) {
            this.mc.theWorld.setWorldTime((long) (System.currentTimeMillis() * this.fastTimeMultiplier % 24000.0));
        }
    }
    
    public enum TimeType {
        DAY, // During the day
        SUNSET, // Just before night
        NIGHT, // Always night
        VANILLA, // The normal time of the game
        FAST // Sped up vanilla time
    }
}
