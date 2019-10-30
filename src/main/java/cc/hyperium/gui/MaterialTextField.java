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

package cc.hyperium.gui;

import cc.hyperium.utils.HyperiumFontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ChatAllowedCharacters;

/*
 * Created by Cubxity on 01/10/2018
 */
public class MaterialTextField {
    private int x, y, width, height;
    private HyperiumFontRenderer fr;
    private String text = "", hint;
    private boolean focused;
    private int blink;

    /**
     * @param x      top left x position
     * @param y      top left y position
     * @param width  width of text field
     * @param height height of text field
     * @param hint   hint to display when the text is empty
     * @param fr     font renderer
     */
    public MaterialTextField(int x, int y, int width, int height, String hint, HyperiumFontRenderer fr) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hint = hint;
        this.fr = fr;
    }

    /**
     * call this method to render the text field
     *
     * @param mx mouse x position
     * @param my mouse y position
     */
    public void render(int mx, int my) {
        boolean hover = mx > x && my > y && mx < x + width && my < y + height;
        Gui.drawRect(x, y + height - 1, x + width, y + height, hover || focused ? 0xffffffff : 0xff969696);
        boolean em = text.isEmpty();
        fr.drawString(em && !focused ? hint : text, x + 2, y + height / 2f - fr.FONT_HEIGHT / 2f, em ? 0xff969696 : 0xffffffff);
        int x = (int) (this.x + 3 + fr.getWidth(text));
        if (focused && blink >= 10) {
            Gui.drawRect(x, y + 4, x + 1, y + height - 4, 0xffffffff);
            if (blink >= 20) blink = -1;
        }
    }

    /**
     * Called on tick
     */
    public void update() {
        blink++;
        if (blink >= 20) blink = -1;
    }

    /**
     * call this method when user clicked anywhere on the screen
     *
     * @param x  mouse x position
     * @param y  mouse y position
     * @param mb mouse button
     */
    public void onClick(int x, int y, int mb) {
        if (mb == 0) focused = x > this.x && y > this.y && x < this.x + width && y < this.y + height;
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (focused) {
            if (keyCode == 28) {
                focused = false;
            } else if (keyCode == 14) {
                if (!text.isEmpty()) {
                    text = text.substring(0, text.length() - 1);
                }
            } else if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                text += typedChar;
            }
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
