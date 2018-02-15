package com.hcc.handlers.handlers.chat;

import com.hcc.config.ConfigOpt;
import net.minecraft.util.IChatComponent;

public class AutoWhoChatHandler extends HCCChatHandler {
    @ConfigOpt
    private boolean enabled = true;

    @Override
    public boolean chatReceived(IChatComponent component, String text) {
        //Idk took this check from 2Pi's AutoWHO
        if (text.equalsIgnoreCase("Teaming is not allowed on Ranked Mode!")) {
            getHcc().getHandlers().getCommandQueue().queue("/who");
        }
        return false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
