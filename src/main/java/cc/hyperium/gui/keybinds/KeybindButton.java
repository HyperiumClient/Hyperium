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

package cc.hyperium.gui.keybinds;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.KeypressEvent;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

public class KeybindButton extends GuiButton {

    private boolean listening;
    private HyperiumBind btnBind;

    KeybindButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, HyperiumBind bind) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.btnBind = bind;
        this.displayString = getName(bind.getKeyCode());
        EventBus.INSTANCE.register(this);
        detectConflicts();
    }

    public void setText(String text) {
        this.displayString = text;
    }

    void setListening(boolean listening) {
        this.listening = listening;

        if (!listening) {
            displayString = getName(btnBind.getKeyCode());
        } else {
            displayString = ChatColor.YELLOW + "LISTENING...";
        }
    }

    boolean isListening() {
        return listening;
    }

    private String getName(int keyCode) {
        if (keyCode < 0) {
            return Mouse.getButtonName(keyCode + 100);
        } else {
            return Keyboard.getKeyName(keyCode);
        }
    }

    @InvokeEvent
    public void keyEvent(KeypressEvent event) {
        if (listening) {
            if (event.getKey() == Keyboard.KEY_ESCAPE) {
                setBindKey(0);
            } else {
                setBindKey(event.getKey());
            }
        }
    }

    private void setBindKey(int key) {
        listening = false;
        displayString = getName(key);
        this.btnBind.setKeyCode(key);
        detectConflicts();
    }

    void resetBind() {
        int defaultKey = btnBind.getDefaultKeyCode();
        setBindKey(defaultKey);
    }

    /*
     * Minecraft method modified to accommodate scrolling offset.
     */
    void drawDynamicButton(Minecraft mc, int mouseX, int mouseY, int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + i * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;

            if (!this.enabled) {
                j = 10526880;
            } else if (this.hovered) {
                j = 16777120;
            }

            if (!btnBind.isConflicted()) {
                this.drawCenteredString(fontrenderer, this.displayString,
                    this.xPosition + this.width / 2,
                    this.yPosition + (this.height - 8) / 2, j);
            } else {
                this.drawCenteredString(fontrenderer, this.displayString,
                    this.xPosition + this.width / 2,
                    this.yPosition + (this.height - 8) / 2, Color.RED.getRGB());
            }
        }
    }

    boolean mousePressedDyanmic(int mouseX, int mouseY) {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    void detectConflicts() {
        for (HyperiumBind hyperiumBind : Hyperium.INSTANCE.getHandlers().getKeybindHandler().getKeybinds().values()) {
            hyperiumBind.detectConflicts();
        }
    }

    void mouseButtonClicked(int mouseButton) {
        if (mouseButton == 0 && !listening) {
            // Listen for further action.
            setListening(true);
        } else if (mouseButton >= 0 && listening) {
            // Mouse button has been pressed in listening phase, set bind.
            setBindKey(mouseButton - 100);
        }
    }
}
