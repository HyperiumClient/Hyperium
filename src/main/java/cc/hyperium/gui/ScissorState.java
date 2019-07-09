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

import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static net.minecraft.client.Minecraft.getMinecraft;
import static org.lwjgl.opengl.GL11.*;

/**
 * ScissorState - Created by ScottehBoeh
 * <p>
 * A more simple approach to the Scissor feature available with
 * OpenGL-based libraries/dev kits. Allows for you to use the GL
 * Scissor method without the necessity of the Minecraft scaled
 * res factor.
 */
public class ScissorState {

    /* Buffer */
    private static final IntBuffer boxBuf = BufferUtils.createIntBuffer(4);

    /**
     * Scissor - Start a Scissor State
     *
     * @param x      - Given X Position
     * @param y      - Given Y Position
     * @param width  - Given Width
     * @param height - Given Height
     */
    public static void scissor(int x, int y, int width, int height) {
        scissor(x, y, width, height, false);
    }

    /**
     * Scissor - Start a Scissor State
     *
     * @param x               - Given X Position
     * @param y               - Given Y Position
     * @param width           - Given Width
     * @param height          - Given Height
     * @param useWindowCoords - Should use Window Coordinates? (Scale-relative)
     */
    public static void scissor(int x, int y, int width, int height, boolean useWindowCoords) {

        /* Check if scissor state should use Minecraft GUI scale factor */
        if (useWindowCoords) {

            /* Get scale factor */
            int scaleFactor = new ScaledResolution(getMinecraft()).getScaleFactor();

            /* Reposition X/Y/Width/Height to position relative to scale factor */
            x *= scaleFactor;
            y *= scaleFactor;
            width *= scaleFactor;
            height *= scaleFactor;

        }

        int sx = x;
        int sy = getMinecraft().displayHeight - (y + height);
        int sw = width;
        int sh = height;

        /* Begin scissor state */
        glPushAttrib(GL_SCISSOR_BIT);

        /* If scissor state already exists, get existing buffer */
        if (glGetBoolean(GL_SCISSOR_TEST)) {
            boxBuf.rewind();
            glGetInteger(GL_SCISSOR_BOX, boxBuf);
            sx = Math.max(sx, boxBuf.get(0));
            sy = Math.max(sy, boxBuf.get(1));
            sw = Math.min(sw, boxBuf.get(2));
            sh = Math.min(sh, boxBuf.get(3));
            /* Else, enable scissor state */
        } else {
            /* Enable scissor state */
            glEnable(GL_SCISSOR_TEST);
        }

        /* Perform scissor state */
        glScissor(sx, sy, sw, sh);
    }

    /**
     * End Scissor - End a Scissor State
     */
    public static void endScissor() {
        glPopAttrib();
    }

}
