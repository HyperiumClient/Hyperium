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
import cc.hyperium.gui.GuiButtonIcon;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.mods.chromahud.ChromaHUD;
import cc.hyperium.mods.chromahud.ChromaHUDApi;
import cc.hyperium.mods.chromahud.DisplayElement;
import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.Dimension;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Sk1er
 */
public class GeneralConfigGui extends GuiScreen {
    private final ChromaHUD mod;
    private final Map<GuiButton, Consumer<GuiButton>> clicks = new HashMap<>();
    private boolean mouseDown;
    private DisplayElement currentElement;
    private GuiButton edit;
    private double lastX;
    private double lastY;
    private boolean lastD;
    private boolean pastClick;
    private int dTick; //double click delay

    public GeneralConfigGui(ChromaHUD mod) {
        this.mod = mod;
    }

    private void reg(GuiButton button, Consumer<GuiButton> consumer) {
        buttonList.add(button);
        clicks.put(button, consumer);
    }

    @Override
    public void initGui() {
        super.initGui();
        reg((edit = new GuiButtonIcon(1, new ResourceLocation("textures/chromahud/iconsheet.png"), 5, 0, 1, .4f)), button -> {
            //Open Gui for editing element
            if (currentElement != null) {
                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new DisplayElementConfig(currentElement, mod));
            }
        });
        ((GuiButtonIcon) edit).setOutline(true);
        reg(new GuiButton(2, 2, ResolutionUtil.current().getScaledHeight() - 22, 100, 20, "New"), (guiButton) -> {
            DisplayElement blank = DisplayElement.blank();
            ChromaHUDApi.getInstance().getElements().add(blank);
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new DisplayElementConfig(blank, mod));
        });

        edit.visible = false;
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution current = ResolutionUtil.current();
        drawRect(0, 0, current.getScaledWidth(), current.getScaledHeight(), new Color(0, 0, 0, 150).getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
        List<DisplayElement> elementList = mod.getDisplayElements();
        elementList.stream().filter(element -> currentElement == null || currentElement != element).forEach(element -> {
            ElementRenderer.startDrawing(element);
            try {
                element.drawForConfig();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ElementRenderer.endDrawing(element);
        });

        if (currentElement != null) {
            ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
            double offset = currentElement.isRightSided() ? currentElement.getDimensions().getWidth() : 0;

            // Left top right bottom
            double x1 = currentElement.getXloc() * resolution.getScaledWidth_double() - offset;
            double x2 = currentElement.getXloc() * resolution.getScaledWidth_double() + currentElement.getDimensions().getWidth() - offset;
            double y1 = currentElement.getYloc() * resolution.getScaledHeight_double();
            double y2 = currentElement.getYloc() * resolution.getScaledHeight_double() + currentElement.getDimensions().getHeight();

            // Chroma selection background
            if (currentElement.isSelected()) {
                HyperiumGui.drawChromaBox((int) x1 - 2, (int) y1 - 2, (int) x2 + 2, (int) y2 - 2, 0.2F);
            }

            ElementRenderer.startDrawing(currentElement);

            // Draw the element after the background
            currentElement.drawForConfig();

            ElementRenderer.endDrawing(currentElement);

            // Turns the edit image on
            edit.visible = true;

            int propX = (int) x1 - 5;
            int propY = (int) y1 - 20;
            if (propX < 10 || propX > resolution.getScaledWidth() - 200) {
                propX = resolution.getScaledWidth() / 2;
            }
            if (propY > resolution.getScaledHeight() - 5 || propY < 0)
                propY = resolution.getScaledHeight() / 2;
            edit.xPosition = propX;
            edit.yPosition = propY;
            // moving the thing
            if (Mouse.isButtonDown(0)) {
                if (mouseX > x1 - 2 && mouseX < x2 + 2 && mouseY > y1 - 2 && mouseY < y2 + 2 || lastD) {
                    //inside
                    double x3 = Mouse.getX() / ResolutionUtil.current().getScaledWidth_double();
                    double y3 = Mouse.getY() / ResolutionUtil.current().getScaledHeight_double();
                    currentElement.setXloc(currentElement.getXloc() - (lastX - x3) / ((double) ResolutionUtil.current().getScaleFactor()));
                    currentElement.setYloc(currentElement.getYloc() + (lastY - y3) / ((double) ResolutionUtil.current().getScaleFactor()));
                    //Math to keep it inside screen
                    if (currentElement.getXloc() * resolution.getScaledWidth_double() - offset < 0) {
                        if (currentElement.isRightSided())
                            currentElement.setXloc(offset / resolution.getScaledWidth_double());
                        else
                            currentElement.setXloc(0);
                    }
                    if (currentElement.getYloc() < 0) currentElement.setYloc(0);
                    if (currentElement.getXloc() * resolution.getScaledWidth() + currentElement.getDimensions().getWidth() - offset > resolution.getScaledWidth()) {
                        currentElement.setXloc(currentElement.isRightSided() ? 1.0 :
                            (resolution.getScaledWidth_double() - currentElement.getDimensions().getWidth()) / resolution.getScaledWidth_double());
                    }

                    if (currentElement.getYloc() * resolution.getScaledHeight() + currentElement.getDimensions().getHeight() > resolution.getScaledHeight()) {
                        currentElement.setYloc((resolution.getScaledHeight_double() - currentElement.getDimensions().getHeight()) / resolution.getScaledHeight_double());
                    }

                    lastD = true;
                }
            } else {
                lastD = false;
            }
        } else {
            edit.visible = false;
        }

        lastX = Mouse.getX() / ResolutionUtil.current().getScaledWidth_double();
        lastY = Mouse.getY() / ResolutionUtil.current().getScaledHeight_double();
        if (dTick <= 0 && pastClick) pastClick = false;
        if (pastClick) dTick--;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        ScaledResolution current = ResolutionUtil.current();
        int i = Mouse.getEventDWheel();
        List<DisplayElement> elements = ChromaHUDApi.getInstance().getElements();
        if (elements.size() > 0) {
            if (i < 0) {
                if (currentElement == null) {
                    currentElement = elements.get(0);
                } else {
                    int i1 = elements.indexOf(currentElement);
                    i1++;
                    if (i1 > elements.size() - 1) i1 = 0;
                    currentElement = elements.get(i1);
                }
            } else if (i > 0) {
                if (currentElement == null) {
                    currentElement = elements.get(0);
                } else {
                    int i1 = elements.indexOf(currentElement);
                    i1--;
                    if (i1 < 0) i1 = elements.size() - 1;
                    currentElement = elements.get(i1);
                }
            }
        }

        boolean isOver = false;

        for (GuiButton button : buttonList) {
            if (button.isMouseOver())
                isOver = true;
        }

        if (!mouseDown && Mouse.isButtonDown(0) && !isOver) {
            //Mouse pushed down. Calculate current element
            int clickX = Mouse.getX() / current.getScaleFactor();
            int clickY = Mouse.getY() / current.getScaleFactor();
            boolean found = false;
            for (DisplayElement element : mod.getDisplayElements()) {
                Dimension dimension = element.getDimensions();
                double displayXLoc = current.getScaledWidth_double() * element.getXloc();
                if (element.isRightSided()) displayXLoc -= element.getDimensions().getWidth();
                double displayYLoc = current.getScaledHeight_double() - current.getScaledHeight_double() * element.getYloc();

                if (clickX > displayXLoc
                    && clickX < displayXLoc + dimension.getWidth()
                    && clickY < displayYLoc
                    && clickY > displayYLoc - dimension.getHeight()) {

                    // Open gui
                    if (currentElement != null && currentElement == element && pastClick) {
                        // Safely nuke the fields and deactivate the chroma effect
                        element.setSelected(false);
                        currentElement = null;

                        mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new DisplayElementConfig(element, mod));
                        return;
                    }

                    currentElement = element;
                    element.setSelected(true);
                    found = true;
                    break;
                }
            }

            if (!found) {
                if (currentElement != null) currentElement.setSelected(false);
                currentElement = null;
            }
        }

        mouseDown = Mouse.isButtonDown(0);
        if (mouseDown) {
            pastClick = true;
            dTick = 5;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        Consumer<GuiButton> guiButtonConsumer = clicks.get(button);
        if (guiButtonConsumer != null) guiButtonConsumer.accept(button);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        mod.saveState();
    }

    public void display() {
        Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(this);
    }


    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_RETURN && currentElement != null) {
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new DisplayElementConfig(currentElement, mod));
        }

        super.keyTyped(typedChar, keyCode);
    }
}
