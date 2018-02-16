package com.hcc.gui;

public class GuiBoxItem<E> {
    private GuiBlock box;
    private E object;

    public GuiBoxItem(GuiBlock box, E object) {
        this.box = box;
        this.object = object;
    }

    @Override
    public String toString() {
        return "GuiBoxItem{" +
                "box=" + box +
                ", object=" + object +
                '}';
    }

    public GuiBlock getBox() {
        return box;
    }

    public E getObject() {
        return object;
    }
}
