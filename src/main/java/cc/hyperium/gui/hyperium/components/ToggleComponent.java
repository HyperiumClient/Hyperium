package cc.hyperium.gui.hyperium.components;

import cc.hyperium.gui.Icons;
import cc.hyperium.mixinsimp.client.GlStateModifier;
import cc.hyperium.utils.HyperiumFontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by Sk1er on today (It will be right for a little bit)
 */
public class ToggleComponent extends AbstractTabComponent {
    private final String label;
    private List<String> lines = new ArrayList<>();
    private Field field;
    private boolean state;
    private Object parentObj;

    public ToggleComponent(AbstractTab tab, List<String> tags, String label, Field field, Object parentObj) {
        super(tab, tags);
        this.label = label;
        this.field = field;
        this.parentObj = parentObj;
        this.state = getStateFromField();
    }

    private boolean getStateFromField() {
        try {
            return field.getBoolean(parentObj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setState(boolean newState) {
        try {
            field.setBoolean(parentObj, newState);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(int x, int y, int width, int mouseX, int mouseY) {
        HyperiumFontRenderer font = tab.gui.getFont();

        lines.clear();

        lines = font.splitString(label, (width + 25) / 2); //16 for icon, 3 for render offset and then some more

        GlStateManager.pushMatrix();
        if (hover)
            Gui.drawRect(x, y, x + width, y + 18 * lines.size(), 0xa0000000);
        GlStateManager.popMatrix();

        int line1 = 0;
        for (String line : lines) {
            font.drawString(line.replaceAll("_"," ").toUpperCase(), x + 3, y + 5 + 17 * line1, 0xffffff);
            line1++;
        }


        int farSide = x + width;
        float halfwayDown = y + 9 * lines.size();

        int toggleW = 26;
        float statX = farSide - toggleW - 5;
        GlStateModifier.INSTANCE.reset();
        if (state)
            Icons.TOGGLE_ON.bind();
        else Icons.TOGGLE_OFF.bind();
        Gui.drawScaledCustomSizeModalRect((int) statX, y + 3, 0, 0, 121, 54, toggleW, 13, 121, 54);


    }

    @Override
    public int getHeight() {
        return 18 * lines.size();

    }


    @Override
    public void onClick(int x, int y) {
        if (y < 18 * lines.size()) {
            setState(!state);
            this.state = getStateFromField(); //Call from reflection to ensure they never desync. Better have it fail and stay off than the user think its a diff tate
            stateChange(this.state);
        }


    }
}
