package cc.hyperium.addons.pingdisplay.commands;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.pingdisplay.gui.PingDisplayGui;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.event.EventBus;

public class PingDisplayCommand implements BaseCommand {
    @Override
    public String getName() {
        return "pingdisplay";
    }

    @Override
    public String getUsage() {
        return "/pingdisplay";
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        EventBus.INSTANCE.register(this);
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new PingDisplayGui());
        EventBus.INSTANCE.unregister(this);
    }
}
