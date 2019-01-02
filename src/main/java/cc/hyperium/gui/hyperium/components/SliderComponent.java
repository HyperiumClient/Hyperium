package cc.hyperium.gui.hyperium.components;

import cc.hyperium.utils.HyperiumFontRenderer;
import cc.hyperium.utils.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by Sk1er on today (It will be right for a little bit)
 */
public class SliderComponent extends AbstractTabComponent {
    private final String label;
    private List<String> lines = new ArrayList<>();
    private Field field;
    private Object parentObj;
    private float minVal;
    private float maxVal;
    private boolean isInteger;
    private boolean round;
    private double currentValue;
    private SliderComponent sliderComponent;
    private int width;
    private int x;
    private int y;
    private boolean wasDown = false;

    public SliderComponent(AbstractTab tab, List<String> tags, String label, Field field, Object parentObj, float minVal, float maxVal, boolean isInteger, boolean round) {
        super(tab, tags);
        tag(label);
        this.label = label;
        this.field = field;
        this.parentObj = parentObj;
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.isInteger = isInteger;
        this.round = round;
        if (!field.isAccessible())
            field.setAccessible(true);
    }

    public String getLabel() {
        return label;
    }

    private double getDouble() {
        try {
            return field.getDouble(parentObj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return minVal;
    }

    public void setDouble(double val) {
        try {
            field.setDouble(parentObj, val);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public int getInt() {
        try {
            return field.getInt(parentObj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (int) maxVal;
    }

    public void setInt(int val) {
        try {
            field.setInt(parentObj, val);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(int x, int y, int width, int mouseX, int mouseY) {
        HyperiumFontRenderer font = tab.gui.getFont();
        this.x = x;
        this.y = y;
        lines.clear();
        lines = font.splitString(label, (width) / 4); //16 for icon, 3 for render offset and then some more

        if (currentValue < minVal) {
            currentValue = minVal;
        } else if (currentValue > maxVal) {
            currentValue = maxVal;
        }

        GlStateManager.pushMatrix();
        if (hover)
            Gui.drawRect(x, y, x + width, y + 18 * lines.size(), 0xa0000000);
        GlStateManager.popMatrix();

        int line1 = 0;
        for (String line : lines) {
            font.drawString(line.replaceAll("_", " ").toUpperCase(), x + 3, y + 5 + 17 * line1, 0xffffff);
            line1++;
        }
        int left = x + width / 2;
        String s = isInteger ? Integer.toString((int) currentValue) : Double.toString(round ? Math.round(currentValue) : currentValue);

        int rightSide = x + width - 5;

        RenderUtils.drawLine(left, y + 9, rightSide, y + 9, 2f, Color.WHITE.getRGB());
        font.drawString(s, left - 8 - font.getWidth(s), y + 4, Color.WHITE.getRGB());
        double d = (currentValue - minVal) / (maxVal - minVal) * width / 2;
        int toInt = (int) (left + d);
        this.width = width;
        RenderUtils.drawFilledCircle(toInt, y + 9, 5f, Color.WHITE.getRGB());
        if (!Mouse.isButtonDown(0) && wasDown) {
            wasDown = false;
            super.stateChange(this.currentValue);
        }
    }

    @Override
    public int getHeight() {
        return 18 * lines.size();
    }

    @Override
    public void onClick(int x, int y) {
        //we don't care about clicks. Its all about those drags
    }

    @Override
    public void mouseEvent(int mouseX, int mouseY) {
        if (Mouse.isButtonDown(0)) {
            wasDown = true;
            int left = width / 2;
            int rightSide = width - 5;

            mouseX -= left;
            rightSide -= left;
            double percent = (double) mouseX / (double) rightSide;
            if (percent < 0)
                percent = 0;
            if (percent > 1.0)
                percent = 1.0;
            this.currentValue = minVal + percent * (double) (maxVal - minVal);
            if (isInteger) {
                setInt((int) currentValue);
            } else setDouble(currentValue);
        }

    }
}
