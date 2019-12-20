package cc.hyperium.commands.defaults;

import cc.hyperium.commands.BaseCommand;
import net.minecraft.client.Minecraft;

public class CommandChangeFov implements BaseCommand {
    /**
     * Gets the name of the command
     */
    @Override
    public String getName() {
        return "fov";
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
     * @param args the arguments the player has entered
     */
    @Override
    public void onExecute(String[] args) {
        if (args.length > 1) {
            sendPlayerMessage("Too many arguments. Usage: /fov <number>");
        } else if (args.length < 1) {
            sendPlayerMessage("Too little arguments. Usage: /fov <number>");
        } else {
            sendPlayerMessage("Fov changed from &e" + Minecraft.getMinecraft().gameSettings.fovSetting + " &rto &a" + Float.parseFloat(args[0]));
            Minecraft.getMinecraft().gameSettings.fovSetting = Float.parseFloat(args[0]);
        }
    }
}
