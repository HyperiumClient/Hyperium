package cc.hyperium.mixinsimp.gui;

import static cc.hyperium.gui.HyperiumGui.clamp;
import static cc.hyperium.gui.HyperiumGui.easeOut;

import cc.hyperium.mixins.gui.MixinGuiButton2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;

public class HyperiumGuiButton {
  private GuiButton parent;
  private long systemTime = Minecraft.getSystemTime();
  private float selectPercent = 0.0f;
  private final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
  public HyperiumGuiButton(GuiButton parent) {
    this.parent = parent;
  }
  public void drawButton(Minecraft mc, int mouseX, int mouseY) {
    MixinGuiButton2 parentA = (MixinGuiButton2) parent;
    if (!parent.visible) return;
    mc.getTextureManager().bindTexture(parentA.getButtonTextures());

    parentA.setHovered(mouseX >= parent.xPosition && mouseY >= parent.yPosition && mouseX < parent.xPosition + parent.getButtonWidth() && mouseY < parent.yPosition + parentA.getHeight());
    parentA.callMouseDragged(mc, mouseX, mouseY);

    if (this.systemTime < Minecraft.getSystemTime() + (1000 / 60)) {
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

    Gui.drawRect(
        (int) (parent.xPosition + (selectPercent * 7)),
        parent.yPosition,
        (int) ((parent.xPosition + parent.getButtonWidth()) - (selectPercent * 7)),
        parent.yPosition + parentA.getHeight(),
        parentA.isHovered() ? parentA.getHoverColor() : parentA.getColor()
    );

        /*// TODO RECT COLORS
        if (this.hovered) {
            drawRect(this.xPosition, this.yPosition,
                    this.xPosition + this.width, this.yPosition + this.height,
                    hoverColor);
        } else {
            drawRect(this.xPosition, this.yPosition,
                    this.xPosition + this.width, this.yPosition + this.height,
                    color);
        }*/

    int j = parentA.getTextColor();

    boolean enabled = true;
    if (!enabled) {
      j = 10526880;
    } else if (parentA.isHovered()) {
      j = parentA.getTextHoverColor();
    }
    parent.drawCenteredString(fontRenderer, parent.displayString, parent.xPosition + parent.getButtonWidth() / 2, parent.yPosition + (parentA.getHeight() - 8) / 2, j);
  }
}
