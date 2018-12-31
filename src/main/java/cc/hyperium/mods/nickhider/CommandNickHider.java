package cc.hyperium.mods.nickhider;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import net.minecraft.util.EnumChatFormatting;

public class CommandNickHider implements BaseCommand {


    @Override
    public String getName() {
        return "nickhider";
    }

    @Override
    public String getUsage() {
        return null;
    }

    public void sendMessage(String in) {
        GeneralChatHandler.instance().sendMessage(in);
    }

    @Override
    public void onExecute(String[] args) throws cc.hyperium.commands.CommandException {
        if (args.length == 0) {
            if (NickHider.INSTANCE.isExtendedUse())
                sendMessage("Extended use is active. You can access limited commands. /nickhider myname <Alternate name instead of your own one>  /nickhider set <user> <nick for that user>");
            sendMessage("Nick hider status: " + (NickHider.INSTANCE.isEnabled() ? EnumChatFormatting.GREEN + "Enabled" : EnumChatFormatting.RED + "Disabled"));
            sendMessage("Hide others " + (NickHider.INSTANCE.isSelfOnly() ? EnumChatFormatting.RED + "No" : EnumChatFormatting.GREEN + "Yes"));
            sendMessage("Skins: " + (NickHider.INSTANCE.isHideSkins() ? EnumChatFormatting.RED + "Off" : EnumChatFormatting.GREEN + "On"));
            sendMessage("/nickhider <toggle,skin,self,pseudo>");
        } else if (args.length == 1) {

            if (args[0].equalsIgnoreCase("clear")) {
                NickHider.INSTANCE.reset();
            } else if (args[0].equalsIgnoreCase("toggle")) {
                NickHider.INSTANCE.toggle();
                sendMessage("Toggled " + (NickHider.INSTANCE.isEnabled() ? EnumChatFormatting.GREEN + "On" : EnumChatFormatting.RED + "Off"));
            } else if (args[0].equalsIgnoreCase("self")) {
                NickHider.INSTANCE.toggleSelf();
                sendMessage("Hide others " + (NickHider.INSTANCE.isSelfOnly() ? EnumChatFormatting.RED + "No" : EnumChatFormatting.GREEN + "Yes"));
            } else if (args[0].equalsIgnoreCase("pseudo")) {
                sendMessage("/nickhider pseudo <show,new,psudo>");
            } else if (args[0].equalsIgnoreCase("skin")) {
                NickHider.INSTANCE.setHideSkins(!NickHider.INSTANCE.isHideSkins());
                sendMessage("Toggled skins: " + (NickHider.INSTANCE.isHideSkins() ? EnumChatFormatting.RED + "Off" : EnumChatFormatting.GREEN + "On"));
            } else
                sendMessage("/nickhider <toggle,skin,self,pseudo>");

        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("pseudo")) {
                String tmp = args[1];
                if (tmp.equalsIgnoreCase("show")) {
                    sendMessage("Current pseudo: " + NickHider.INSTANCE.getPseudo_key());
                } else {
                    NickHider.INSTANCE.setPseudo_key(tmp);
                    NickHider.INSTANCE.reset();
                    sendMessage("Set new pseudo");
                }
            } else if (args[0].equalsIgnoreCase("myname")) {
                if (!NickHider.INSTANCE.isExtendedUse()) {
                    sendMessage("You cannot use this command.");
                    return;
                }
                String arg = args[1];
                NickHider.INSTANCE.setOwnName(arg + "*");
            } else
                sendMessage("/nickhider <toggle,skin,self,pseudo>");

        } else if (args.length == 3) {
            String arg = args[0];
            String arg1 = args[1];
            String arg2 = args[2] + "*";
            if (!NickHider.INSTANCE.isExtendedUse()) {
                sendMessage("You cannot use this command.");
                return;
            }
            if (arg.equalsIgnoreCase("set")) {
                NickHider.INSTANCE.getNicks().removeIf(nick -> nick.oldName.equalsIgnoreCase(arg1));
                NickHider.INSTANCE.getUsedNicks().remove(arg.toLowerCase());
                NickHider.INSTANCE.remap(arg1, arg2);
                sendMessage("Remapped!");
            } else {
                sendMessage("/nickhider <toggle,skin,self,pseudo>");
            }

        } else
            sendMessage("/nickhider <toggle,skin,self,pseudo>");
        NickHider.INSTANCE.getCache().clear();
    }
}
