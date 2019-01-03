package cc.hyperium.gui.keybinds;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.KeypressEvent;
import cc.hyperium.handlers.handlers.keybinds.HyperiumBind;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class KeybindButton extends GuiButton {

    private boolean listening = false;
    private HyperiumBind btnBind;

    public KeybindButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, HyperiumBind bind) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.btnBind = bind;
        this.displayString = getName(bind.getKeyCode());
        EventBus.INSTANCE.register(this);
        detectConflicts();
    }

    public void setText(String text) {
        this.displayString = text;
    }

    public void setListening(boolean listening) {
        this.listening = listening;

        if (listening == false) {
            setText(getName(btnBind.getKeyCode()));
        } else {
            setText("LISTENING...");
        }
    }

    public boolean isListening() {
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
        setText(getName(key));
        this.btnBind.setKeyCode(key);
        detectConflicts();
    }

    public void resetBind() {
        int defaultKey = btnBind.getDefaultKeyCode();
        setBindKey(defaultKey);
    }

    /*
     * Minecraft method modified to accommodate scrolling offset.
     */
    public void drawDynamicButton(Minecraft mc, int mouseX, int mouseY, int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
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

    public boolean mousePressedDyanmic(Minecraft mc, int mouseX, int mouseY) {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    public void detectConflicts() {
        for (HyperiumBind hyperiumBind : Hyperium.INSTANCE.getHandlers().getKeybindHandler().getKeybinds().values()) {
            hyperiumBind.detectConflicts();
        }
    }

    public void mouseButtonClicked(int mouseButton) {
        if (mouseButton == 0 && !listening) {
            // Listen for further action.
            setListening(true);
        } else if (mouseButton >= 0 && listening) {
            // Mouse button has been pressed in listening phase, set bind.
            setBindKey(mouseButton - 100);
        }
    }
}
