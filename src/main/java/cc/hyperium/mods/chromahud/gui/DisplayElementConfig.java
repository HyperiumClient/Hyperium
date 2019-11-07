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

package cc.hyperium.mods.chromahud.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.mods.chromahud.ChromaHUD;
import cc.hyperium.mods.chromahud.ChromaHUDApi;
import cc.hyperium.mods.chromahud.DisplayElement;
import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.config.GuiSlider;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Sk1er
 */
public class DisplayElementConfig extends GuiScreen {

    private final Map<GuiButton, Consumer<GuiButton>> clicks = new HashMap<>();
    private final Map<GuiButton, Consumer<GuiButton>> updates = new HashMap<>();
    private final Map<String, GuiButton> nameMap = new HashMap<>();
    private DisplayElement element;
    private int ids;
    private int lastX, lastY;
    private DynamicTexture texture;
    private DynamicTexture texture2;
    private int hue = -1;
    private int saturation = -1;
    private int brightness = 5;
    private ChromaHUD mod;
    private int lastWidth;
    private int lastHeight;
    private boolean mouseLock;

    DisplayElementConfig(DisplayElement element, ChromaHUD mod) {
        assert element != null : "Display element is null!";
        this.mod = mod;
        this.element = element;
        regenImage();
        mouseLock = Mouse.isButtonDown(0);
    }

    private void regenImage() {
        int dim = 256;
        BufferedImage image = new BufferedImage(dim, dim, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < dim; x++) {
            for (int y = 0; y < dim; y++) {
                image.setRGB(x, y, Color.HSBtoRGB(x / 256F, 1.0F - y / 256F, 1F));
            }
        }

        texture = new DynamicTexture(image);
        if (hue != -1 && saturation != -1) {
            BufferedImage image1 = new BufferedImage(1, dim, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < dim; y++) {
                float hue = this.hue / 256F;
                float saturation = this.saturation / 256F;
                image1.setRGB(0, y, Color.HSBtoRGB(hue, saturation, 1.0F - y / 256F));
            }

            texture2 = new DynamicTexture(image1);
        }
    }

    private void reg(String name, GuiButton button, Consumer<GuiButton> consumer) {
        reg(name, button, consumer, button1 -> {
        });
    }

    private void reg(String name, GuiButton button, Consumer<GuiButton> consumer, Consumer<GuiButton> tick) {
        buttonList.add(button);
        clicks.put(button, consumer);
        updates.put(button, tick);
        nameMap.put(name, button);
    }

    private int nextId() {
        return ++ids;
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    private void repack() {
        buttonList.clear();
        clicks.clear();
        updates.clear();
        nameMap.clear();
        ScaledResolution current = ResolutionUtil.current();
        int start_y = Math.max((int) (current.getScaledHeight_double() * .1) - 20, 5);
        int posX = (int) (current.getScaledWidth_double() * .5) - 100;
        reg("pos", new GuiButton(nextId(), posX, start_y, "Change Position"), button ->
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new MoveElementGui(mod, element)));
        reg("items", new GuiButton(nextId(), posX, start_y + 22, "Change Items"), button ->
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new EditItemsGui(element, mod)));

        //Highlighted
        reg("Highlight", new GuiButton(nextId(), posX, start_y + 22 * 2, "-"), button -> {
            //On click
            element.setHighlighted(!element.isHighlighted());
        }, button -> {
            //On Gui Update
            button.displayString = ChatColor.YELLOW.toString() + "Highlighted: " + (element.isHighlighted() ? ChatColor.GREEN + "Yes" : ChatColor.RED.toString() + "No");
        });


        //Shadow
        reg("shadow", new GuiButton(nextId(), posX, start_y + 22 * 3, "-"), button -> {
            //On click
            element.setShadow(!element.isShadow());
        }, button -> {
            //On Gui Update
            button.displayString = ChatColor.YELLOW.toString() + "Shadow: " + (element.isShadow() ? ChatColor.GREEN + "Yes" : ChatColor.RED.toString() + "No");
        });
        reg("Toggle Right", new GuiButton(nextId(), posX, start_y + 22 * 4, "-"), button -> {
            //On click
            element.setRightSided(!element.isRightSided());
        }, button -> {
            //On Gui Update
            button.displayString = ChatColor.YELLOW.toString() + "Right side: " + (element.isRightSided() ? ChatColor.GREEN + "Yes" : ChatColor.RED.toString() + "No");
        });
        //*4

        reg("Scale Slider", new GuiSlider(nextId(), 5, 5, 200, 20, "Scale: ", "",
            50, 200, element.getScale() * 100D, false, true), button -> {
            //clicked
            //Toggle between chroma types.

        }, button -> {
            //on tick
            element.setScale(((GuiSlider) button).getValue() / 100D);
            button.displayString = EnumChatFormatting.YELLOW + "Scale: " + ((GuiSlider) button).getValueInt() + "%";
        });

