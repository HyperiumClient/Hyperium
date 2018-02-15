package com.hcc.mods.sk1ercommon;

import com.hcc.event.InvokeEvent;
import com.hcc.event.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class ResolutionUtil {
    private static ScaledResolution resolution;

    public static ScaledResolution current() {
        return resolution;
    }

    @InvokeEvent
    public void tick(TickEvent event) {
        resolution = new ScaledResolution(Minecraft.getMinecraft());
    }
}
