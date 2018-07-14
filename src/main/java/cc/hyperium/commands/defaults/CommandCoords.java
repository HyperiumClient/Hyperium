package cc.hyperium.commands.defaults;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

public class CommandCoords implements BaseCommand {
    @Override
    public String getName() {
        return "coords";
    }

    @Override
    public String getUsage() {
        return "/coords";
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
        Hyperium.INSTANCE.getHandlers().getCommandQueue().queue("X:" + ((int) thePlayer.posX) + " Y:" + ((int) thePlayer.posY) + " Z:" + ((int) thePlayer.posZ));
    }
}
