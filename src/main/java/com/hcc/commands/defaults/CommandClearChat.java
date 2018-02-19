package com.hcc.commands.defaults;

import com.hcc.commands.BaseCommand;
import com.hcc.commands.CommandException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;

/**
 * A simple command to clear your chat history & sent commands,
 * simply calls the {@link GuiNewChat#clearChatMessages()} method
 *
 * @author boomboompower
 */
public class CommandClearChat implements BaseCommand {
    
    @Override
    public String getName() {
        return "clearchat";
    }
    
    @Override
    public String getUsage() {
        return null;
    }
    
    @Override
    public void onExecute(String[] args) throws CommandException {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages();
    }
}
