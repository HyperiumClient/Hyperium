package com.hcc.handlers;

import com.hcc.HCC;
import com.hcc.config.ConfigOpt;
import com.hcc.event.ChatEvent;
import com.hcc.event.InvokeEvent;
import net.minecraft.util.EnumChatFormatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationHandler {

    private HCC hcc = HCC.INSTANCE;
    @ConfigOpt
    private String location = "";

    private Pattern whereami = Pattern.compile("You are currently connected to server (?<server>.+)");

    public LocationHandler() {

    }

    /*
    Example ingame locations: https://sk1er.exposed/20.25.35-12.02.18.png

     */
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
