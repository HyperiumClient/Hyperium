package cc.hyperium.addons.customcrosshair.gui;

import cc.hyperium.addons.customcrosshair.crosshair.Crosshair;
import cc.hyperium.addons.customcrosshair.gui.items.Button;
import cc.hyperium.addons.customcrosshair.gui.items.EditColourButton;
import cc.hyperium.addons.customcrosshair.gui.items.GuiItem;
import cc.hyperium.addons.customcrosshair.gui.items.HelpButton;
import cc.hyperium.addons.customcrosshair.gui.items.Scrollbar;
import cc.hyperium.addons.customcrosshair.gui.items.Slider;
import cc.hyperium.addons.customcrosshair.gui.items.Tickbox;
import cc.hyperium.addons.customcrosshair.main.CustomCrosshairAddon;
import cc.hyperium.addons.customcrosshair.utils.GuiGraphics;
import cc.hyperium.addons.customcrosshair.utils.GuiTheme;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

public class GuiEditCrosshair extends ModGuiScreen
{
    private Crosshair crosshair;
    private Tickbox tickbox_enabled;
    private Tickbox tickbox_visibleDefault;
    private EditColourButton editColour_crosshair;
    private Tickbox tickbox_visibleHiddenGui;
    private Tickbox tickbox_visibleDebug;
    private Tickbox tickbox_visibleSpectator;
    private Tickbox tickbox_visibleThirdPerson;
    private Tickbox tickbox_outline;
    private EditColourButton editColour_outline;
    private Tickbox tickbox_dot;
    private EditColourButton editColour_dot;
    private Slider slider_crosshairType;
    private Slider slider_width;
    private Slider slider_height;
    private Slider slider_gap;
    private Slider slider_thickness;
    private Tickbox tickbox_dynamicBow;
    private Button button_resetCrosshair;
    private Button button_settings;
    private Scrollbar scrollbar;
    private int prevScrollbarPosition;
    private int itemOffset;
    private List<HelpButton> helpButtonList;

    public GuiEditCrosshair() {
        this.itemOffset = 0;
        this.helpButtonList = new ArrayList<HelpButton>();
    }

