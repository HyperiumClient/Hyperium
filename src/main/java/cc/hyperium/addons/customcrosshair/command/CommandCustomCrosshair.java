package cc.hyperium.addons.customcrosshair.command;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.customcrosshair.CustomCrosshairAddon;
import cc.hyperium.addons.customcrosshair.gui.GuiCustomCrosshairEditCrosshair;
import cc.hyperium.commands.BaseCommand;

import java.util.Collections;
import java.util.List;

public class CommandCustomCrosshair implements BaseCommand {

    /**
     * The addon instance
     */
    private CustomCrosshairAddon addon;

    public CommandCustomCrosshair(CustomCrosshairAddon addon) {
        this.addon = addon;
    }

    /**
     * Gets the name of the command (text after slash).
     *
     * @return The command name
     */
    @Override
    public String getName() {
        return "customcrosshairaddon";
    }

    /**
     * Gets the usage string for the command.
     *
     * @return The command usage
     */
    @Override
    public String getUsage() {
        return "/customcrosshairaddon";
    }

    /**
     * Callback when the command is invoked
     */
    @Override
    public void onExecute(String[] strings) {
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GuiCustomCrosshairEditCrosshair(this.addon));
    }

    /**
     * A list of aliases to the main command. This will not be used if the list
     * is empty or {@code null}.
     *
     * @return The command aliases, which behave the same as the {@link #getName()}.
     */
    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("cch");
    }
}
