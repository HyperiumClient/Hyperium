package cc.hyperium.commands.defaults;

import cc.hyperium.commands.BaseCommand;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.math.NumberUtils;

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
        } else if (args[0].equals("0")) {
            sendPlayerMessage("Changing your fov to 0 is disabled due to breaking the game.");
        } else {
            try {
                float f = Float.parseFloat(args[0]);
                if (f == 0) { // precision errors can make the parsed float 0 while the string isn't
                    sendPlayerMessage("Changing your fov to 0 is disabled due to breaking the game.");
                    return;
                }
                sendPlayerMessage("Fov changed from &e" + Minecraft.getMinecraft().gameSettings.fovSetting + " &rto &a" + f);
                Minecraft.getMinecraft().gameSettings.fovSetting = f;
            } catch (NumberFormatException e) {
                sendPlayerMessage("You cannot use a letter.");
            }
        }
    }
}
