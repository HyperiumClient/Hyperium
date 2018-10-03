package cc.hyperium.addons.bossbar.commands;

import cc.hyperium.addons.bossbar.BossbarAddon;
import cc.hyperium.addons.bossbar.gui.GuiBossbarSetting;
import cc.hyperium.addons.sidebar.gui.screen.GuiScreenSettings;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import com.chattriggers.ctjs.commands.Command;
import net.minecraft.client.Minecraft;

public class CommandBossbar implements BaseCommand {
    private BossbarAddon addon;
    public CommandBossbar(BossbarAddon addon){
        this.addon = addon;
    }
    @Override
    public String getName() {
        return "bossbaraddon";
    }

    @Override
    public String getUsage() {
        return "/bossbaraddon";
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        EventBus.INSTANCE.register(this);
    }

    @InvokeEvent
    public void onTick(TickEvent e) {
        EventBus.INSTANCE.unregister(this);
        Minecraft.getMinecraft().displayGuiScreen(addon.getGuiBossBarSetting());
    }
}
