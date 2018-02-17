package com.hcc.mods.togglechat.commands;

import com.hcc.handlers.handlers.command.BaseCommand;
import com.hcc.mods.sk1ercommon.ChatColor;
import com.hcc.mods.togglechat.ToggleChatMod;

import java.util.Collections;
import java.util.List;

/**
 * The BaseCommand implementation for ToggleChat's command with aliases
 *
 * @author boomboompower
 */
public class CommandToggleChat implements BaseCommand {

    /** The "mod" instance */
    private ToggleChatMod mod;

    /** Default constructor */
    public CommandToggleChat(ToggleChatMod impl) {
        this.mod = impl;
    }

    @Override
    public String getName() {
        return "chattoggle";
    }

    @Override
    public String getUsage() {
        return ChatColor.RED + "Usage: /chattoggle";
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("tc");
    }

    @Override
    public void onExecute(String[] args) {
        this.mod.openGui();
    }
}
