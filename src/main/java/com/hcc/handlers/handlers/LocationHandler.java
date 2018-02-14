package com.hcc.handlers.handlers;

import com.hcc.config.ConfigOpt;
import com.hcc.event.ChatEvent;
import com.hcc.event.InvokeEvent;
import net.minecraft.util.EnumChatFormatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationHandler {

    @ConfigOpt
    private String location = "";
    private Pattern whereami = Pattern.compile("You are currently connected to server (?<server>.+)");


    @InvokeEvent
    public void chatRecieve(ChatEvent event) {
        String raw = EnumChatFormatting.getTextWithoutFormattingCodes(event.getChat().getUnformattedText());
        Matcher whereAmIMatcher = whereami.matcher(raw);
        if (whereAmIMatcher.matches()) {
            this.location = whereAmIMatcher.group("server");
            return;
        }
        if (raw.equalsIgnoreCase("you are currently in limbo")) {
            this.location = "Limbo";
        }

    }


    public String getLocation() {
        return location;
    }
}
