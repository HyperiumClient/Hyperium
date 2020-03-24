/*
 * Forge Mod Loader
 * Copyright (c) 2012-2014 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Contributors (this class):
 *     bspkrs - implementation
 */

package net.minecraftforge.fml.client.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

/**
 * This class provides a button that fixes several bugs present in the vanilla GuiButton drawing
 * code. The gist of it is that it allows buttons of any size without gaps in the graphics and with
 * the borders drawn properly. It also prevents button text from extending out of the sides of the
 * button by trimming the end of the string and adding an ellipsis.<br/><br/>
 * <p>
 * The code that handles drawing the button is in GuiUtils.
 *
 * @author bspkrs
 */
public class GuiButtonExt extends GuiButton {

  public GuiButtonExt(int id, int xPos, int yPos, String displayString) {
    super(id, xPos, yPos, displayString);
  }

  public GuiButtonExt(int id, int xPos, int yPos, int width, int height, String displayString) {
    super(id, xPos, yPos, width, height, displayString);
  }

  private double hoverFade;
  private long prevDeltaTime;

  /**
   * Draws this button to the Screen.
   */
  @Override
  public void drawButton(Minecraft mc, int mouseX, int mouseY) {
    if (prevDeltaTime == 0) {
      prevDeltaTime = System.currentTimeMillis();
    }

    if (visible) {
      mc.getTextureManager().bindTexture(buttonTextures);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width
          && mouseY < yPosition + height;
      double hoverInc = (System.currentTimeMillis() - prevDeltaTime) / 2F;
      hoverFade = hovered ? Math.min(100, hoverFade + hoverInc) : Math.max(0, hoverInc - hoverInc);

      drawRect(xPosition, yPosition, xPosition + width, yPosition + height,
          new Color(0, 0, 0, (int) (100 - (hoverFade / 2))).getRGB());

      if (hovered) {
        drawRect(xPosition, yPosition + 19, xPosition + width, yPosition + height,
            new Color(3, 169, 244).getRGB());
      }

      mouseDragged(mc, mouseX, mouseY);

      int textColor = enabled ? 255 : 180;
      drawCenteredString(mc.fontRendererObj, displayString, xPosition + width / 2,
          yPosition + (height - 8) / 2,
          new Color(textColor, textColor, textColor, 255).getRGB());
      prevDeltaTime = System.currentTimeMillis();
    }
  }
}