        reg("color", new GuiButton(nextId(), posX, start_y + 22 * 5, "-"), button -> {
            //clicked
            //Chroma -> RGB -> Color Pallet -> Chroma
            if (element.isChroma()) {
                element.setChroma(false);
                element.setRgb(true);
            } else if (element.isRGB()) {
                element.setRgb(false);
                element.setColorPallet(true);
            } else {
                element.setColorPallet(false);
                element.setChroma(true);
            }
        }, button -> {
            //on tick
            String type = "Error";

            if (element.isRGB())
                type = "RGB";
            if (element.isColorPallet())
                type = "Color Pallet";
            if (element.isChroma())
                type = "Chroma";
            button.displayString = ChatColor.YELLOW + "Color mode: " + ChatColor.GREEN.toString() + type;
        });

        reg("chromaMode", new GuiButton(nextId(), posX, start_y + 22 * 6, "-"), button -> {
            //clicked
            //Toggle between chroma types.
            element.setStaticChroma(!element.isStaticChroma());
        }, button -> {
            //on tick
            if (!element.isChroma()) {
                button.enabled = false;
                button.visible = false;
            } else {
                button.visible = true;
                button.enabled = true;
                button.displayString = ChatColor.YELLOW + "Chroma mode: " + (element.isStaticChroma() ? ChatColor.GREEN + "Static" : ChatColor.GREEN + "Wave");
            }
        });
        reg("redSlider", new GuiSlider(nextId(), posX, start_y + 22 * 6, 200, 20, "Red: ", "", 0, 255,
            element.getData().optInt("red"), false, true), button -> {
        }, button -> {
            //on tick
            if (!element.isRGB()) {
                button.enabled = false;
                button.visible = false;
            } else {
                button.visible = true;
                button.enabled = true;
                element.getData().put("red", ((GuiSlider) button).getValueInt());
                button.displayString = EnumChatFormatting.YELLOW + "Red: " + (element.getData().optInt("red"));
            }
        });


