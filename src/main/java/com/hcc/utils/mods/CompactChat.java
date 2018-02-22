package com.hcc.utils.mods;

import com.hcc.HCC;
import com.hcc.event.ChatEvent;
import com.hcc.event.EventBus;
import com.hcc.event.InvokeEvent;
import com.hcc.event.Priority;
import com.hcc.gui.settings.items.GeneralSetting;
import com.hcc.utils.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;

public class CompactChat {

    private static CompactChat instance;

    private String lastMessage = "";

    private int line = 0;
    private int amount = 0;

    @InvokeEvent(priority = Priority.LOW)
    public void onChat(ChatEvent event) {
        GuiNewChat guiNewChat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        if(this.lastMessage.equals(event.getChat().getUnformattedText()) && GeneralSetting.compactChatEnabled) {
            guiNewChat.deleteChatLine(line);
            this.amount++;
            this.lastMessage = event.getChat().getUnformattedText();
            event.getChat().appendText(ChatColor.GRAY + " (" + amount + ")");
        } else {
            this.amount = 1;
            this.lastMessage = event.getChat().getUnformattedText();
        }
        this.line++;
        guiNewChat.printChatMessageWithOptionalDeletion(event.getChat(), line);
        if(line > 256) line = 0; // yeah...
        event.setCancelled(true);
    }

    public static CompactChat getInstance() {
        if(instance == null)
            instance = new CompactChat();
        return instance;
    }

}
