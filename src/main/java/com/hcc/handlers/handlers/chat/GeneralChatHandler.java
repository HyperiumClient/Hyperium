package com.hcc.handlers.handlers.chat;

import com.hcc.event.ChatEvent;
import com.hcc.event.InvokeEvent;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

/**
 * Created by mitchellkatz on 2/14/18. Designed for production use on Sk1er.club
 */
public class GeneralChatHandler {

    private List<HCCChatHandler> handlerList;

    public GeneralChatHandler(List<HCCChatHandler> handlerList) {
        this.handlerList = handlerList;
    }

    @InvokeEvent
    public void chatEvent(ChatEvent event) {
        for (HCCChatHandler hccChatHandler : handlerList) {
            //Surround in try catch so errors don't stop further chat parsers
            try {
                hccChatHandler.chatReceived(event.getChat(), EnumChatFormatting.getTextWithoutFormattingCodes(event.getChat().getUnformattedText()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
