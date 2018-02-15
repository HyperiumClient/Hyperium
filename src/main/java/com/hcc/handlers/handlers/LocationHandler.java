package com.hcc.handlers.handlers;

import com.hcc.HCC;
import com.hcc.config.ConfigOpt;
import com.hcc.event.ChatEvent;
import com.hcc.event.InvokeEvent;
import com.hcc.event.SpawnpointChangeEvent;
import com.hcc.event.TickEvent;
import net.minecraft.util.EnumChatFormatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationHandler {

    @ConfigOpt
    private String location = "";
    private Pattern whereami = Pattern.compile("You are currently connected to server (?<server>.+)");
    private boolean sendingWhereAmI = false;
    private long ticksInWorld = 0;

    @InvokeEvent
    public void chatRecieve(ChatEvent event) {
        String raw = EnumChatFormatting.getTextWithoutFormattingCodes(event.getChat().getUnformattedText());
        Matcher whereAmIMatcher = whereami.matcher(raw);
        if (raw.equalsIgnoreCase("you are currently in limbo")) {
            this.location = "Limbo";
            if (sendingWhereAmI) {
                sendingWhereAmI = false;
                event.setCancelled(true);
            }

            return;
        }
        if (!whereAmIMatcher.matches()) {
            return;
        }

        this.location = whereAmIMatcher.group("server");
        if (sendingWhereAmI) {
            sendingWhereAmI = false;
            event.setCancelled(true);
        }

    }

    @InvokeEvent
    public void tick(TickEvent event) {
        if (ticksInWorld < 20) {
            ticksInWorld++;
            if (ticksInWorld == 20) {
                HCC.INSTANCE.getHandlers().getCommandQueue().queue("/whereami");
                sendingWhereAmI = true;
            }
        }

    }

    @InvokeEvent
    public void swapWorld(SpawnpointChangeEvent event) {
        ticksInWorld = 0;
    }

    public String getLocation() {
        return location;
    }
}
