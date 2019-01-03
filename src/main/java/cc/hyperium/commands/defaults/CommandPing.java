package cc.hyperium.commands.defaults;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.handlers.handlers.hud.NetworkInfo;
import cc.hyperium.handlers.handlers.hud.TabCompletionUtil;
import net.minecraft.client.Minecraft;

import java.util.List;

public class CommandPing implements BaseCommand {
    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getUsage() {
        return "/ping or /ping <name>";
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        final String name = (args.length == 1) ? args[0] : Minecraft.getMinecraft().getSession().getUsername();
        NetworkInfo.getInstance().printPing(name);
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        return TabCompletionUtil.getListOfStringsMatchingLastWord(args, TabCompletionUtil.getTabUsernames());
    }
}
