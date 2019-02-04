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
import cc.hyperium.mods.chromahud.api.ButtonConfig;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.mods.chromahud.api.StringConfig;
import cc.hyperium.mods.chromahud.api.TextConfig;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EditItemsGui extends GuiScreen {
    private final DisplayElement element;
    private final Map<GuiButton, Consumer<GuiButton>> clicks = new HashMap<>();
    private final Map<GuiButton, Consumer<GuiButton>> updates = new HashMap<>();
    private final Map<String, GuiButton> nameMap = new HashMap<>();
    private final ChromaHUD mod;
    private DisplayItem modifying;
    private int tmpId;

    public EditItemsGui(DisplayElement element, ChromaHUD mod) {
        this.element = element;
        this.mod = mod;
        boolean mouseLock = Mouse.isButtonDown(0);
    }

    private int nextId() {
        return (++tmpId);
    }

    @Override
    public void initGui() {
        reg("add", new GuiButton(nextId(), 2, 2, 100, 20, "Add Items"), (guiButton) -> {
            //On click
            Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new AddItemsGui(mod, element));
        }, (guiButton) -> {

        });
        reg("Remove", new GuiButton(nextId(), 2, 23, 100, 20, "Remove Item"), (guiButton) -> {
            //On click
            if (modifying != null) {
                element.removeDisplayItem(modifying.getOrdinal());
                if (modifying.getOrdinal() >= element.getDisplayItems().size())
                    modifying = null;
            }
        }, (guiButton) -> guiButton.enabled = modifying != null);
        reg("Move Up", new GuiButton(nextId(), 2, 23 + 21, 100, 20, "Move Up"), (guiButton) -> {
            //On click
            if (modifying != null) {
                int i = modifying.getOrdinal();
                Collections.swap(element.getDisplayItems(), modifying.getOrdinal(), modifying.getOrdinal() - 1);
                element.adjustOrdinal();
            }
        }, (guiButton) -> guiButton.enabled = modifying != null && this.modifying.getOrdinal() > 0);
        reg("Move Down", new GuiButton(nextId(), 2, 23 + 21 * 2, 100, 20, "Move Down"), (guiButton) -> {
            //On click
            if (modifying != null) {
                int i = modifying.getOrdinal();
                Collections.swap(element.getDisplayItems(), modifying.getOrdinal(), modifying.getOrdinal() + 1);
                element.adjustOrdinal();
            }
        }, (guiButton) -> guiButton.enabled = modifying != null && this.modifying.getOrdinal() < this.element.getDisplayItems().size() - 1);


        reg("Back", new GuiButton(nextId(), 2, ResolutionUtil.current().getScaledHeight() - 22, 100, 20, "Back"), (guiButton) -> Hyperium.INSTANCE.getHandlers().getGuiDisplayHandler().setDisplayNextTick(new DisplayElementConfig(element, mod)), (guiButton) -> {
        });

    }

    @Override
    protected void actionPerformed(GuiButton button) {
        Consumer<GuiButton> guiButtonConsumer = clicks.get(button);
        if (guiButtonConsumer != null) {
            guiButtonConsumer.accept(button);
        }

    }

    private void reg(String name, GuiButton button, Consumer<GuiButton> consumer) {
        reg(name, button, consumer, button1 -> {

        });
    }

    private void reg(String name, GuiButton button, Consumer<GuiButton> consumer, Consumer<GuiButton> tick) {
        this.buttonList.add(button);
        this.clicks.put(button, consumer);
        this.updates.put(button, tick);
        this.nameMap.put(name, button);
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
    public void onGuiClosed() {
        mod.saveState();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (modifying == null)
            return;
        List<TextConfig> textConfigs = ChromaHUDApi.getInstance().getTextConfigs(modifying.getType());
        if (textConfigs != null && !textConfigs.isEmpty()) {
            for (TextConfig config : textConfigs) {
                GuiTextField textField = config.getTextField();
                textField.textboxKeyTyped(typedChar, keyCode);
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (modifying != null) {
            List<ButtonConfig> configs = ChromaHUDApi.getInstance().getButtonConfigs(modifying.getType());
            if (configs != null && !configs.isEmpty()) {

                for (ButtonConfig config : configs) {
                    GuiButton button = config.getButton();
                    if (button.mousePressed(mc, mouseX, mouseY)) {
                        config.getAction().accept(button, modifying);
                        return;
                    }
                }
            }
            List<TextConfig> textConfigs = ChromaHUDApi.getInstance().getTextConfigs(modifying.getType());
            if (textConfigs != null && !textConfigs.isEmpty()) {
                for (TextConfig config : textConfigs) {
                    GuiTextField textField = config.getTextField();
                    textField.mouseClicked(mouseX, mouseY, mouseButton);
                    if (textField.isFocused()) {
                        return;
                    }

                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            DisplayItem item1 = null;
            //Check X range first since it is easy
            ScaledResolution current = ResolutionUtil.current();
            int xCenter = current.getScaledWidth() / 2;
            if (mouseX >= xCenter - 80 && mouseX <= xCenter + 80) {
                //now some super janky code
                int yPosition = 40;

                for (DisplayItem displayItem : element.getDisplayItems()) {
                    if (mouseY >= yPosition && mouseY <= yPosition + 20) {
                        item1 = displayItem;
                        break;
                    }
                    //Adjust for 3 pixel gap
                    yPosition += 23;
                }
            }
            for (GuiButton guiButton : super.buttonList) {
                if (guiButton.isMouseOver())
                    return;
            }
            this.modifying = item1;
            if (this.modifying != null) {
                ChromaHUDApi.getInstance().getTextConfigs(this.modifying.getType()).forEach((config) -> config.getLoad().accept(config.getTextField(), this.modifying));
                ChromaHUDApi.getInstance().getButtonConfigs(this.modifying.getType()).forEach((button) -> button.getLoad().accept(button.getButton(), this.modifying));
                ChromaHUDApi.getInstance().getStringConfigs(this.modifying.getType()).forEach((button) -> button.getLoad().accept(this.modifying));
            }


        }


    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (modifying != null) {
            List<ButtonConfig> configs = ChromaHUDApi.getInstance().getButtonConfigs(modifying.getType());
            if (configs != null && !configs.isEmpty()) {

                for (ButtonConfig config : configs) {
                    GuiButton button = config.getButton();
                    button.mouseReleased(mouseX, mouseY);
                }
            }

        }

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution current = ResolutionUtil.current();
        drawRect(0, 0, current.getScaledWidth(), current.getScaledHeight(), new Color(0, 0, 0, 150).getRGB());

        super.drawScreen(mouseX, mouseY, partialTicks);
        ElementRenderer.startDrawing(element);
        element.renderEditView();
        ElementRenderer.endDrawing(element);
        int xPosition = ResolutionUtil.current().getScaledWidth() / 2 - 80;
        int yPosition = 40;
        int width = 160;
        int height = 20;

        Color defaultColor = new Color(255, 255, 255, 100);
        Color otherColor = new Color(255, 255, 255, 150);

        for (DisplayItem displayItem : element.getDisplayItems()) {
            FontRenderer fontrenderer = mc.fontRendererObj;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            boolean hovered = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
            drawRect(xPosition, yPosition, xPosition + width, yPosition + height, this.modifying != null && this.modifying.getOrdinal() == displayItem.getOrdinal() || hovered ? otherColor.getRGB() : defaultColor.getRGB());
            int j = Color.RED.getRGB();
            String displayString = ChromaHUDApi.getInstance().getName(displayItem.getType());
            fontrenderer.drawString(displayString, (xPosition + width / 2 - fontrenderer.getStringWidth(displayString) / 2), yPosition + (height - 8) / 2, j, false);
            yPosition += 23;
        }
        if (modifying != null) {
            List<ButtonConfig> configs = ChromaHUDApi.getInstance().getButtonConfigs(modifying.getType());
            xPosition = 3;
            yPosition = 5 + 21 * 4;
            if (configs != null && !configs.isEmpty()) {
                for (ButtonConfig config : configs) {
                    GuiButton button = config.getButton();
                    button.xPosition = xPosition;
                    button.yPosition = yPosition;
                    button.drawButton(mc, mouseX, mouseY);
                    yPosition += 23;
                }
            }
            List<TextConfig> textConfigs = ChromaHUDApi.getInstance().getTextConfigs(modifying.getType());
            if (textConfigs != null && !textConfigs.isEmpty()) {
                for (TextConfig config : textConfigs) {
                    GuiTextField textField = config.getTextField();
                    textField.xPosition = xPosition;
                    textField.yPosition = yPosition;
                    textField.drawTextBox();
                    yPosition += 23;
                    config.getAction().accept(textField, modifying);
                }
            }
            int rightBound = (int) (ResolutionUtil.current().getScaledWidth_double() / 2 - 90);
            List<StringConfig> stringConfigs = ChromaHUDApi.getInstance().getStringConfigs(modifying.getType());
            if (stringConfigs != null && !stringConfigs.isEmpty()) {
                for (StringConfig config : stringConfigs) {
                    config.getDraw().accept(modifying);
                    String draw = config.getString();
                    List<String> lines = new ArrayList<>();
                    String[] split = draw.split(" ");
                    FontRenderer fontRendererObj = Minecraft.getMinecraft().fontRendererObj;
                    StringBuilder currentLine = new StringBuilder();
                    for (String s : split) {
                        if (!s.contains("\n")) {
                            if (fontRendererObj.getStringWidth(" " + currentLine.toString()) +
                                fontRendererObj.getStringWidth(s) + xPosition < rightBound - 10)
                                currentLine.append(" ").append(s);
                            else {
                                lines.add(currentLine.toString());
                                currentLine = new StringBuilder();
                                currentLine.append(s);
                            }
                        } else {
                            String[] split1 = s.split("\n");
                            Iterator<String> iterator = Arrays.asList(split1).iterator();

                            while (iterator.hasNext()) {
                                currentLine.append(" ").append(iterator.next());
                                if (iterator.hasNext()) {
                                    lines.add(currentLine.toString());
                                    currentLine = new StringBuilder();
                                }
                            }
                        }
                    }
                    lines.add(currentLine.toString());

                    yPosition += 10;
                    for (String string : lines) {
                        Minecraft.getMinecraft().fontRendererObj.drawString(string, xPosition, yPosition, Color.RED.getRGB());
                        yPosition += 10;
                    }
                }
            }
        }
    }
}
