package cc.hyperium.mods.keystrokes.keys.impl;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.LeftMouseClickEvent;
import cc.hyperium.event.RightMouseClickEvent;
import cc.hyperium.mods.keystrokes.KeystrokesMod;
import cc.hyperium.mods.keystrokes.keys.IKey;
import cc.hyperium.mods.keystrokes.utils.AntiReflection;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CPSKey extends IKey {

    @AntiReflection.HiddenField
    private boolean wasPressed = true;

    @AntiReflection.HiddenField
    private List<Long> clicks = new ArrayList<>();

    public CPSKey(int xOffset, int yOffset) {
        super(xOffset, yOffset);

        EventBus.INSTANCE.register(this);

        AntiReflection.filterClassMembers(getClass());
    }

    private int getCPS() {
        long time = System.currentTimeMillis();

        this.clicks.removeIf(o -> o + 1000L < time);

        return this.clicks.size();
    }

    @AntiReflection.HiddenMethod
    @Override
    public void renderKey(int x, int y) {
        int yOffset = this.yOffset;

        if (!KeystrokesMod.getSettings().isShowingMouseButtons()) {
            yOffset -= 24;
        }

        if (!KeystrokesMod.getSettings().isShowingSpacebar()) {
            yOffset -= 18;
        }

        Mouse.poll();

        int textColor = getColor();
        int pressedColor = getPressedColor();

        Gui.drawRect(x + this.xOffset, y + yOffset, x + this.xOffset + 70, y + yOffset + 16, new Color(0, 0, 0, 120).getRGB());

        String name = this.getCPS() + " CPS";
        if (super.settings.isChroma()) {
            drawChromaString(name, ((x + (this.xOffset + 70) / 2) - this.mc.fontRendererObj.getStringWidth(name) / 2), y + (yOffset + 4));
        } else {
            drawCenteredString(name, x + ((this.xOffset + 70) / 2), y + (yOffset + 4), textColor);
        }
    }

    @InvokeEvent
    public void onClick(LeftMouseClickEvent event) {
        if (super.settings.isLeftClick()) {
            Mouse.poll();
            this.clicks.add(System.currentTimeMillis());
        }


    }

    @InvokeEvent
    public void onClickRight(RightMouseClickEvent event) {
        if (super.settings.isLeftClick()) return;
        Mouse.poll();
        this.clicks.add(System.currentTimeMillis());
    }
}