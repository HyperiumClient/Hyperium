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

package cc.hyperium.mixins.gui;

import cc.hyperium.mixinsimp.gui.HyperiumGuiButton;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("unused")
@Mixin(GuiButton.class)
public abstract class MixinGuiButton extends Gui {
    @Shadow
    @Final
    protected static ResourceLocation buttonTextures;
    private final int hoverColor = new Color(0, 0, 0, 120).getRGB();
    private final int color = new Color(0, 0, 0, 70).getRGB();
    private final int textColor = new Color(255, 255, 255, 255).getRGB();
    private final int textHoverColor = new Color(255, 255, 255, 255).getRGB();
    private final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
    @Shadow
    public boolean visible;
    @Shadow
    public int xPosition;
    @Shadow
    public int yPosition;
    @Shadow
    public String displayString;
    @Shadow
    protected boolean hovered;
    @Shadow
    protected int width;
    @Shadow
    protected int height;
    private float selectPercent = 0.0f;
    private long systemTime = Minecraft.getSystemTime();
    private HyperiumGuiButton hyperiumButton = new HyperiumGuiButton((GuiButton) (Object) this);

    @Shadow
    protected abstract void mouseDragged(Minecraft mc, int mouseX, int mouseY);

    @Overwrite
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        hyperiumButton.drawButton(mc, mouseX, mouseY);
    }
}
