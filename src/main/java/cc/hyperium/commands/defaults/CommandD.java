package cc.hyperium.commands.defaults;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.handlers.handlers.HypixelDetector;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.handlers.handlers.hud.TabCompletionUtil;
import net.minecraft.client.Minecraft;
import java.util.List;

public class CommandD implements BaseCommand {
    @Override
    public String getName() {
        return "d";
    }

    @Override
    public String getUsage() {
        return "/d";    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        if (HypixelDetector.getInstance().isHypixel()) {
            GeneralChatHandler.instance().sendMessage("This command cannot be used on Hypixel!");
        } else {
            return;
        }
    }

    @Override
    public List<String> onTabComplete(String[] args) {
        return TabCompletionUtil.getListOfStringsMatchingLastWord(args, TabCompletionUtil.getTabUsernames());
    }
}
