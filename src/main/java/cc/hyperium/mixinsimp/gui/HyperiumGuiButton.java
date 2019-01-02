package cc.hyperium.mixinsimp.gui;

import static cc.hyperium.gui.HyperiumGui.clamp;
import static cc.hyperium.gui.HyperiumGui.easeOut;
import static net.minecraft.client.gui.Gui.drawRect;

import cc.hyperium.config.Settings;
import cc.hyperium.mixins.gui.MixinGuiButton2;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

public class HyperiumGuiButton {
    private final int hoverColor = new Color(0, 0, 0, 120).getRGB();
    private final int hoverColorHyperium2 = new Color(85, 85, 255, 255).getRGB();
    private final int color = new Color(0, 0, 0, 70).getRGB();
    private final int textColor = new Color(255, 255, 255, 255).getRGB();
    private final int textHoverColor = new Color(255, 255, 255, 255).getRGB();
    private final int rgbColor = new Color(Settings.BUTTON_RED, Settings.BUTTON_GREEN, Settings.BUTTON_BLUE).getRGB();
    private final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
    private GuiButton parent;
    private long systemTime = Minecraft.getSystemTime();
    private float selectPercent = 0.0f;
    boolean enabled = true;

    public HyperiumGuiButton(GuiButton parent) {
        this.parent = parent;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        MixinGuiButton2 parentA = (MixinGuiButton2) parent;
        if (!parent.visible) return;
        mc.getTextureManager().bindTexture(parentA.getButtonTextures());

        parentA.setHovered(mouseX >= parent.xPosition && mouseY >= parent.yPosition && mouseX < parent.xPosition + parent.getButtonWidth() && mouseY < parent.yPosition + parentA.getHeight());
        parentA.callMouseDragged(mc, mouseX, mouseY);

        if (this.systemTime < Minecraft.getSystemTime() + (1000 / 60) && Settings.BUTTON_STYLE.equalsIgnoreCase("HYPERIUM")) {
            this.selectPercent = clamp(
                easeOut(
                    this.selectPercent,
                    parentA.isHovered() ? 1.0f : 0.0f,
                    0.01f,
                    parentA.isHovered() ? 5f : 2f
                ),
                0.0f,
                1.0f
            );

            this.systemTime += (1000 / 60);
        }

        if (Settings.BUTTON_STYLE.equalsIgnoreCase("HYPERIUM")) {
            drawRect(
                (int) (parent.xPosition + (selectPercent * 7)),
                parent.yPosition,
                (int) ((parent.xPosition + parent.getButtonWidth()) - (selectPercent * 7)),
                parent.yPosition + parentA.getHeight(),
                parentA.isHovered() ? hoverColor : color
            );
        }

        /* hyperium 2 button style created by nystrex, helped port to hyperium */

        if (Settings.BUTTON_STYLE.equalsIgnoreCase("HYPERIUM 2")) {
            drawRect(
                (int) (parent.xPosition + (selectPercent * 7)),
                parent.yPosition,
                (int) ((parent.xPosition + parent.getButtonWidth()) - (selectPercent * 7)),
                parent.yPosition + parentA.getHeight(),
                parentA.isHovered() ? hoverColor : color);
            if (Settings.BUTTON_TYPE.equalsIgnoreCase("DEFAULT")) {
                this.drawBorderedRect(parent.xPosition, parent.yPosition, parent.xPosition + parent.getButtonWidth(), parent.yPosition + parentA.getHeight(), 1, parentA.isHovered() ? hoverColorHyperium2 : textColor, color);
            } else if (Settings.BUTTON_TYPE.equalsIgnoreCase("RGB")) {
                this.drawBorderedRect(parent.xPosition, parent.yPosition, parent.xPosition + parent.getButtonWidth(), parent.yPosition + parentA.getHeight(), 1, parentA.isHovered() ? rgbColor : textColor, color);
            } else if (Settings.BUTTON_TYPE.equalsIgnoreCase("CHROMA")) {
                this.drawBorderedRect(parent.xPosition, parent.yPosition, parent.xPosition + parent.getButtonWidth(), parent.yPosition + parentA.getHeight(), 1, parentA.isHovered() ? getChromaColor() : textColor, color);
            }
        }
        int j = textColor;

        if (!enabled) {
            j = 10526880;
        } else if (parentA.isHovered() && Settings.BUTTON_STYLE.equalsIgnoreCase("HYPERIUM")) {
            j = textHoverColor;
        } else if (parentA.isHovered() && Settings.BUTTON_STYLE.equalsIgnoreCase("HYPERIUM 2") && Settings.BUTTON_TYPE.equalsIgnoreCase("DEFAULT")) {
            j = hoverColorHyperium2;
        } else if (parentA.isHovered() && Settings.BUTTON_STYLE.equalsIgnoreCase("HYPERIUM 2") && Settings.BUTTON_TYPE.equalsIgnoreCase("RGB")) {
            j = rgbColor;
        } else if (parentA.isHovered() && Settings.BUTTON_STYLE.equalsIgnoreCase("HYPERIUM 2") && Settings.BUTTON_TYPE.equalsIgnoreCase("CHROMA")) {
            j = getChromaColor();
        }
        parent.drawCenteredString(fontRenderer, parent.displayString, parent.xPosition + parent.getButtonWidth() / 2, parent.yPosition + (parentA.getHeight() - 8) / 2, j);
    }

    private void drawBorderedRect(int x, int y, int x1, int y1, int size, int borderC, int insideC) {
        drawRect(x + size, y + size, x1 - size, y1 - size, insideC);
        drawRect(x + size, y + size, x1, y, borderC);
        drawRect(x, y, x + size, y1 - 1, borderC);
        drawRect(x1, y1 - 1, x1 - size, y + size, borderC);
        drawRect(x, y1 - size, x1, y1, borderC);
    }

    public int getChromaColor() {
        return Color.HSBtoRGB(System.currentTimeMillis() % 5000L / 5000.0f, 1.0f, 1.0f);
    }
}
