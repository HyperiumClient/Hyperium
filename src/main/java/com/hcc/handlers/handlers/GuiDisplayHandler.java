package com.hcc.handlers.handlers;

import com.hcc.event.InvokeEvent;
import com.hcc.event.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiDisplayHandler {

    private GuiScreen displayNextTick;

    public void setDisplayNextTick(GuiScreen displayNextTick) {
        this.displayNextTick = displayNextTick;
    }

    @InvokeEvent
    public void tick(TickEvent event) {
        if (displayNextTick != null) {
            Minecraft.getMinecraft().displayGuiScreen(displayNextTick);
            displayNextTick = null;
        }
    }
}

