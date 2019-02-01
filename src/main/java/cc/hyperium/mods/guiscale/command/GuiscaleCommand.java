package cc.hyperium.mods.guiscale.command;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.config.Settings;
import cc.hyperium.utils.ChatColor;

public class GuiscaleCommand implements BaseCommand {
    @Override
    public String getName() {
        return "guiscale";
    }

    @Override
    public String getUsage() {
        return "/guiscale <integer>";
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        if (args.length > 0) {
            try {
                Settings.GUISCALEBONUS = Double.parseDouble(args[0]);
                Hyperium.CONFIG.save();
            } catch (Exception e) {
                Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(ChatColor.RED + "Failed to parse double.");
            }
        }

        Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage(ChatColor.GOLD + "Gui scale bonus: " + ChatColor.GRAY + Settings.GUISCALEBONUS);
    }
}
