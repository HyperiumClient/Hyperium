package cc.hyperium.addons.sidebar.commands;

import cc.hyperium.addons.sidebar.SidebarAddon;
import cc.hyperium.addons.sidebar.gui.screen.GuiScreenSettings;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import net.minecraft.client.Minecraft;

public class CommandSidebar implements BaseCommand {
    private SidebarAddon addon;

    public CommandSidebar(SidebarAddon addon) {
        this.addon = addon;
    }

    @Override
    public String getName() {
        return "sidebaraddon";
    }

    @Override
    public String getUsage() {
        return "/sidebaraddon";
    }

    @Override
    public void onExecute(String[] strings) {
        EventBus.INSTANCE.register(this);
    }

    @InvokeEvent
    public void onTick(TickEvent e) {
        EventBus.INSTANCE.unregister(this);
        Minecraft.getMinecraft().displayGuiScreen(new GuiScreenSettings(this.addon));
    }
}
