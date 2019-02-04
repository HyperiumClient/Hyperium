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

package cc.hyperium.mods.chromahud.gui;


import cc.hyperium.Hyperium;
import cc.hyperium.mods.chromahud.ChromaHUD;
import cc.hyperium.mods.chromahud.ChromaHUDApi;
import cc.hyperium.mods.chromahud.DisplayElement;
import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.ChromaHUDDescription;
import cc.hyperium.mods.chromahud.api.ChromaHUDParser;
import cc.hyperium.mods.chromahud.api.Dimension;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class AddItemsGui extends GuiScreen {
    private final ChromaHUD mod;
    private final DisplayElement element;
    private final Map<GuiButton, Consumer<GuiButton>> clicks = new HashMap<>();
    private final Map<GuiButton, Consumer<GuiButton>> updates = new HashMap<>();
    private final Map<String, GuiButton> nameMap = new HashMap<>();
    private final List<DisplayElement> all = new ArrayList<>();
    private final DisplayElement target;
    private int tmpId = 0;
    private boolean adding = true;
    private int offset = 0;
    private boolean mouseLock;

    public AddItemsGui(ChromaHUD mod, DisplayElement element) {
        this.mod = mod;
        this.element = element;
        //Make all parsers so we can access them
        for (ChromaHUDParser chromaHUDParser : ChromaHUDApi.getInstance().getParsers()) {
            for (String s : chromaHUDParser.getNames().keySet()) {
                DisplayItem item = chromaHUDParser.parse(s, 0, new JsonHolder().put("type", s));
                DisplayElement blank = DisplayElement.blank();
                blank.getDisplayItems().add(item);
                blank.adjustOrdinal();
                all.add(blank);
            }
        }
        for (DisplayElement displayElement : all) {
            displayElement.drawForConfig();
        }
        target = DisplayElement.blank();
        target.setColor(Color.GREEN.getRGB());
        mouseLock = Mouse.isButtonDown(0);
    }

    private int nextId() {
        return (++tmpId);
    }

    @Override
    public void initGui() {
        super.initGui();
        reg("Add", new GuiButton(nextId(), 2, 2, 100, 20, "Add"), (guiButton) -> {
            //On click
            adding = true;
            offset = 0;
        }, (guiButton) -> guiButton.enabled = !adding);
        reg("Explore", new GuiButton(nextId(), 2, 23, 100, 20, "Explore"), (guiButton) -> {
            //On click
            adding = false;
            offset = 0;
        }, (guiButton) -> guiButton.enabled = adding);

        reg("Down", new GuiButton(nextId(), 2, 23 + 21 * 2, 100, 20, "Scroll Down"), (guiButton) -> {
            //On click
            offset += 50;
        }, (guiButton) -> {
        });
        reg("Up", new GuiButton(nextId(), 2, 23 + 21 * 1, 100, 20, "Scroll Up"), (guiButton) -> {
            //On click
            offset -= 50;
        }, (guiButton) -> {
        });


        reg("Back", new GuiButton(nextId(), 2, ResolutionUtil.current().getScaledHeight() - 22, 100, 20, "Back"), (guiButton) -> Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new EditItemsGui(element, mod)), (guiButton) -> {
        });

    }

    private void reg(String name, GuiButton button, Consumer<GuiButton> consumer, Consumer<GuiButton> tick) {
        this.buttonList.add(button);
        this.clicks.put(button, consumer);
        this.updates.put(button, tick);
        this.nameMap.put(name, button);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        Consumer<GuiButton> guiButtonConsumer = clicks.get(button);
        if (guiButtonConsumer != null) {
            guiButtonConsumer.accept(button);
        }

    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        for (GuiButton guiButton : buttonList) {
            Consumer<GuiButton> guiButtonConsumer = updates.get(guiButton);
            if (guiButtonConsumer != null) {
                guiButtonConsumer.accept(guiButton);
            }
        }
    }

    @Override
    public void drawDefaultBackground() {
        super.drawDefaultBackground();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i < 0) {
            offset += 10;
        } else if (i > 0) {
            offset -= 10;
        }
    }

    @Override
    public void onGuiClosed() {
        mod.saveState();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        mouseLock = mouseLock && Mouse.isButtonDown(0);
        ScaledResolution current = ResolutionUtil.current();
        drawRect(0, 0, current.getScaledWidth(), current.getScaledHeight(), new Color(0, 0, 0, 150).getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
        ElementRenderer.startDrawing(target);
        if (adding) {
            Color defaultColor = new Color(255, 255, 255, 100);

            int cursorY = 50 + offset;
            drawCenteredString(mc.fontRendererObj, "Click Explore to see examples!", current.getScaledWidth() / 2, cursorY - 30, Color.RED.getRGB());
            List<ChromaHUDParser> parsers = ChromaHUDApi.getInstance().getParsers();
            for (ChromaHUDParser parser : parsers) {

                Map<String, String> names = parser.getNames();
                for (String s : names.keySet()) {
                    String text1 = names.get(s) + "";
                    drawRect(current.getScaledWidth() / 2 - 80, cursorY, current.getScaledWidth() / 2 + 80, cursorY + 20, defaultColor.getRGB());
                    int j = Color.RED.getRGB();
                    int width = 160;
                    int height = 20;
                    mc.fontRendererObj.drawString(text1, (current.getScaledWidth() / 2 - 80 + width / 2 - mc.fontRendererObj.getStringWidth(text1) / 2), cursorY + (height - 8) / 2, j, false);
                    int i = ResolutionUtil.current().getScaledHeight() - (Mouse.getY() / current.getScaleFactor());
                    if (Mouse.isButtonDown(0) && !mouseLock)
                        if (i >= cursorY && i <= cursorY + 23) {
                            int i1 = Mouse.getX() / current.getScaleFactor();
                            if (i1 >= current.getScaledWidth() / 2 - 80 && i1 <= current.getScaledWidth() / 2 + 80) {
                                DisplayItem item = ChromaHUDApi.getInstance().parse(s, 0, new JsonHolder().put("type", s));
                                element.getDisplayItems().add(item);
                                element.adjustOrdinal();
                                Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new EditItemsGui(element, mod));

                            }
                        }
                    cursorY += 23;


                }

            }
        } else {
            int cursorY = 50 + offset;
            List<ChromaHUDParser> parsers = ChromaHUDApi.getInstance().getParsers();
            for (ChromaHUDParser parser : parsers) {
                ChromaHUDDescription description = parser.description();
                String text = "Items in " + description.getName() + ".";
                mc.fontRendererObj.drawString(text, (current.getScaledWidth() - mc.fontRendererObj.getStringWidth(text)) / 2, cursorY, Color.RED.getRGB(), true);
                cursorY += 30;

                Map<String, String> names = parser.getNames();
                for (String s : names.keySet()) {
                    String text1 = names.get(s) + ": ";
                    DisplayElement displayElement = find(s);
                    if (displayElement == null) {
                        String text2 = "ERROR LOCATING DISPLAY ELEMENT " + s;
                        mc.fontRendererObj.drawString(text2, (current.getScaledWidth() - mc.fontRendererObj.getStringWidth(text2)) / 2, cursorY, Color.RED.getRGB(), true);
                        cursorY += 15;
                        continue;
                    }
                    Dimension dimensions = displayElement.getDimensions();
                    int stringWidth = mc.fontRendererObj.getStringWidth(text1);
                    double totalWidth = dimensions.getWidth() + stringWidth;
                    double left = (current.getScaledWidth_double() - totalWidth) / 2;
                    double startDraw = left + stringWidth;
                    displayElement.setXloc(startDraw / current.getScaledWidth_double());
                    displayElement.setYloc(((double) cursorY) / current.getScaledHeight_double());
                    displayElement.drawForConfig();
                    mc.fontRendererObj.drawString(text1, (float) ((current.getScaledWidth() - totalWidth) / 2), cursorY, Color.RED.getRGB(), true);
                    cursorY += dimensions.getHeight() + 5;

                }

            }
        }
        ElementRenderer.endDrawing(target);
    }

    private DisplayElement find(String key) {
        for (DisplayElement displayElement : all) {
            if (displayElement.getDisplayItems().get(0).getType().equalsIgnoreCase(key)) {
                return displayElement;
            }
        }
        return null;
    }
}
