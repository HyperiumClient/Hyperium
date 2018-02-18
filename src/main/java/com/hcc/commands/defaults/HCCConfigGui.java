package com.hcc.commands.defaults;

import com.hcc.gui.ModConfigGui;
import com.hcc.handlers.handlers.chat.GeneralChatHandler;
import com.hcc.commands.BaseCommand;

public class HCCConfigGui implements BaseCommand {
    @Override
    public String getName() {
        return "hccconfig";
    }

    @Override
    public String getUsage() {
        return "hccconfig";
    }

    @Override
    public void onExecute(String[] args) {
        GeneralChatHandler.instance().sendMessage("e");
        new ModConfigGui().show();
    }
}
