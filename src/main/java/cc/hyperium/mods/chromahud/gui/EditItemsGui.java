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
import cc.hyperium.mods.chromahud.ChromaHUDApi;
import cc.hyperium.mods.chromahud.DisplayElement;
import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.ButtonConfig;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.mods.chromahud.api.StringConfig;
import cc.hyperium.mods.chromahud.api.TextConfig;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;

public class EditItemsGui extends GuiScreen {
    private final DisplayElement element;
    private final Map<GuiButton, Consumer<GuiButton>> clicks = new HashMap<>();
    private final Map<GuiButton, Consumer<GuiButton>> updates = new HashMap<>();
    private final ChromaHUD mod;
    private DisplayItem modifying;
    private int tmpId;

    EditItemsGui(DisplayElement element, ChromaHUD mod) {
        this.element = element;
        this.mod = mod;
    }

    private int nextId() {
        return (++tmpId);
    }

    @Override
    public void initGui() {
        reg(new GuiButton(nextId(), 2, 2, 100, 20, "Add Items"), button ->
            mc.displayGuiScreen(new AddItemsGui(mod, element)));

        reg(new GuiButton(nextId(), 2, 23, 100, 20, "Remove Item"), button -> {
            if (modifying != null) {
                element.removeDisplayItem(modifying.getOrdinal());

                if (modifying.getOrdinal() >= element.getDisplayItems().size()) {
                    modifying = null;
                }
            }
        });

        reg(new GuiButton(nextId(), 2, 23 + 21, 100, 20, "Move Up"), button -> {
            if (modifying != null) {
                if (modifying.getOrdinal() > 0) {
                    Collections.swap(element.getDisplayItems(), modifying.getOrdinal(), modifying.getOrdinal() - 1);
                    element.adjustOrdinal();
                }
            }
        });

        reg(new GuiButton(nextId(), 2, 23 + 21 * 2, 100, 20, "Move Down"), button -> {
            if (modifying != null) {
                if (modifying.getOrdinal() < element.getDisplayItems().size() - 1) {
                    Collections.swap(element.getDisplayItems(), modifying.getOrdinal(), modifying.getOrdinal() + 1);
                    element.adjustOrdinal();
                }
            }
        });

        reg(new GuiButton(nextId(), 2, ResolutionUtil.current().getScaledHeight() - 22, 100, 20, "Back"), button ->
            mc.displayGuiScreen(new DisplayElementConfig(element, mod)));

    }

    @Override
    protected void actionPerformed(GuiButton button) {
        Consumer<GuiButton> guiButtonConsumer = clicks.get(button);
        if (guiButtonConsumer != null) guiButtonConsumer.accept(button);
    }

    private void reg(GuiButton button, Consumer<GuiButton> consumer) {
        buttonList.removeIf(button1 -> button1.id == button.id);
        clicks.keySet().removeIf(button1 -> button1.id == button.id);
        buttonList.add(button);
        if (consumer != null) clicks.put(button, consumer);
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
    public void onGuiClosed() {
        mod.saveState();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (modifying == null) return;
        List<TextConfig> textConfigs = ChromaHUDApi.getInstance().getTextConfigs(modifying.getType());
        if (textConfigs != null && !textConfigs.isEmpty()) {
            textConfigs.stream().map(TextConfig::getTextField).forEach(textField -> textField.textboxKeyTyped(typedChar, keyCode));
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
                if (guiButton.isMouseOver()) return;
            }

            modifying = item1;
            if (modifying != null) {
                ChromaHUDApi.getInstance().getTextConfigs(modifying.getType()).forEach((config) -> config.getLoad().accept(config.getTextField(), modifying));
                ChromaHUDApi.getInstance().getButtonConfigs(modifying.getType()).forEach((button) -> button.getLoad().accept(button.getButton(), modifying));
                ChromaHUDApi.getInstance().getStringConfigs(modifying.getType()).forEach((button) -> button.getLoad().accept(modifying));
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (modifying != null) {
            List<ButtonConfig> configs = ChromaHUDApi.getInstance().getButtonConfigs(modifying.getType());
            if (configs != null && !configs.isEmpty()) {
                configs.stream().map(ButtonConfig::getButton).forEach(button -> button.mouseReleased(mouseX, mouseY));
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
            drawRect(xPosition, yPosition, xPosition + width, yPosition + height, modifying != null && modifying.getOrdinal() == displayItem.getOrdinal() ||
                hovered ? otherColor.getRGB() : defaultColor.getRGB());
            int j = Color.RED.getRGB();
            String displayString = ChromaHUDApi.getInstance().getName(displayItem.getType());
            fontrenderer.drawString(displayString, (xPosition + (width >> 1) - (fontrenderer.getStringWidth(displayString) >> 1)), yPosition + ((height - 8) >> 1), j,
                false);
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
