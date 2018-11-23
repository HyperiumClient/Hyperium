package cc.hyperium.addons.bossbar.commands;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.bossbar.BossbarAddon;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;

/**
 * Class which handles command input for "/autodab".
 */
public class CommandBossbar implements BaseCommand {

    /**
     * Addon instance
     */
    private BossbarAddon addon;

    public CommandBossbar(BossbarAddon addon) {
        this.addon = addon;
    }

    /**
     * Gets the name of the command (text after slash).
     *
     * @return The command name
     */
    @Override
    public String getName() {
        return "bossbaraddon";
    }

    /**
     * Gets the usage string for the command.
     *
     * @return The command usage
     */
    @Override
    public String getUsage() {
        return "/bossbaraddon";
    }

    /**
     * Callback when the command is invoked
     *
     * @throws CommandException for errors inside the command, these errors
     *                          will log directly to the players chat (without a prefix)
     */
    @Override
    public void onExecute(String[] args) throws CommandException {
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(addon.getGuiBossBarSetting());
    }


}
