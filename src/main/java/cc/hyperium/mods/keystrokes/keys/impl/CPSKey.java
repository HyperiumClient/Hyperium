/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods.keystrokes.keys.impl;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderEvent;
import cc.hyperium.mods.keystrokes.KeystrokesMod;
import cc.hyperium.mods.keystrokes.keys.IKey;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class CPSKey extends IKey {

    private final List<Long> leftClicks = new ArrayList<>();
    private final List<Long> rightClicks = new ArrayList<>();
    private boolean leftWasDown = false;
    private boolean rightWasDown = false;

    public CPSKey(KeystrokesMod mod, int xOffset, int yOffset) {
        super(mod, xOffset, yOffset);

        EventBus.INSTANCE.register(this);
    }


    @Override
    public void renderKey(int x, int y) {
        int yOffset = this.yOffset;

        if (!this.mod.getSettings().isShowingMouseButtons()) {
            yOffset -= 24;
        }

        if (!this.mod.getSettings().isShowingSpacebar()) {
            yOffset -= 18;
        }

        if (!this.mod.getSettings().isShowingSneak()) {
            yOffset -= 18;
        }

        Mouse.poll();

        int textColor = getColor();

        Gui.drawRect(x + this.xOffset, y + yOffset, x + this.xOffset + 70, y + yOffset + 16, new Color(0, 0, 0, 120).getRGB());

        String name = (this.mod.getSettings().isLeftClick() ? this.getLeftCPS() : this.getRightCPS()) + " CPS";
        if (this.mod.getSettings().isChroma()) {
            this.drawChromaString(name, x + (this.xOffset + 70) / 2 - this.mc.fontRendererObj.getStringWidth(name) / 2, y + (yOffset + 4));
        } else {
            this.drawCenteredString(name, x + (this.xOffset + 70) / 2, y + (yOffset + 4), textColor);
        }
    }

    int getLeftCPS() {
        long time = System.currentTimeMillis();

        this.leftClicks.removeIf(o -> o + 1000L < time);

        return this.leftClicks.size();
    }

    int getRightCPS() {
        long time = System.currentTimeMillis();

        this.rightClicks.removeIf(o -> o + 1000L < time);

        return this.rightClicks.size();
    }

    @InvokeEvent
    public void onRender(RenderEvent event) {
        Mouse.poll();

        boolean downNow = Mouse.isButtonDown(mod.getRenderer().getMouseButtons()[0].getButton());
        if (downNow != leftWasDown && downNow) {
            leftClicks.add(System.currentTimeMillis());
        }

        leftWasDown = downNow;
        downNow = Mouse.isButtonDown(mod.getRenderer().getMouseButtons()[1].getButton());
        if (downNow != rightWasDown && downNow) {
            rightClicks.add(System.currentTimeMillis());
        }

        rightWasDown = downNow;
    }
}
