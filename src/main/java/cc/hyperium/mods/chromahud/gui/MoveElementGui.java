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

import cc.hyperium.mods.chromahud.ChromaHUD;
import cc.hyperium.mods.chromahud.DisplayElement;
import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

class MoveElementGui extends GuiScreen {
    private final Map<GuiButton, Consumer<GuiButton>> clicks = new HashMap<>();
    private final Map<GuiButton, Consumer<GuiButton>> updates = new HashMap<>();
    private final ChromaHUD mod;
    private final DisplayElement element;
    private GuiButton edit;
    private double lastX;
    private double lastY;
    private boolean lastD;
    private boolean mouseLock;

    public MoveElementGui(ChromaHUD mod, DisplayElement element) {
        this.mod = mod;
        this.element = element;
        mouseLock = Mouse.isButtonDown(0);
    }

    @Override
    public void initGui() {
        super.initGui();
        reg(edit = new GuiButton(1, 5, 5, 100, 20, "Save"), button ->
            mc.displayGuiScreen(new DisplayElementConfig(element, mod)));
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        mod.saveState();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        buttonList.forEach(guiButton -> {
            Consumer<GuiButton> guiButtonConsumer = updates.get(guiButton);
            if (guiButtonConsumer != null) guiButtonConsumer.accept(guiButton);
        });
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        Consumer<GuiButton> guiButtonConsumer = clicks.get(button);
        if (guiButtonConsumer != null) guiButtonConsumer.accept(button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution current = ResolutionUtil.current();
        drawRect(0, 0, current.getScaledWidth(), current.getScaledHeight(), new Color(0, 0, 0, 150).getRGB());
        mouseLock = mouseLock && Mouse.isButtonDown(0);
        super.drawScreen(mouseX, mouseY, partialTicks);
        ElementRenderer.startDrawing(element);
        element.drawForConfig();
        ElementRenderer.endDrawing(element);
        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        double offset = element.isRightSided() ? element.getDimensions().getWidth() : 0;
        double x1 = element.getXloc() * resolution.getScaledWidth_double() - offset;
        double x2 = element.getXloc() * resolution.getScaledWidth_double() + element.getDimensions().getWidth() - offset;
        double y1 = element.getYloc() * resolution.getScaledHeight_double();
        double y2 = element.getYloc() * resolution.getScaledHeight_double() + element.getDimensions().getHeight();
        //Left top right bottom

        //Outline
        drawHorizontalLine((int) (x1 - 5), (int) (x2 + 5), (int) y1 - 5, Color.RED.getRGB());
        drawHorizontalLine((int) (x1 - 5), (int) (x2 + 5), (int) y2 + 5, Color.RED.getRGB());
        drawVerticalLine((int) x1 - 5, (int) (y1 - 5), (int) (y2 + 5), Color.RED.getRGB());
        drawVerticalLine((int) x2 + 5, (int) (y1 - 5), (int) (y2 + 5), Color.RED.getRGB());
        int propX = (int) x1 - 5;
        int propY = (int) y1 - 30;

        if (propX < 10 || propX > resolution.getScaledWidth() - 200) propX = resolution.getScaledWidth() / 2;
        if (propY > resolution.getScaledHeight() - 5 || propY < 0) propY = resolution.getScaledHeight() / 2;

        edit.xPosition = propX;
        edit.yPosition = propY;

        if (Mouse.isButtonDown(0) && !mouseLock) {
            if (mouseX > x1 - 2 && mouseX < x2 + 2 && mouseY > y1 - 2 && mouseY < y2 + 2 || lastD) {
                //inside
                double x3 = Mouse.getX() / ResolutionUtil.current().getScaledWidth_double();
                double y3 = Mouse.getY() / ResolutionUtil.current().getScaledHeight_double();
                element.setXloc(element.getXloc() - (lastX - x3) / ((double) ResolutionUtil.current().getScaleFactor()));
                element.setYloc(element.getYloc() + (lastY - y3) / ((double) ResolutionUtil.current().getScaleFactor()));

                //Math to keep it inside screen
                if (element.getXloc() * resolution.getScaledWidth_double() - offset < 0) {
                    if (element.isRightSided())
                        element.setXloc(offset / resolution.getScaledWidth_double());
                    else
                        element.setXloc(0);
                }

                if (element.getYloc() < 0) element.setYloc(0);
                if (element.getXloc() * resolution.getScaledWidth() + element.getDimensions().getWidth() - offset > resolution.getScaledWidth()) {
                    element.setXloc(element.isRightSided() ? 1.0 : (resolution.getScaledWidth_double() - element.getDimensions().getWidth()) / resolution.getScaledWidth_double());
                }

                if (element.getYloc() * resolution.getScaledHeight() + element.getDimensions().getHeight() > resolution.getScaledHeight()) {
                    element.setYloc((resolution.getScaledHeight_double() - element.getDimensions().getHeight()) / resolution.getScaledHeight_double());
                }

                lastD = true;
            }
        } else {
            lastD = false;
        }

        lastX = Mouse.getX() / ResolutionUtil.current().getScaledWidth_double();
        lastY = Mouse.getY() / ResolutionUtil.current().getScaledHeight_double();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private void reg(GuiButton button, Consumer<GuiButton> consumer) {
        buttonList.removeIf(button1 -> button1.id == button.id);
        clicks.keySet().removeIf(button1 -> button1.id == button.id);
        buttonList.add(button);
        if (consumer != null) clicks.put(button, consumer);
    }
}
