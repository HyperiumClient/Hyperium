/*
 *  Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  Hyperium Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mods.chromahud.gui;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.TickEvent;
import cc.hyperium.gui.GuiButtonIcon;
import cc.hyperium.mods.chromahud.ChromaHUD;
import cc.hyperium.mods.chromahud.ChromaHUDApi;
import cc.hyperium.mods.chromahud.DisplayElement;
import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.Dimension;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by mitchellkatz on 5/30/17.
 */
public class GeneralConfigGui extends GuiScreen {
    private ChromaHUD mod;
    private boolean mouseDown;
    private DisplayElement currentElement;
    private GuiButton edit;
    private HashMap<GuiButton, Consumer<GuiButton>> clicks = new HashMap<>();
    private boolean mouseLock;

    public GeneralConfigGui(ChromaHUD mod) {
        this.mod = mod;
        mouseLock = Mouse.isButtonDown(0);
    }

    private void reg(GuiButton button, Consumer<GuiButton> consumer) {
        this.buttonList.add(button);
        this.clicks.put(button, consumer);
    }

    @Override
    public void initGui() {
        super.initGui();
        reg((edit = new GuiButtonIcon(1, new ResourceLocation("textures/chromahud/iconsheet.png"),5, 0, 1, .2f) ), button -> {
            //Open Gui for editing element
            if (currentElement != null) {
                Minecraft.getMinecraft().displayGuiScreen(new DisplayElementConfig(currentElement, mod));
            }
        });
        reg(new GuiButton(2, 2, ResolutionUtil.current().getScaledHeight() - 22, 100, 20, "New"), (guiButton) -> {
            DisplayElement blank = DisplayElement.blank();
            ChromaHUDApi.getInstance().getElements().add(blank);
            System.out.println(ChromaHUDApi.getInstance().getElements());
            Minecraft.getMinecraft().displayGuiScreen(new DisplayElementConfig(blank, mod));
        });

        edit.visible = false;
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution current = ResolutionUtil.current();
        drawRect(0, 0, current.getScaledWidth(), current.getScaledHeight(), new Color(0, 0, 0, 150).getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
        List<DisplayElement> elementList = mod.getDisplayElements();
        for (DisplayElement element : elementList) {
            ElementRenderer.startDrawing(element);
            try {
                element.drawForConfig();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ElementRenderer.endDrawing(element);
        }
        if (currentElement != null) {
            boolean cbHud = currentElement.getDisplayItems().stream().anyMatch(i -> i.getType().contains("CB"));
            ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
            double offset = currentElement.isRightSided() ? currentElement.getDimensions().getWidth() : 0;
//            double offset= 4;
            double x1 = currentElement.getXloc() * resolution.getScaledWidth_double()-offset;
            double x2 = currentElement.getXloc() * resolution.getScaledWidth_double() + (cbHud ? 60 : currentElement.getDimensions().getWidth())-offset;
            double y1 = currentElement.getYloc() * resolution.getScaledHeight_double();
            double y2 = currentElement.getYloc() * resolution.getScaledHeight_double() + currentElement.getDimensions().getHeight();
            //Left top right bottom

            //Outline
            drawHorizontalLine((int) (x1 - 5), (int) (x2 + 5), (int) y1 - 5, Color.RED.getRGB());
            drawHorizontalLine((int) (x1 - 5), (int) (x2 + 5), (int) y2 + 5, Color.RED.getRGB());
            drawVerticalLine((int) x1 - 5, (int) (y1 - 5), (int) (y2 + 5), Color.RED.getRGB());
            drawVerticalLine((int) x2 + 5, (int) (y1 - 5), (int) (y2 + 5), Color.RED.getRGB());

//            drawVerticalLine((int) (y1 + 5), (int) (y1 - 5), (int) 3, Color.RED.getRGB());

//            Gui.drawRect((int) x1, (int) y1, (int) x2, (int) y2, Color.WHITE.getRGB());
//            currentElement.drawForConfig();
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
//            System.out.println("Y1: " + y1 +" res/2: " + (resolution.getScaledHeight() / 2));


//
        } else edit.visible = false;
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
                    if (i1 > elements.size() - 1)
                        i1 = 0;
                    currentElement = elements.get(i1);
                }
            } else if (i > 0) {
                if (currentElement == null) {
                    currentElement = elements.get(0);
                } else {
                    int i1 = elements.indexOf(currentElement);
                    i1--;
                    if (i1 < 0)
                        i1 = elements.size() - 1;
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
                if (element.isRightSided())
                    displayXLoc -= element.getDimensions().getWidth();
                double displayYLoc = current.getScaledHeight_double() - current.getScaledHeight_double() * element.getYloc();
                if (clickX > displayXLoc
                        && clickX < displayXLoc + dimension.getWidth()
                        && clickY < displayYLoc
                        && clickY > displayYLoc - dimension.getHeight()) {
                    this.currentElement = element;
                    found = true;
                    break;
                }
            }
            if (!found)
                currentElement = null;
        }
        mouseDown = Mouse.isButtonDown(0);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        Consumer<GuiButton> guiButtonConsumer = clicks.get(button);
        if (guiButtonConsumer != null) {
            guiButtonConsumer.accept(button);
        }

    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        mod.saveState();
    }
    
    public void display() {
        EventBus.INSTANCE.register(this);
    }
    
    @InvokeEvent
    public void tick(TickEvent e) {
        EventBus.INSTANCE.unregister(this);
        Minecraft.getMinecraft().displayGuiScreen(this);
    }
}