        reg("blueSlider", new GuiSlider(nextId(), posX, start_y + 22 * 8, 200, 20, "Blue: ", "", 0,
            255, element.getData().optInt("blue"), false, true), button -> {
            //clicked
            //Toggle between chroma types.
        }, button -> {
            //on tick
            if (!element.isRGB()) {
                button.enabled = false;
                button.visible = false;
            } else {
                button.visible = true;
                button.enabled = true;
                element.getData().put("blue", ((GuiSlider) button).getValueInt());
                button.displayString = EnumChatFormatting.YELLOW + "Blue: " + (element.getData().optInt("blue"));
            }
        });
        reg("greenSlider", new GuiSlider(nextId(), posX, start_y + 22 * 7, 200, 20, "Green: ", "",
            0, 255, element.getData().optInt("green"), false, true), button -> {
            //clicked
            //Toggle between chroma types.

        }, button -> {
            //on tick
            if (!element.isRGB()) {
                button.enabled = false;
                button.visible = false;
            } else {
                button.visible = true;
                button.enabled = true;
                element.getData().put("green", ((GuiSlider) button).getValueInt());
                button.displayString = EnumChatFormatting.YELLOW + "Green: " + (element.getData().optInt("green"));
            }
        });
        reg("Back", new GuiButton(nextId(), 2, ResolutionUtil.current().getScaledHeight() - 22, 100, 20, "Back"),
            (guiButton) -> Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GeneralConfigGui(mod)), (guiButton) -> {
            });
        reg("Delete", new GuiButton(nextId(), 2, ResolutionUtil.current().getScaledHeight() - 22 * 2, 100, 20, "Delete"), (guiButton) -> {

            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new GeneralConfigGui(mod));
            ChromaHUDApi.getInstance().getElements().remove(element);
        }, (guiButton) -> {
        });


    }

    @Override
    protected void actionPerformed(GuiButton button) {
        Consumer<GuiButton> guiButtonConsumer = clicks.get(button);
        if (guiButtonConsumer != null) guiButtonConsumer.accept(button);
    }

    @Override
    public void updateScreen() {
        ScaledResolution current = ResolutionUtil.current();
        if (current.getScaledWidth() != lastWidth || current.getScaledHeight() != lastHeight) {
            repack();
            lastWidth = current.getScaledWidth();
            lastHeight = current.getScaledHeight();
        }

        if (element.isRGB()) element.recalculateColor();

        buttonList.forEach(guiButton -> {
            Consumer<GuiButton> guiButtonConsumer = updates.get(guiButton);
            if (guiButtonConsumer != null) guiButtonConsumer.accept(guiButton);
        });
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        apply(mouseX, mouseY);
    }

    public float scale() {
        return availableSpace() / 285F;
    }

    private void apply(int mouseX, int mouseY) {
        if (mouseLock) return;
        if (!Mouse.isButtonDown(0)) return;
        if (!element.isColorPallet()) return;

        float scale = scale();
        int left = posX(1);
        int right = posX(2);
        int top = posY(1);
        int bottom = posY(3);
        float x;
        float y;

        if (mouseX > left && mouseX < right) {
            if (mouseY > top && mouseY < bottom) {
                x = mouseX - left;
                y = mouseY - top;
                x /= scale;
                y /= scale;

                if (y > 0 && y <= 256) {
                    if (x < 256 && x > 0) {
                        hue = (int) x;
                        saturation = (int) (256 - y);
                        regenImage();
                        lastX = mouseX;
                        lastY = mouseY;
                    } else if (x > 256 + 15 && x < 256 + 15 + 15) {
                        brightness = (int) y;
                        regenImage();
                    }
                }
            }
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        mod.saveState();
    }

    private void drawCircle(int x, int y) {
        GlStateManager.color(0F, 0F, 0F, 1.0F);
        Gui.drawRect(x - 2, y + 12, x + 2, y + 3, Color.BLACK.getRGB());
        Gui.drawRect(x - 2, y - 3, x + 2, y - 12, Color.BLACK.getRGB());
        Gui.drawRect(x + 12, y - 2, x + 3, y + 2, Color.BLACK.getRGB());
        Gui.drawRect(x - 12, y - 2, x - 3, y + 2, Color.BLACK.getRGB());
        Gui.drawRect(posX(2) + 5,

            (int) (startY() + (brightness - 2) * scale()),
            posX(2) + 15, (int) (startY() + (brightness + 2) * scale()), Color.BLACK.getRGB());
        element.setColor(Color.HSBtoRGB(hue / 255F, saturation / 255F, 1.0F - brightness / 255F));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution current = ResolutionUtil.current();
        mouseLock = mouseLock && Mouse.isButtonDown(0);
        drawRect(0, 0, current.getScaledWidth(), current.getScaledHeight(), new Color(0, 0, 0, 150).getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);

        ElementRenderer.startDrawing(element);
        element.renderEditView();
        ElementRenderer.endDrawing(element);
        int left = posX(1);
        int top = posY(2);
        int right = posX(2);
        int size = right - left;

        if (element.isRGB()) {
            int start_y = Math.max((int) (current.getScaledHeight_double() * .1) - 20, 5) + 22 * 8 + 25;
            int left1 = current.getScaledWidth() / 2 - 100;
            int right1 = current.getScaledWidth() / 2 + 100;
            Gui.drawRect(left1, start_y, right1, right1 - left1 + 200, element.getColor());
        }

        if (!element.isColorPallet()) return;

        apply(mouseX, mouseY);
        GlStateManager.bindTexture(texture.getGlTextureId());
        GlStateManager.enableTexture2D();
        GL11.glPushMatrix();
        GL11.glTranslatef(left, top, 0);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);//
        GlStateManager.scale(size / 285F, size / 285F, 0);
        drawTexturedModalRect(0, 0, 0, 0, 256, 256);

        if (texture2 != null) {
            GlStateManager.bindTexture(texture2.getGlTextureId());
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslatef(256 + 15, 0, 0);
            drawTexturedModalRect(0, 0, 0, 0, 15, 256);
        }

        GlStateManager.scale(285F / size, 285F / size, 0);
        GL11.glPopMatrix();
        if (lastX != 0 && lastY != 0) drawCircle(lastX, lastY);
    }


    //Method vars to make things nice and easy
    private int availableSpace() {
        ScaledResolution current = ResolutionUtil.current();
        int yMin = current.getScaledHeight() - 15 - startY();
        if (yMin + 20 > current.getScaledWidth()) return yMin - 50;
        return yMin;
    }


    private int startY() {
        ScaledResolution current = ResolutionUtil.current();
        return (int) (Math.max((current.getScaledHeight_double() * .1) - 20, 5) + 22 * 6);
    }

    /*
      1: top left
      2: top right
      3: bottom left
      4: bottom right
       */
    private int posX(int vertex) {
        int i = availableSpace();
        ScaledResolution current = ResolutionUtil.current();
        switch (vertex) {
            case 1:
            case 3: {
                return (current.getScaledWidth() - i + 30) / 2;
            }
            case 2:
            case 4: {
                return (current.getScaledWidth() + i + 30) / 2;
            }

            default:
                throw new IllegalArgumentException("Vertex not found " + vertex);
        }
    }

    private int posY(int vertex) {
        switch (vertex) {
            case 1:
            case 2: {
                return startY();
            }
            case 3:
            case 4: {
                return startY() + availableSpace();
            }

            default:
                throw new IllegalArgumentException("Vertex not found " + vertex);
        }
    }
}
