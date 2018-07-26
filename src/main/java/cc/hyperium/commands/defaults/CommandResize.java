package cc.hyperium.commands.defaults;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.mixins.IMixinMinecraft;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class CommandResize implements BaseCommand {
    @Override
    public String getName() {
        return "resize";
    }

    @Override
    public String getUsage() {
        return "/resize <width,height>";
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        if (args.length != 2 || !StringUtils.isNumeric(args[0]) || !StringUtils.isNumeric(args[1])) {
            GeneralChatHandler.instance().sendMessage(getUsage());
        }
        int displayWidth = Integer.parseInt(args[0]);
        Minecraft.getMinecraft().displayWidth = displayWidth;
        int displayHeight = Integer.parseInt(args[1]);
        Minecraft.getMinecraft().displayHeight = displayHeight;
        try {
            Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        Display.setResizable(false);
        Display.setResizable(true);
        ((IMixinMinecraft) Minecraft.getMinecraft()).callResize(displayWidth, displayHeight);

    }
}
