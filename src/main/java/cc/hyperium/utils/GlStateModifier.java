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

package cc.hyperium.utils;

import me.semx11.autotip.universal.ReflectionUtil;
import net.minecraft.client.renderer.GlStateManager;

import java.lang.reflect.Field;

public class GlStateModifier implements IGlStateModifier {

    // Create an instance to be used in other classes
    public static final IGlStateModifier INSTANCE = new GlStateModifier();

    // Array of fields and such that are retrieved through Reflection
    private Object[] theArray;

    // textureName in GlStateManager
    private Field textureNamefield;

    // redColor in GlStateManager
    private Field redColorField;

    // colorState in GlStateManager
    private Object colorStateObject;

    // activeTextureUnit in GlStateManager
    private Field activeTextureUnitField;

    /**
     * Get textureName through reflection to allow for accessibility
     *
     * @param id Texture ic
     */
    public void setTexture(int id) {
        if (theArray == null) {
            try {
                theArray = (Object[]) ReflectionUtil.findField(GlStateManager.class, "textureState", "field_179174_p", "p").get(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (textureNamefield == null) {
            Class<?> aClass = ReflectionUtil.findClazz("net.minecraft.client.renderer.GlStateManager$TextureState", "bfl$r");
            try {
                textureNamefield = aClass.getDeclaredField("textureName");
            } catch (NoSuchFieldException e) {
                try {
                    textureNamefield = aClass.getDeclaredField("field_179059_b");
                } catch (NoSuchFieldException e1) {
                    try {
                        textureNamefield = aClass.getDeclaredField("b");
                    } catch (NoSuchFieldException e2) {
                        e2.printStackTrace();
                        // At this point there is no hope.
                    }
                }
            }

            if (textureNamefield != null) {
                textureNamefield.setAccessible(true);
            }
        }

        int activeTextureUnit = -1;
        if (activeTextureUnitField == null) {
            activeTextureUnitField = ReflectionUtil.findField(GlStateManager.class, "activeTextureUnit", "field_179162_o", "o");
        }
        try {
            if (activeTextureUnitField != null)
                activeTextureUnit = ((int) activeTextureUnitField.get(null));
        } catch (ReflectionUtil.UnableToAccessFieldException | IllegalAccessException ignored) {
        }

        if (theArray == null || textureNamefield == null || activeTextureUnit == -1) {
            return;
        }
        Object o = theArray[activeTextureUnit];

        try {
            textureNamefield.set(o, id);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * Reset the color
     */
    @Override
    public void resetColor() {
        if (colorStateObject == null) {
            Class<?> aClass = ReflectionUtil.findClazz("net.minecraft.client.renderer.GlStateManager", "bfl");
            Field tmp = null;
            try {
                tmp = aClass.getDeclaredField("colorState");
            } catch (NoSuchFieldException e) {
                try {
                    tmp = aClass.getDeclaredField("field_179170_t");
                } catch (NoSuchFieldException e1) {
                    try {
                        tmp = aClass.getDeclaredField("t");
                    } catch (NoSuchFieldException e2) {
                        e2.printStackTrace();
                    }
                }
            }
            if (tmp != null) {
                tmp.setAccessible(true);
                try {
                    colorStateObject = tmp.get(null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
        if (redColorField == null) {
            Class<?> aClass = ReflectionUtil.findClazz("net.minecraft.client.renderer.GlStateManager$Color", "bfl$e");
            try {
                redColorField = aClass.getDeclaredField("red");
            } catch (NoSuchFieldException e) {
                try {
                    redColorField = aClass.getDeclaredField("field_179195_a");
                } catch (NoSuchFieldException e1) {
                    try {
                        redColorField = aClass.getDeclaredField("a");
                    } catch (NoSuchFieldException e2) {
                        e2.printStackTrace();
                    }
                }
            }
            if (redColorField != null)
                redColorField.setAccessible(true);
        }
        if (colorStateObject == null || redColorField == null) {
            return;
        }
        try {
            redColorField.set(colorStateObject, -1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reset the texture
     */
    public void reset() {
        setTexture(-1);
    }

}
