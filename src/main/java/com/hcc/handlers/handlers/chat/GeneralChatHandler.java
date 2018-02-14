package com.hcc.handlers.handlers.chat;

import com.hcc.event.ChatEvent;
import com.hcc.event.InvokeEvent;
import com.hcc.event.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by mitchellkatz on 2/14/18. Designed for production use on Sk1er.club
 */
public class GeneralChatHandler {
    private static GeneralChatHandler generalChatHandler = null;
    private List<HCCChatHandler> handlerList;

    private ConcurrentLinkedQueue<IChatComponent> messages = new ConcurrentLinkedQueue<>();

    public GeneralChatHandler(List<HCCChatHandler> handlerList) {
        this.handlerList = handlerList;
        generalChatHandler = this;
    }

    public static GeneralChatHandler instance() {
        return generalChatHandler;
    }

    public void sendMessage(IChatComponent component) {
        messages.add(component);
    }

    public void sendMessage(String message, boolean addHeader) {
        if (addHeader) {
            message = EnumChatFormatting.RED + "[HCC] " + EnumChatFormatting.WHITE.toString() + message;
        }
        sendMessage(new ChatComponentText(message));
    }

    public void sendMessage(String message) {
        sendMessage(message, true);
    }

    @InvokeEvent
    public void tick(TickEvent event) {
        if (Minecraft.getMinecraft().thePlayer == null)
            return;
        while (!messages.isEmpty()) {
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(messages.poll());
        }
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
