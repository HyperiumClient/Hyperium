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

package cc.hyperium.gui.hyperium.components;

import cc.hyperium.Hyperium;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.utils.GlStateModifier;
import cc.hyperium.utils.HyperiumFontRenderer;
import cc.hyperium.utils.RenderUtils;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

/*
 * Created by Sk1er on today (It will be right for a little bit)
 */
public class ToggleComponent extends AbstractTabComponent {

    private final String label;
    private List<String> lines = new ArrayList<>();
    private Field field;
    private boolean state;
    private Object parentObj;
    private double animation = 0.5;
    private long lastDeltaTime = System.currentTimeMillis();

    public ToggleComponent(AbstractTab tab, List<String> tags, String label, Field field, Object parentObj) {
        super(tab, tags);
        tag(label);
        this.label = label;
        this.field = field;
        this.parentObj = parentObj;
        state = getStateFromField();
    }

    private boolean getStateFromField() {
        if (field == null) Hyperium.LOGGER.error(label);
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
        if (hover) Gui.drawRect(x, y, x + width, y + 18 * lines.size(), 0xa0000000);
        GlStateManager.popMatrix();

        int line1 = 0;
        for (String line : lines) {
            font.drawString(line.replaceAll("_", " ").toUpperCase(), x + 3, y + 5 + 17 * line1, 0xffffff);
            line1++;
        }

        int farSide = x + width;

        int toggleW = 26;
        float statX = farSide - toggleW - 5;
        GlStateModifier.INSTANCE.reset();
        double animationInc = (System.currentTimeMillis() - lastDeltaTime) / 400f;

        animation = HyperiumGui.clamp(
            HyperiumGui.easeOut(
                (float) animation,
                    state ? 1.0f : 0.0f,
                (float) animationInc,
                5
            ),
            0.0f,
            1.0f
        );

        Color FAR = new Color(76, 175, 80);
        Color CLOSE = new Color(200, 200, 200);

        int red = (int) Math.abs((animation * FAR.getRed()) + ((1 - animation) * CLOSE.getRed()));
        int green = (int) Math.abs((animation * FAR.getGreen()) + ((1 - animation) * CLOSE.getGreen()));
        int blue = (int) Math.abs((animation * FAR.getBlue()) + ((1 - animation) * CLOSE.getBlue()));

        RenderUtils.drawSmoothRect((int) statX, y + 7, (int) (statX + 20), y + 10, 1, Color.WHITE.getRGB());
        RenderUtils.drawFilledCircle((int) ((int) statX + 20D * animation), y + 8, 5, new Color(red, green, blue).getRGB());
        lastDeltaTime = System.currentTimeMillis();
    }

    @Override
    public int getHeight() {
        return 18 * lines.size();
    }


    @Override
    public void onClick(int x, int y) {
        if (y < 18 * lines.size()) {
            setState(!state);
            state = getStateFromField(); //Call from reflection to ensure they never desync. Better have it fail and stay off than the user think its a diff tate
            stateChange(state);
        }
    }

    public String getLabel() {
        return label;
    }
}
