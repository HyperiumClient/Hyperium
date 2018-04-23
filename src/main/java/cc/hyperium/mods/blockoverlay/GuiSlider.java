package cc.hyperium.mods.blockoverlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

import java.text.DecimalFormat;

public class GuiSlider extends GuiButton
{
    private boolean isDragging;
    private double minValue;
    private double maxValue;
    private double sliderValue;
    private DecimalFormat decimalFormat;
    private String sliderPrefix;

    public GuiSlider(final int buttonId, final int x, final int y, final String sliderPrefix, final int minValue, final int maxValue, final int currentValue) {
        this(buttonId, x, y, sliderPrefix, minValue, maxValue, currentValue, "#");
    }

    public GuiSlider(final int buttonId, final int x, final int y, final String sliderPrefix, final float minValue, final float maxValue, final float currentValue) {
        this(buttonId, x, y, sliderPrefix, minValue, maxValue, currentValue, "#.00");
    }

    public GuiSlider(final int buttonId, final int x, final int y, final String sliderPrefix, final float minValue, final float maxValue, final float currentValue, final String pattern) {
        super(buttonId, x, y, sliderPrefix);
        this.isDragging = false;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.sliderValue = (currentValue - minValue) / (maxValue - minValue);
        this.decimalFormat = new DecimalFormat(pattern);
        this.sliderPrefix = sliderPrefix;
        super.displayString = this.sliderPrefix + this.decimalFormat.format(this.sliderValue * (this.maxValue - this.minValue) + this.minValue);
    }

    protected int getHoverState(final boolean mouseOver) {
        return 0;
    }

    protected void mouseDragged(final Minecraft mc, final int mouseX, final int mouseY) {
        if (super.visible) {
            if (this.isDragging) {
                this.sliderValue = (mouseX - (super.xPosition + 4)) / (super.width - 8);
                this.updateSlider();
            }
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.drawTexturedModalRect(super.xPosition + (int)(this.sliderValue * (super.width - 8)), super.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(super.xPosition + (int)(this.sliderValue * (super.width - 8)) + 4, super.yPosition, 196, 66, 4, 20);
        }
    }

    public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            this.sliderValue = (mouseX - (super.xPosition + 4)) / (super.width - 8);
            this.updateSlider();
            return this.isDragging = true;
        }
        return false;
    }

    public void mouseReleased(final int mouseX, final int mouseY) {
        this.isDragging = false;
    }

    public void updateSlider() {
        if (this.sliderValue < 0.0) {
            this.sliderValue = 0.0;
        }
        else if (this.sliderValue > 1.0) {
            this.sliderValue = 1.0;
        }
        super.displayString = this.sliderPrefix + this.decimalFormat.format(this.sliderValue * (this.maxValue - this.minValue) + this.minValue);
    }

    public float getSliderValue() {
        return (float)this.sliderValue;
    }
}
