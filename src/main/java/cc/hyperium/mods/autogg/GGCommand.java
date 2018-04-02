package cc.hyperium.mods.autogg;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class GGCommand implements BaseCommand {
    @Override
    public String getName() {
        return "autogg";
    }

    @Override
    public String getUsage() {
        return "/autogg <toggle, delay [seconds]>";
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        if (args.length == 0 || args.length > 2) {
            this.showSyntaxError();
            return;
        }
        final String s = args[0];
        switch (s) {
            case "toggle":
            case "t": {
                AutoGG.getInstance().setToggled();
                this.showMessage(EnumChatFormatting.GRAY + "AutoGG: " + (AutoGG.getInstance().isToggled() ? (EnumChatFormatting.GREEN + "On") : (EnumChatFormatting.RED + "Off")));
                break;
            }
            case "delay":
            case "d":
            case "time": {
                if (args.length == 2) {
                    try {
                        final int delay = Integer.parseInt(args[1]);
                        if (delay < 0 || delay > 5) {
                            throw new NumberFormatException("Invalid integer");
                        }
                        AutoGG.getInstance().setDelay(delay);
                        ConfigUtil.setConfigDelay();
                        this.showMessage(EnumChatFormatting.GRAY + "AutoGG delay set to " + EnumChatFormatting.GREEN + AutoGG.getInstance().getDelay() + "s");
                    } catch (NumberFormatException e) {
                        this.showError("Please use an integer between 1 and 5 seconds.");
                    }
                    break;
                }
                this.showMessage(EnumChatFormatting.GRAY + "AutoGG Delay: " + EnumChatFormatting.GREEN + AutoGG.getInstance().getDelay() + "s");
                break;
            }
            default: {
                this.showSyntaxError();
                break;
            }
        }
    }

    private void showSyntaxError() {
        this.showMessage(EnumChatFormatting.RED + "Usage: " + this.getUsage());
    }
    private void showMessage(final String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(((IChatComponent)new ChatComponentText(message)));
    }
    private void showError(final String error) {
        this.showMessage(EnumChatFormatting.RED + "Error: " + error);
    }
}
