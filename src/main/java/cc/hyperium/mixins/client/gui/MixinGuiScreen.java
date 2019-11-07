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

package cc.hyperium.mixins.client.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.mixinsimp.client.gui.HyperiumGuiScreen;
import com.chattriggers.ctjs.triggers.TriggerType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Mixin(GuiScreen.class)
public abstract class MixinGuiScreen {

    @Shadow protected Minecraft mc;
    @Shadow protected abstract void setText(String newChatText, boolean shouldOverwrite);
    @Shadow protected abstract void actionPerformed(GuiButton button) throws IOException;
    @Shadow protected abstract void keyTyped(char typedChar, int keyCode) throws IOException;
    @Shadow protected FontRenderer fontRendererObj;
    @Shadow public int width;
    @Shadow public int height;

    private HyperiumGuiScreen hyperiumGuiScreen = new HyperiumGuiScreen((GuiScreen) (Object) this);

    /**
     * @author SiroQ
     * @reason Fix input bug (MC-2781)
     **/
    @Overwrite
    public void handleKeyboardInput() throws IOException {
        char character = Keyboard.getEventCharacter();

        if (Keyboard.getEventKey() == 0 && character >= 32 || Keyboard.getEventKeyState()) {
            keyTyped(character, Keyboard.getEventKey());
        }

        mc.dispatchKeypresses();
    }

    @Inject(method = "drawScreen", at = @At("HEAD"), cancellable = true)
    private void drawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        hyperiumGuiScreen.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Inject(method = "drawWorldBackground", at = @At("HEAD"), cancellable = true)
    private void drawWorldBackground(int tint, CallbackInfo ci) {
        if (mc.theWorld != null && Settings.FAST_CONTAINER) ci.cancel();
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void mouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) {
        hyperiumGuiScreen.mouseClicked(mouseX, mouseY, mouseButton, ci);
    }

