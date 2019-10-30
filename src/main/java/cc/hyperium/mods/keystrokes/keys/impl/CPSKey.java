/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods.keystrokes.keys.impl;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.render.RenderEvent;
import cc.hyperium.mods.keystrokes.KeystrokesMod;
import cc.hyperium.mods.keystrokes.keys.AbstractKey;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CPSKey extends AbstractKey {

    private final List<Long> leftClicks = new ArrayList<>();
    private final List<Long> rightClicks = new ArrayList<>();
    private boolean leftWasDown;
    private boolean rightWasDown;

    public CPSKey(KeystrokesMod mod, int xOffset, int yOffset) {
        super(mod, xOffset, yOffset);

        EventBus.INSTANCE.register(this);
    }


    @Override
    public void renderKey(int x, int y) {
        int yOffset = this.yOffset;

        if (!mod.getSettings().isShowingMouseButtons()) yOffset -= 24;
        if (!mod.getSettings().isShowingSpacebar()) yOffset -= 18;
        if (!mod.getSettings().isShowingSneak()) yOffset -= 18;
        if (!mod.getSettings().isShowingWASD()) yOffset -= 48;

        Mouse.poll();

        int textColor = getColor();

        if (mod.getSettings().isKeyBackgroundEnabled()) {
            Gui.drawRect(x + xOffset, y + yOffset, x + xOffset + 70, y + yOffset + 16,
                new Color(mod.getSettings().getKeyBackgroundRed(), mod.getSettings().getKeyBackgroundGreen(), mod.getSettings().getKeyBackgroundBlue(),
                    mod.getSettings().getKeyBackgroundOpacity()).getRGB());
        }

        String name = (mod.getSettings().isLeftClick() ? getLeftCPS() : getRightCPS()) + " CPS";
        if (mod.getSettings().isChroma()) {
            drawChromaString(name, ((x + (xOffset + 70) / 2) - mc.fontRendererObj.getStringWidth(name) / 2), y + (yOffset + 4), 1.0F);
        } else {
            drawCenteredString(name, x + (xOffset + 70) / 2, y + (yOffset + 4), textColor);
        }
    }

    int getLeftCPS() {
        long time = System.currentTimeMillis();
        leftClicks.removeIf(o -> o + 1000L < time);
        return leftClicks.size();
    }

    int getRightCPS() {
        long time = System.currentTimeMillis();
        rightClicks.removeIf(o -> o + 1000L < time);
        return rightClicks.size();
    }

    @InvokeEvent
    public void onRender(RenderEvent event) {
        Mouse.poll();

        boolean downNow = Mouse.isButtonDown(mod.getRenderer().getMouseButtons()[0].getButton());
        if (downNow != leftWasDown && downNow) leftClicks.add(System.currentTimeMillis());

        leftWasDown = downNow;
        downNow = Mouse.isButtonDown(mod.getRenderer().getMouseButtons()[1].getButton());
        if (downNow != rightWasDown && downNow) rightClicks.add(System.currentTimeMillis());

        rightWasDown = downNow;
    }
}
