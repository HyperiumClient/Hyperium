package com.hcc.chromahud.api;

import net.minecraft.client.gui.GuiTextField;

import java.util.function.BiConsumer;

/**
 * Created by mitchellkatz on 1/8/18. Designed for production use on Sk1er.club
 */
public class TextConfig {
    /*
        Action is called when the text field is drawn. You cancel actions, please modify the given GuiTextField class.
        Load is called on load to initialize  to right state
     */
    private BiConsumer<GuiTextField, DisplayItem> action;
    private GuiTextField button;
    private BiConsumer<GuiTextField, DisplayItem> load;

    public TextConfig(BiConsumer<GuiTextField, DisplayItem> action, GuiTextField button, BiConsumer<GuiTextField, DisplayItem> load) {
        this.action = action;
        this.button = button;
        this.load = load;
    }

    public BiConsumer<GuiTextField, DisplayItem> getAction() {
        return action;
    }

    public GuiTextField getTextField() {
        return button;
    }


    public BiConsumer<GuiTextField, DisplayItem> getLoad() {
        return load;
    }
}
