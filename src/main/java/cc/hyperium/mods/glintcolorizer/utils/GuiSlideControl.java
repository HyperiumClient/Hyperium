package cc.hyperium.mods.glintcolorizer.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;

public class GuiSlideControl extends GuiButton {
    private static DecimalFormat numFormat = new DecimalFormat("#.00");
    public String label;
    private float curValue;
    private float minValue;
    private float maxValue;
    private boolean isSliding;
    private boolean useIntegers;

    public GuiSlideControl(int id, int x, int y, int width, int height, String displayString, float minVal, float maxVal, float curVal, boolean useInts) {
        super(id, x, y, width, height, useInts ? (displayString + (int) curVal) : (displayString + GuiSlideControl.numFormat.format(curVal)));
        this.label = displayString;
        this.minValue = minVal;
        this.maxValue = maxVal;
        this.curValue = (curVal - minVal) / (maxVal - minVal);
        this.useIntegers = useInts;
    }

    private float getValueAsFloat() {
        return (this.maxValue - this.minValue) * this.curValue + this.minValue;
    }

    public int getValueAsInt() {
        return (int) ((this.maxValue - this.minValue) * this.curValue + this.minValue);
    }

    public String getLabel() {
        if (this.useIntegers) {
            return this.label + this.getValueAsInt();
        }
        return this.label + GuiSlideControl.numFormat.format(this.getValueAsFloat());
    }

    private void setLabel() {
        this.displayString = this.getLabel();
    }

    public int getHoverState(boolean isMouseOver) {
        return 0;
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mousePosX, int mousePosY) {
        if (this.visible) {
            if (this.isSliding) {
                this.curValue = (mousePosX - (this.xPosition + 4)) / (this.width - 8);
                if (this.curValue < 0.0f) {
                    this.curValue = 0.0f;
                }
                if (this.curValue > 1.0f) {
                    this.curValue = 1.0f;
                }
                this.setLabel();
            }
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            this.drawTexturedModalRect(this.xPosition + (int) (this.curValue * (this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int) (this.curValue * (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mousePosX, int mousePosY) {
        if (!super.mousePressed(mc, mousePosX, mousePosY)) {
            return false;
        }
        this.curValue = (mousePosX - (this.xPosition + 4)) / (this.width - 8);
        if (this.curValue < 0.0f) {
            this.curValue = 0.0f;
        }
        if (this.curValue > 1.0f) {
            this.curValue = 1.0f;
        }
        this.setLabel();
        if (this.isSliding) {
            return this.isSliding = false;
        }
        return this.isSliding = true;
    }

    public void mouseReleased(int mousePosX, int mousePosY) {
        this.isSliding = false;
    }

}
