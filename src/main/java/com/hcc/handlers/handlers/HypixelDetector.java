package com.hcc.handlers.handlers;

import com.hcc.event.InvokeEvent;
import com.hcc.event.TickEvent;
import me.semx11.autotip.util.ReflectionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.lang.reflect.Field;

public class HypixelDetector {
    private static final String HYPIXEL_HEADER = "You are playing on MC.HYPIXEL.NET";
    private static final Field HEADER_FIELD = ReflectionUtil.findField(GuiPlayerTabOverlay.class, "field_175256_i", "header");
    private boolean hypixel = false;


    public HypixelDetector() {

    }

    public static Object getHeader() {
        try {
            return HEADER_FIELD.get(Minecraft.getMinecraft().ingameGUI.getTabList());
        } catch (IllegalAccessException | NullPointerException e) {
            return null;
        }
    }

    @InvokeEvent
    public void tick(TickEvent event) {

        //Check for Hypixel every tick to make sure bad things don't happen
        Object header = getHeader();
        if (header != null) {
            String text = EnumChatFormatting.getTextWithoutFormattingCodes(((IChatComponent) header).getUnformattedText());
            hypixel = text.equalsIgnoreCase(HYPIXEL_HEADER);
        } else hypixel = false;
    }

    public boolean isHypixel() {
        return hypixel;
    }
}
