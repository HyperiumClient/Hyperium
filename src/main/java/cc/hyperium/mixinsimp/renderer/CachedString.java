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

package cc.hyperium.mixinsimp.renderer;


public class CachedString {
    private String text;
    private int listId;
    private float width;
    private float height;
    private float lastRed;
    private float lastBlue;
    private float lastGreen;
    private float lastAlpha;

    public CachedString(String text, int listId, float width, float height) {
        this.text = text;
        this.listId = listId;
        this.width = width;
        this.height = height;
    }

    public float getLastAlpha() {
        return lastAlpha;
    }

    public void setLastAlpha(float lastAlpha) {
        this.lastAlpha = lastAlpha;
    }

    public float getLastRed() {
        return lastRed;
    }

    public void setLastRed(float lastRed) {
        this.lastRed = lastRed;
    }

    public float getLastBlue() {
        return lastBlue;
    }

    public void setLastBlue(float lastBlue) {
        this.lastBlue = lastBlue;
    }

    public float getLastGreen() {
        return lastGreen;
    }

    public void setLastGreen(float lastGreen) {
        this.lastGreen = lastGreen;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float v) {
        this.width = v;
    }

    public float getHeight() {
        return height;
    }

    public String getText() {
        return text;
    }

    public int getListId() {
        return listId;
    }
}
