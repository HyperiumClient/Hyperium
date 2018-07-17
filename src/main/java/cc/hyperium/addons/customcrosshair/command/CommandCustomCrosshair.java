package cc.hyperium.addons.customcrosshair.command;

import cc.hyperium.addons.customcrosshair.gui.GuiEditCrosshair;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import net.minecraft.client.Minecraft;

import java.util.Collections;
import java.util.List;

public class CommandCustomCrosshair implements BaseCommand {
    @Override
    public String getName() {
        return "customcrosshairaddon";
    }

    @Override
    public String getUsage() {
        return "/customcrosshairaddon";
    }

    @Override
    public void onExecute(String[] strings) throws CommandException {
        EventBus.INSTANCE.register(this);
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("cch");
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        return Collections.singletonList("customcrosshairaddon");
    }

    @InvokeEvent
    public void onTick(TickEvent event) {
        if (Minecraft.getMinecraft().currentScreen == null) {
            EventBus.INSTANCE.unregister(this);
            Minecraft.getMinecraft().displayGuiScreen(new GuiEditCrosshair());
        }
    }
}
