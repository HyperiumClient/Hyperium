package com.hcc.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.HashMap;
import java.util.function.Consumer;

public class HCCGui extends GuiScreen {
    private int idIteration;
    private HashMap<GuiButton, Consumer<GuiButton>> clicks = new HashMap<>();
    private HashMap<GuiButton, Consumer<GuiButton>> updates = new HashMap<>();
    private HashMap<String, GuiButton> nameMap = new HashMap<>();

    public int nextId() {
        return (++idIteration);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        Consumer<GuiButton> guiButtonConsumer = clicks.get(button);
        if (guiButtonConsumer != null)
            guiButtonConsumer.accept(button);
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

    public void reg(String name, GuiButton button, Consumer<GuiButton> consumer, Consumer<GuiButton> tick) {
        this.buttonList.add(button);
        this.clicks.put(button, consumer);
        this.updates.put(button, tick);
        this.nameMap.put(name, button);
    }


}
