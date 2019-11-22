package cc.hyperium.commands.defaults;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.handlers.handlers.cloud.CloudRenderer;
import net.minecraft.client.Minecraft;

public class CloudWireframeCommand implements BaseCommand {
    /**
     * Gets the name of the command
     */
    @Override
    public String getName() {
        return "cloud_wireframe";
    }

    /**
     * Gets the usage string for the command.
     */
    @Override
    public String getUsage() {
        return "/" + getName();
    }

    /**
     * Callback when the command is invoked
     *
     * @param args
     * @throws CommandException for errors inside the command, these errors
     *                          will log directly to the players chat (without a prefix)
     */
    @Override
    public void onExecute(String[] args) throws CommandException {
        CloudRenderer.WIREFRAME = !CloudRenderer.WIREFRAME;
        Hyperium.INSTANCE.getHandlers().getGeneralChatHandler().sendMessage("Cloud Wireframe was set to " + CloudRenderer.WIREFRAME);
    }
}