    public void initGui() {
        final int[] screenSize = GuiGraphics.getScreenSize();
        this.crosshair = CustomCrosshairAddon.getCrosshairMod().getCrosshair();
        this.itemList.clear();
        this.itemList.add(this.tickbox_enabled = new Tickbox(this, 0, "Enabled", 0, 0) {
            @Override
            public void mouseClicked(final int mouseX, final int mouseY) {
                super.mouseClicked(mouseX, mouseY);
                GuiEditCrosshair.this.crosshair.setEnabled(GuiEditCrosshair.this.tickbox_enabled.getChecked());
            }
        });
        this.tickbox_enabled.setChecked(this.crosshair.getEnabled());
        this.tickbox_enabled.getHelpText().add("Enables or disables the crosshair mod.");
        this.itemList.add(this.editColour_crosshair = new EditColourButton(this, 6, "Crosshair Colour", 0, 0, 100, 20, this.crosshair.getColour()));
        this.editColour_crosshair.getHelpText().add("Changes the main colour of the crosshair.");
        this.itemList.add(this.slider_crosshairType = new Slider(this, 5, "Crosshair Type", 0, 0, 120, 10, 0, 3));
        this.slider_crosshairType.setValue(this.crosshair.getCrosshairTypeID());
        this.slider_crosshairType.getHelpText().add("Changes the crosshair type.");
        this.slider_crosshairType.getHelpText().add("[0 = Cross]");
        this.slider_crosshairType.getHelpText().add("[1 = Circle]");
        this.slider_crosshairType.getHelpText().add("[2 = Square]");
        this.slider_crosshairType.getHelpText().add("[3 = Arrow]");
        this.itemList.add(this.tickbox_visibleDefault = new Tickbox(this, 18, "Crosshair Visible", 0, 0) {
            @Override
            public void mouseClicked(final int mouseX, final int mouseY) {
                super.mouseClicked(mouseX, mouseY);
                CustomCrosshairAddon.getCrosshairMod().getCrosshair().setVisibleDefault(GuiEditCrosshair.this.tickbox_visibleDefault.getChecked());
            }
        });
        this.tickbox_visibleDefault.setChecked(this.crosshair.getVisibleDefault());
        this.tickbox_visibleDefault.getHelpText().add("Shows or hides the crosshair.");
        this.itemList.add(this.tickbox_visibleHiddenGui = new Tickbox(this, 19, "Visible in Hide Gui", 0, 0) {
            @Override
            public void mouseClicked(final int mouseX, final int mouseY) {
                super.mouseClicked(mouseX, mouseY);
                GuiEditCrosshair.this.crosshair.setVisibleHiddenGui(GuiEditCrosshair.this.tickbox_visibleHiddenGui.getChecked());
            }
        });
        this.tickbox_visibleHiddenGui.setChecked(this.crosshair.getVisibleHiddenGui());
        this.tickbox_visibleHiddenGui.getHelpText().add("Shows or hides the crosshair when the HUD (F1 Mode) is off.");
        this.itemList.add(this.tickbox_visibleDebug = new Tickbox(this, 1, "Visible in debug screen", 0, 0) {
            @Override
            public void mouseClicked(final int mouseX, final int mouseY) {
                super.mouseClicked(mouseX, mouseY);
                GuiEditCrosshair.this.crosshair.setVisibleDebug(GuiEditCrosshair.this.tickbox_visibleDebug.getChecked());
            }
        });
        this.tickbox_visibleDebug.setChecked(this.crosshair.getVisibleDebug());
        this.tickbox_visibleDebug.getHelpText().add("Shows or hides the crosshair in the debug screen (F3 Mode).");
        this.itemList.add(this.tickbox_visibleSpectator = new Tickbox(this, 2, "Visible in spectator mode", 0, 0) {
            @Override
            public void mouseClicked(final int mouseX, final int mouseY) {
                super.mouseClicked(mouseX, mouseY);
                GuiEditCrosshair.this.crosshair.setVisibleSpectator(GuiEditCrosshair.this.tickbox_visibleSpectator.getChecked());
            }
        });
        this.tickbox_visibleSpectator.setChecked(this.crosshair.getVisibleSpectator());
        this.tickbox_visibleSpectator.getHelpText().add("Shows or hides the crosshair when in spectator mode.");
        this.itemList.add(this.tickbox_visibleThirdPerson = new Tickbox(this, 2, "Visible in third person mode", 0, 0) {
            @Override
            public void mouseClicked(final int mouseX, final int mouseY) {
                super.mouseClicked(mouseX, mouseY);
                GuiEditCrosshair.this.crosshair.setVisibleThirdPerson(GuiEditCrosshair.this.tickbox_visibleThirdPerson.getChecked());
            }
        });
        this.tickbox_visibleThirdPerson.setChecked(this.crosshair.getVisibleThirdPerson());
        this.tickbox_visibleThirdPerson.getHelpText().add("Shows or hides the crosshair when in third person mode.");
        this.itemList.add(this.tickbox_outline = new Tickbox(this, 3, "Outline", 0, 0) {
            @Override
            public void mouseClicked(final int mouseX, final int mouseY) {
                super.mouseClicked(mouseX, mouseY);
                GuiEditCrosshair.this.crosshair.setOutline(GuiEditCrosshair.this.tickbox_outline.getChecked());
            }
        });
        this.tickbox_outline.setChecked(this.crosshair.getOutline());
        this.tickbox_outline.getHelpText().add("Draws a black outline around the crosshair.");
        this.itemList.add(this.editColour_outline = new EditColourButton(this, 7, "Outline Colour", 0, 0, 100, 20, this.crosshair.getOutlineColour()));
        this.editColour_outline.getHelpText().add("Changes the outline colour of the crosshair.");
        this.itemList.add(this.tickbox_dot = new Tickbox(this, 4, "Dot", 0, 0) {
            @Override
            public void mouseClicked(final int mouseX, final int mouseY) {
                super.mouseClicked(mouseX, mouseY);
                GuiEditCrosshair.this.crosshair.setDot(GuiEditCrosshair.this.tickbox_dot.getChecked());
            }
        });
        this.tickbox_dot.setChecked(this.crosshair.getDot());
        this.tickbox_dot.getHelpText().add("Draws a white dot in the centre of the screen.");
        this.itemList.add(this.editColour_dot = new EditColourButton(this, 7, "Dot Colour", 0, 0, 100, 20, this.crosshair.getDotColour()));
        this.editColour_dot.getHelpText().add("Changes the dot colour of the crosshair.");
        this.itemList.add(this.slider_width = new Slider(this, 10, "Width", 0, 0, 150, 10, 1, 50));
        this.slider_width.setValue(this.crosshair.getWidth());
        this.slider_width.getHelpText().add("Changes the horizontal width of the crosshair.");
        this.itemList.add(this.slider_height = new Slider(this, 11, "Height", 0, 0, 150, 10, 1, 50));
        this.slider_height.setValue(this.crosshair.getHeight());
        this.slider_height.getHelpText().add("Changes the vertical height of the crosshair.");
        this.itemList.add(this.slider_gap = new Slider(this, 12, "Gap", 0, 0, 150, 10, 0, 50));
        this.slider_gap.setValue(this.crosshair.getGap());
        this.slider_gap.getHelpText().add("Changes the gap/radius at the centre of the crosshair.");
        this.itemList.add(this.slider_thickness = new Slider(this, 13, "Thickness", 0, 0, 150, 10, 1, 10));
        this.slider_thickness.setValue(this.crosshair.getThickness());
        this.slider_thickness.getHelpText().add("Changes the thickness of the crosshair.");
        this.itemList.add(this.tickbox_dynamicBow = new Tickbox(this, 14, "Dynamic Crosshair (Bow)", 0, 0) {
            @Override
            public void mouseClicked(final int mouseX, final int mouseY) {
                super.mouseClicked(mouseX, mouseY);
                GuiEditCrosshair.this.crosshair.setDynamicBow(GuiEditCrosshair.this.tickbox_dynamicBow.getChecked());
            }
        });
        this.tickbox_dynamicBow.setChecked(this.crosshair.getDynamicBow());
        this.tickbox_dynamicBow.getHelpText().add("When using a bow, indicates the duration of the pull animation.");
        int y = 32;
        for (int i = 0; i < this.itemList.size(); ++i) {
            if (i > 0) {
                y += this.itemList.get(i - 1).getHeight() + 6;
            }
            this.itemList.get(i).setPosition(21, y - this.itemOffset);
        }
        y = 32;
        this.helpButtonList.clear();
        for (int i = 0; i < this.itemList.size(); ++i) {
            this.helpButtonList.add(new HelpButton(this, this.itemList.get(i).getHelpText()));
            if (i > 0) {
                y += this.itemList.get(i - 1).getHeight() + 6;
            }
            this.helpButtonList.get(i).setPosition(5, y - this.itemOffset);
        }
        this.button_resetCrosshair = new Button(this, 20, "Reset", this.width - 101, 0, 50, 25) {
            @Override
            public void mouseClicked(final int mouseX, final int mouseY) {
                super.mouseClicked(mouseX, mouseY);
                CustomCrosshairAddon.getCrosshairMod().resetCrosshair();
                GuiEditCrosshair.this.initGui();
            }
        };
        this.button_settings = new Button(this, 21, "Settings", this.width - 51, 0, 50, 25) {
            @Override
            public void mouseClicked(final int mouseX, final int mouseY) {
                super.mouseClicked(mouseX, mouseY);
                Minecraft.getMinecraft().displayGuiScreen((GuiScreen)new GuiSettings());
            }
        };
        this.scrollbar = new Scrollbar(this, 100, screenSize[0] - 11, 25, 10, screenSize[1] - 26, y - 9);
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.toolTip = null;
        for (int i = 0; i < this.itemList.size(); ++i) {
            final GuiItem item = this.itemList.get(i);
            item.drawItem(mouseX, mouseY);
            switch (item.getActionID()) {
                case 5: {
                    this.crosshair.setCrosshairType(this.slider_crosshairType.getValue());
                    break;
                }
                case 10: {
                    this.crosshair.setWidth(this.slider_width.getValue());
                    break;
                }
                case 11: {
                    this.crosshair.setHeight(this.slider_height.getValue());
                    break;
                }
                case 12: {
                    this.crosshair.setGap(this.slider_gap.getValue());
                    break;
                }
                case 13: {
                    this.crosshair.setThickness(this.slider_thickness.getValue());
                    break;
                }
            }
        }
        for (int i = 0; i < this.helpButtonList.size(); ++i) {
            this.helpButtonList.get(i).drawItem(mouseX, mouseY);
        }
        final int[] screenSize = GuiGraphics.getScreenSize();
        final String titleText = "Custom Crosshair Addon" + " v" + CustomCrosshairAddon.VERSION;
        GuiGraphics.drawBorderedRectangle(0, 0, screenSize[0] - 1, 25, GuiTheme.PRIMARY, GuiTheme.SECONDARY);
        GuiGraphics.drawStringWithShadow(titleText, 5, 10, 16777215);
        this.button_resetCrosshair.drawItem(mouseX, mouseY);
        this.button_settings.drawItem(mouseX, mouseY);
        this.scrollbar.drawItem(mouseX, mouseY);
        if (this.prevScrollbarPosition != this.scrollbar.getValue()) {
            this.itemOffset = this.scrollbar.getValue();
            int y = 32;
            for (int j = 0; j < this.itemList.size(); ++j) {
                final GuiItem item2 = this.itemList.get(j);
                if (item2 != this.button_resetCrosshair && item2 != this.button_settings && !(item2 instanceof HelpButton)) {
                    if (j > 0) {
                        y += this.itemList.get(j - 1).getHeight() + 6;
                    }
                    item2.setPosition(21, y - this.itemOffset);
                }
            }
            y = 32;
            for (int j = 0; j < this.itemList.size(); ++j) {
                if (j > 0) {
                    y += this.itemList.get(j - 1).getHeight() + 6;
                }
                this.helpButtonList.get(j).setPosition(5, y - this.itemOffset);
            }
        }
        this.prevScrollbarPosition = this.scrollbar.getValue();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void updateScreen() {
        final int wheel = Mouse.getDWheel();
        final int increment = (int)Math.ceil(this.scrollbar.getMaxValue() / 10);
        if (wheel > 0) {
            this.scrollbar.setValue(this.scrollbar.getValue() - increment);
        }
        if (wheel < 0) {
            this.scrollbar.setValue(this.scrollbar.getValue() + increment);
        }
        super.updateScreen();
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        for (int i = 0; i < this.itemList.size(); ++i) {
            final GuiItem item = this.itemList.get(i);
            if (mouseX >= item.getPosX() && mouseX <= item.getPosX() + item.getWidth() && mouseY >= item.getPosY() && mouseY <= item.getPosY() + item.getHeight()) {
                item.mouseClicked(mouseX, mouseY);
            }
        }
        if (mouseX >= this.button_resetCrosshair.getPosX() && mouseX <= this.button_resetCrosshair.getPosX() + this.button_resetCrosshair.getWidth() && mouseY >= this.button_resetCrosshair.getPosY() && mouseY <= this.button_resetCrosshair.getPosY() + this.button_resetCrosshair.getHeight()) {
            this.button_resetCrosshair.mouseClicked(mouseX, mouseY);
        }
        if (mouseX >= this.button_settings.getPosX() && mouseX <= this.button_settings.getPosX() + this.button_settings.getWidth() && mouseY >= this.button_settings.getPosY() && mouseY <= this.button_settings.getPosY() + this.button_settings.getHeight()) {
            this.button_settings.mouseClicked(mouseX, mouseY);
        }
        this.scrollbar.mouseClicked(mouseX, mouseY);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        for (int i = 0; i < this.itemList.size(); ++i) {
            final GuiItem item = this.itemList.get(i);
            item.mouseReleased(mouseX, mouseY);
        }
        this.scrollbar.mouseReleased(mouseX, mouseY);
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        switch (keyCode) {
            case 200: {
                this.scrollbar.setValue(this.scrollbar.getValue() - 5);
                break;
            }
            case 208: {
                this.scrollbar.setValue(this.scrollbar.getValue() + 5);
                break;
            }
        }
        super.keyTyped(typedChar, keyCode);
    }
}

