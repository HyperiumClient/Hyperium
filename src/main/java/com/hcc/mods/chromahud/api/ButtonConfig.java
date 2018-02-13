package com.hcc.mods.chromahud.api;

import net.minecraft.client.gui.GuiButton;

import java.util.function.BiConsumer;

/**
 * Created by mitchellkatz on 1/8/18. Designed for production use on Sk1er.club
 */
public class ButtonConfig {
    /*
        Called when the button is pressed. GuiButton is the gui button instance provided. DisplayItem is the object of that display item
        Load is called on load to initalize to right state
     */
    private BiConsumer<GuiButton, DisplayItem> action;
    private GuiButton button;
    private BiConsumer<GuiButton, DisplayItem> load;

    public ButtonConfig(BiConsumer<GuiButton, DisplayItem> action, GuiButton button, BiConsumer<GuiButton, DisplayItem> load) {
        this.action = action;
        this.button = button;
        this.load = load;
    }

    public BiConsumer<GuiButton, DisplayItem> getAction() {
        return action;
    }

    public GuiButton getButton() {
        return button;
    }

    public BiConsumer<GuiButton, DisplayItem> getLoad() {
        return load;
    }
}
