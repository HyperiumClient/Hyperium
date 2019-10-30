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

import cc.hyperium.gui.ScissorState;
import cc.hyperium.gui.hyperium.HyperiumMainGui;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.utils.SimpleAnimValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * Created by Cubxity on 27/08/2018
 */
public abstract class AbstractTab {

    protected List<AbstractTabComponent> components = new ArrayList<>();
    protected Map<AbstractTabComponent, Boolean> clickStates = new HashMap<>();
    protected HyperiumMainGui gui;
    protected String title;
    private SimpleAnimValue scrollAnim = new SimpleAnimValue(0L, 0f, 0f);
    private int scroll;
    private String filter;

    /**
     * Default Constructor
     *
     * @param gui   - Given parent GUI
     * @param title - Given tab title
     */
    public AbstractTab(HyperiumMainGui gui, String title) {
        this.gui = gui;
        this.title = title;
    }

    /**
     * Render - Renders the Tab
     *
     * @param x      - Given X Position
     * @param y      - Given Y Position
     * @param width  - Given Width
     * @param height - Given Height
     */
    public void render(int x, int y, int width, int height) {

        ScaledResolution sr = ResolutionUtil.current();
        int sw = sr.getScaledWidth();
        int sh = sr.getScaledHeight();
        int xg = width / 9;   // X grid

        /* Begin new scissor state */
        ScissorState.scissor(x, y, width, height, true);

        /* Get mouse X and Y */
        final int mx = Mouse.getX() * sw / Minecraft.getMinecraft().displayWidth;           // Mouse X
        final int my = sh - Mouse.getY() * sh / Minecraft.getMinecraft().displayHeight - 1; // Mouse Y

        if (scrollAnim.getValue() != scroll * 18 && scrollAnim.isFinished()) {
            scrollAnim = new SimpleAnimValue(1000L, scrollAnim.getValue(), scroll * 18);
        }

        y += scrollAnim.getValue();
        /* Render each tab component */
        for (AbstractTabComponent comp : filter == null ? components : components.stream().filter(c -> c.filter(filter)).collect(Collectors.toList())) {
            comp.render(x, y, width, mx, my);

            /* If mouse is over component, set as hovered */
            if (mx >= x && mx <= x + width && my > y && my <= y + comp.getHeight()) {
                comp.hover = true;
                //For slider
                comp.mouseEvent(mx - xg, my - y /* Make the Y relevant to the component */);
                if (Mouse.isButtonDown(0)) {
                    if (!clickStates.computeIfAbsent(comp, ignored -> false)) {
                        comp.onClick(mx, my - y /* Make the Y relevant to the component */);
                        clickStates.put(comp, true);
                    }
                } else if (clickStates.computeIfAbsent(comp, ignored -> false))
                    clickStates.put(comp, false);
            } else {
                comp.hover = false;
            }

            y += comp.getHeight();
        }

        /* End scissor state */
        ScissorState.endScissor();
    }

    /**
     * Get Title - Get the Title of the tab
     *
     * @return - Given Title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get Filter - Get the tab filter value
     *
     * @return - Given tab filter
     */
    public String getFilter() {
        return filter;
    }

    /**
     * Handle Mouse Input - Handle mouse events/inputs
     */
    public void handleMouseInput() {
        if (Mouse.getEventDWheel() > 0) scroll++;
        else if (Mouse.getEventDWheel() < 0) scroll--;
        if (scroll > 0) scroll = 0;
    }

    /**
     * Set Title - Set the Title of the tab
     *
     * @param givenTitle - Given Title Value
     */
    public void setTitle(String givenTitle) {
        title = givenTitle;
    }

    /**
     * Set Filter - Set the tab filter
     *
     * @param filter - Given Filter Value
     */
    public void setFilter(String filter) {
        this.filter = filter;
    }
}
