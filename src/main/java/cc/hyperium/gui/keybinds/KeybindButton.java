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
import cc.hyperium.event.interact.KeyPressEvent;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class KeybindButton extends GuiButton {

    private boolean listening;
    private HyperiumBind btnBind;

    KeybindButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, HyperiumBind bind) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        btnBind = bind;
        displayString = getName(bind.getKeyCode());
        EventBus.INSTANCE.register(this);
        detectConflicts();
    }

    public void setText(String text) {
        displayString = text;
    }

    void setListening(boolean listening) {
        this.listening = listening;
        displayString = !listening ? getName(btnBind.getKeyCode()) : ChatColor.YELLOW + "LISTENING...";
    }

    boolean isListening() {
        return listening;
    }

    private String getName(int keyCode) {
        return keyCode < 0 ? Mouse.getButtonName(keyCode + 100) : Keyboard.getKeyName(keyCode);
    }

    @InvokeEvent
    public void keyEvent(KeyPressEvent event) {
        if (listening) setBindKey(event.getKey() == Keyboard.KEY_ESCAPE ? 0 : event.getKey());
    }

    private void setBindKey(int key) {
        listening = false;
        displayString = getName(key);
        btnBind.setKeyCode(key);
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
        xPosition = x;
        yPosition = y;
        if (visible) {
            FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
            int hoverState = getHoverState(hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            drawTexturedModalRect(xPosition, yPosition, 0, 46 + hoverState * 20, width / 2, height);
            drawTexturedModalRect(xPosition + width / 2, yPosition, 200 - width / 2, 46 + hoverState * 20, width / 2, height);
            mouseDragged(mc, mouseX, mouseY);
            int textColor = 14737632;

            if (!enabled) textColor = 10526880;
            else if (hovered) textColor = 16777120;

            if (!btnBind.isConflicted()) {
                drawCenteredString(fontrenderer, displayString,
                    xPosition + width / 2,
                    yPosition + (height - 8) / 2, textColor);
            } else {
                drawCenteredString(fontrenderer, ChatColor.RED + displayString,
                    xPosition + width / 2,
                    yPosition + (height - 8) / 2, Color.RED.getRGB());
            }
        }
    }

    boolean mousePressedDynamic(int mouseX, int mouseY) {
        return enabled && visible && mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
    }

    void detectConflicts() {
        Hyperium.INSTANCE.getHandlers().getKeybindHandler().getKeybinds().values().forEach(HyperiumBind::detectConflicts);
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
