/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods.nickhider.command;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.mods.nickhider.NickHider;
import cc.hyperium.mods.nickhider.config.NickHiderConfig;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;

public class CommandNickHider implements BaseCommand {

    private final String syntax = "/nickhider <toggle, names, othernames, skins, otherskin, myskin, myskinonothers, prefix, suffix>";

    @Override
    public String getName() {
        return "nickhider";
    }

    @Override
    public String getUsage() {
        return "/" + getName();
    }

    @Override
    public void onExecute(String[] args) {
        NickHider instance = NickHider.instance;
        NickHiderConfig config = instance.getNickHiderConfig();

        if (args.length == 0) {
            if (instance.isExtendedUse())
                sendMessage("Extended use is active. You can access limited commands. /nickhider myname <Alternate name instead of your own> /nickhider set <user> <nick for that user>");
            sendAll();
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("clear")) {
                instance.reset();
            } else if (args[0].equalsIgnoreCase("toggle")) {
                config.setMasterEnabled(!config.isMasterEnabled());
                sendMessage("Toggled " + (config.isMasterEnabled() ? ChatColor.GREEN + "On" : ChatColor.RED + "Off"));
                instance.reset();
                sendAll();
            } else if (args[0].equalsIgnoreCase("names")) {
                config.setHideNames(!config.isHideNames());
                sendMessage("Set Hide Names " + (config.isHideNames() ? ChatColor.GREEN + "On" : ChatColor.RED + "Off"));
                instance.reset();
                sendAll();
            } else if (args[0].equalsIgnoreCase("othernames")) {
                config.setHideOtherNames(!config.isHideOtherNames());
                sendMessage("Set Hide Other Names " + (config.isHideOtherNames() ? ChatColor.GREEN + "On" : ChatColor.RED + "Off"));
                instance.reset();
                sendAll();
            } else if (args[0].equalsIgnoreCase("skins")) {
                config.setHideSkins(!config.isHideSkins());
                sendMessage("Hide Your Skin: " + (config.isHideSkins() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"));
                sendAll();
            } else if (args[0].equalsIgnoreCase("otherskins")) {
                config.setHideOtherSkins(!config.isHideOtherSkins());
                sendMessage("Hide Other Skins: " + (config.isHideOtherSkins() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"));
                sendAll();
            } else if (args[0].equalsIgnoreCase("myskin")) {
                config.setUseRealSkinForSelf(!config.isUseRealSkinForSelf());
                sendMessage("Set Use Your Real Skin On Yourself: " + (config.isUseRealSkinForSelf() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"));
                sendAll();
            } else if (args[0].equalsIgnoreCase("myskinonothers")) {
                config.setUsePlayerSkinForAll(!config.isUsePlayerSkinForAll());
                sendMessage("Set Use Your Real Skin On Other Players: " + (config.isUsePlayerSkinForAll() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"));
                sendAll();
            } else if (args[0].equalsIgnoreCase("pseudo")) {
                sendMessage("/nickhider pseudo <show,new,pseudo>");
                sendAll();
            } else if (args[0].equalsIgnoreCase("prefix")) {
                sendMessage("/nickhider prefix <prefix>");
            } else if (args[0].equalsIgnoreCase("suffix")) {
                sendMessage("/nickhider suffix <suffix>");
            } else {
                sendMessage(syntax);
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("pseudo")) {
                String tmp = args[1];
                if (tmp.equalsIgnoreCase("show")) {
                    sendMessage("Current pseudo: " + config.getPseudoKey());
                } else {
                    config.setPseudoKey(tmp);
                    instance.reset();
                    sendMessage("Set new pseudo");
                }
            } else if (args[0].equalsIgnoreCase("prefix")) {
                if (args[1].equalsIgnoreCase("NONE"))
                    args[1] = "";
                config.setPrefix(args[1]);
                instance.reset();
                sendMessage("Prefix changed to: " + args[1]);
            } else if (args[0].equalsIgnoreCase("suffix")) {
                if (args[1].equalsIgnoreCase("NONE"))
                    args[1] = "";
                config.setSuffix(args[1]);
                instance.reset();
                sendMessage("Suffix changed to: " + args[1]);
            } else if (args[0].equalsIgnoreCase("myname")) {
                if (!instance.isExtendedUse()) {
                    sendMessage("You cannot use this command.");
                    return;
                }

                String arg = args[1];
                instance.setOwnName(arg + "*");
            } else sendMessage(syntax);
        } else if (args.length == 3) {
            String arg = args[0];
            String arg1 = args[1];
            String arg2 = args[2] + "*";
            if (!instance.isExtendedUse()) {
                sendMessage("You cannot use this command.");
                return;
            }

            if (arg.equalsIgnoreCase("set")) {
                instance.getNicks().removeIf(nick -> nick.oldName.equalsIgnoreCase(arg1));
                instance.getUsedNicks().remove(arg.toLowerCase());
                instance.remap(arg1, arg2);
                sendMessage("Remapped!");
                instance.reset();
            } else {
                sendMessage(syntax);
            }
        } else {
            sendMessage(syntax);
        }
    }

    private void sendMessage(String message) {
        sendMessage(message, "");
    }

    public void sendMessage(String message, String hover) {
        sendMessage(message, hover, "");
    }

    public void sendMessage(String message, String hover, String click) {
        ChatComponentText chatComponent = new ChatComponentText(ChatColor.YELLOW + message);
        if (hover != null && !hover.isEmpty()) {
            chatComponent.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ChatComponentText(ChatColor.AQUA + hover + (!click.isEmpty() ? "\n" + click : ""))));
        }

        if (click != null && !click.isEmpty()) {
            chatComponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, click));
        }

        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(chatComponent);
    }

    private void sendAll() {
        NickHider instance = NickHider.instance;
        NickHiderConfig config = instance.getNickHiderConfig();
        sendMessage("Mod Status: " + (config.isMasterEnabled() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"), "Click to toggle the mod", "/nickhider toggle");
        sendMessage("Name Hiding: " + (config.isHideNames() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"), "Click to toggle name hiding", "/nickhider names");
        sendMessage("Hide Other Names " + (config.isHideOtherNames() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"), "Click to toggle hiding other players' names", "/nickhider othernames");
        sendMessage("Hide Your Skin: " + (config.isHideSkins() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"), "Click to toggle hiding skins", "/nickhider skins");
        sendMessage("Hide Other Players' Skins: " + (config.isHideOtherSkins() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"), "Click to toggle other players's names", "/nickhider otherskins");
        sendMessage("Use Your Real Skin On Yourself: " + (config.isUseRealSkinForSelf() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"), "Click to toggle using your own skin vs the default one", "/nickhider myskin");
        sendMessage("Use Your Real Skin On Other Players " + (config.isUsePlayerSkinForAll() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"), "Click to toggle using your own skin vs the default one", "/nickhider myskinonothers");
        sendMessage("Clear Cache", "Clears the mod's cache of names", "/nickhider clear");
        sendMessage("Change Psuedo: ", "Change the psuedo NickHider uses with /nickhider pseudo <pseudo>");
        sendMessage("Change Nick Prefix (Hover)", "Change the prefix NickHider uses with /nickhider prefix <prefix>. Use NONE to reset. \nA * will be added to the end of the player name.");
        sendMessage("Change Nick Suffix (Hover)", "Change the suffix NickHider uses with /nickhider suffix <suffix>. Use NONE to reset. \nA * will be added to the end of the player name.");
        sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + "Hover for details, click to execute command");
        sendMessage(syntax);
    }
}
