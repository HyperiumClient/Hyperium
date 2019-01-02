package cc.hyperium.commands.defaults;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import net.minecraft.client.Minecraft;

/**
 * Created by mitchellkatz on 6/25/18. Designed for production use on Sk1er.club
 */
public class DevTestCommand implements BaseCommand {
    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        Hyperium.INSTANCE.getHandlers().getYeetHandler().yeet(Minecraft.getMinecraft().thePlayer.getUniqueID());
    }
}