    @Inject(method = "initGui", at = @At("HEAD"))
    private void initGui(CallbackInfo ci) {
        hyperiumGuiScreen.initGui();
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    private void actionPerformed(GuiButton button, CallbackInfo info) {
        if (hyperiumGuiScreen.actionPerformed(button)) info.cancel();
    }

    @Inject(
        method = "sendChatMessage(Ljava/lang/String;Z)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onSendChatMessage(String msg, boolean addToChat, CallbackInfo ci) {
        TriggerType.MESSAGE_SENT.triggerAll(ci, msg);
    }

    @Inject(
        method = "handleComponentClick",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;sendChatMessage(Ljava/lang/String;Z)V")
    )
    private void runCommand(IChatComponent chatComponent, CallbackInfoReturnable<Boolean> cir) {
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().runningCommand = true;
    }

    private int scrollX, scrollY;
    private boolean allowScrolling;

    /**
     * @author asbyth
     * @reason Scrollable Tooltips
     */
    @Overwrite
    protected void drawHoveringText(List<String> textLines, int x, int y) {
        drawScrollableHoveringText(textLines, x, y, width, height, -1, fontRendererObj);
    }

    /**
     * Draws a tooltip box on the screen with text in it.
     * Automatically positions the box relative to the mouse to match Mojang's implementation.
     * Automatically wraps text when there is not enough space on the screen to display the text without wrapping.
     * Can have a maximum width set to avoid creating very wide tooltips.
     *
     * @param textLines    the lines of text to be drawn in a hovering tooltip box.
     * @param mouseX       the mouse X position
     * @param mouseY       the mouse Y position
     * @param screenWidth  the available screen width for the tooltip to drawn in
     * @param screenHeight the available  screen height for the tooltip to drawn in
     * @param maxTextWidth the maximum width of the text in the tooltip box.
     * @param font         the font for drawing the text in the tooltip box
     */
    private void drawScrollableHoveringText(List<String> textLines, int mouseX, int mouseY, int screenWidth, int screenHeight, int maxTextWidth, FontRenderer font) {
        if (!textLines.isEmpty()) {
            if (!allowScrolling) {
                scrollX = 0;
                scrollY = 0;
            }

            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int tooltipTextWidth = 0;

            for (String textLine : textLines) {
                int textLineWidth = font.getStringWidth(textLine);

                if (textLineWidth > tooltipTextWidth) {
                    tooltipTextWidth = textLineWidth;
                }
            }

            boolean needsWrap = false;

            int titleLinesCount = 1;
            int tooltipX = mouseX + 12;
            if (tooltipX + tooltipTextWidth + 4 > screenWidth) {
                tooltipX = mouseX - 16 - tooltipTextWidth;
                if (tooltipX < 4) { // if the tooltip doesn't fit on the screen
                    tooltipTextWidth = mouseX > screenWidth / 2 ? mouseX - 12 - 8 : screenWidth - 16 - mouseX;
                    needsWrap = true;
                }
            }

            if (maxTextWidth > 0 && tooltipTextWidth > maxTextWidth) {
                tooltipTextWidth = maxTextWidth;
                needsWrap = true;
            }

            if (needsWrap) {
                int wrappedTooltipWidth = 0;
                List<String> wrappedTextLines = new ArrayList<>();
                for (int i = 0; i < textLines.size(); i++) {
                    String textLine = textLines.get(i);
                    List<String> wrappedLine = font.listFormattedStringToWidth(textLine, tooltipTextWidth);

                    if (i == 0) {
                        titleLinesCount = wrappedLine.size();
                    }

                    for (String line : wrappedLine) {
                        int lineWidth = font.getStringWidth(line);

                        if (lineWidth > wrappedTooltipWidth) {
                            wrappedTooltipWidth = lineWidth;
                        }

                        wrappedTextLines.add(line);
                    }
                }

                tooltipTextWidth = wrappedTooltipWidth;
                textLines = wrappedTextLines;

                tooltipX = mouseX > screenWidth / 2 ? mouseX - 16 - tooltipTextWidth : mouseX + 12;
            }

            int tooltipY = mouseY - 12;
            int tooltipHeight = 8;

            if (textLines.size() > 1) {
                tooltipHeight += (textLines.size() - 1) * 10;
                if (textLines.size() > titleLinesCount) {
                    tooltipHeight += 2; // gap between title lines and next lines
                }
            }

            if (tooltipY + tooltipHeight + 6 > screenHeight) {
                tooltipY = screenHeight - tooltipHeight - 6;
            }

            allowScrolling = tooltipY < 0;
            GlStateManager.pushMatrix();
            if (allowScrolling) {
                int eventDWheel = Mouse.getDWheel();
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    if (eventDWheel < 0) {
                        scrollX += 10;
                    } else if (eventDWheel > 0) {
                        //Scrolling to access higher stuff
                        scrollX -= 10;
                    }
                } else {
                    if (eventDWheel < 0) {
                        scrollY -= 10;
                    } else if (eventDWheel > 0) {
                        //Scrolling to access higher stuff
                        scrollY += 10;
                    }
                }

                if (scrollY + tooltipY > 6) {
                    scrollY = -tooltipY + 6;
                } else if (scrollY + tooltipY + tooltipHeight + 6 < screenHeight) {
                    scrollY = screenHeight - 6 - tooltipY - tooltipHeight;
                }
            }

            GlStateManager.translate(scrollX, scrollY, 0);
            int zLevel = 300;
            int backgroundColor = 0xF0100010;
            GuiUtils.drawGradientRect(zLevel, tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY - 3, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(zLevel, tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 4, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(zLevel, tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            GuiUtils.drawGradientRect(zLevel, tooltipX + tooltipTextWidth + 3, tooltipY - 3, tooltipX + tooltipTextWidth + 4, tooltipY + tooltipHeight + 3, backgroundColor, backgroundColor);
            int borderColorStart = 0x505000FF;
            int borderColorEnd = (borderColorStart & 0xFEFEFE) >> 1 | borderColorStart & 0xFF000000;
            GuiUtils.drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            GuiUtils.drawGradientRect(zLevel, tooltipX + tooltipTextWidth + 2, tooltipY - 3 + 1, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3 - 1, borderColorStart, borderColorEnd);
            GuiUtils.drawGradientRect(zLevel, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY - 3 + 1, borderColorStart, borderColorStart);
            GuiUtils.drawGradientRect(zLevel, tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, borderColorEnd, borderColorEnd);

            for (int lineNumber = 0; lineNumber < textLines.size(); ++lineNumber) {
                String line = textLines.get(lineNumber);
                font.drawStringWithShadow(line, (float) tooltipX, (float) tooltipY, -1);

                if (lineNumber + 1 == titleLinesCount) {
                    tooltipY += 2;
                }

                tooltipY += 10;
            }

            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.popMatrix();
        }
    }
}
