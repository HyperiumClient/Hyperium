package cc.hyperium.mixinsimp.client;

import cc.hyperium.mixins.client.IMixinGlstateManager;
import me.semx11.autotip.util.ReflectionUtil;
import net.minecraft.client.renderer.GlStateManager;

import java.lang.reflect.Field;

public class GlStateModifier implements IGlStateModifier {

    public static final IGlStateModifier INSTANCE = new GlStateModifier();
    private Object[] theArray;
    private GlStateManager stateManager = new GlStateManager();
    private Field textureNamefield;
    private Field redColorField = null;
    private Object colorStateObject = null;


    private GlStateModifier() {

    }

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
                        //At this point there is no hope.
                    }
                }
            }

            if (textureNamefield != null)
                textureNamefield.setAccessible(true);
        }

        if (theArray == null || textureNamefield == null) {
            System.out.println("Hey staff team if you see this, there was no hope but this message band aids it so hello. ");
            return;
        }
        Object o = theArray[((IMixinGlstateManager) stateManager).getActiveTextureUnit()];

        try {
            textureNamefield.set(o, id);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

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
                        //At this point there is no hope.
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
                        //At this point there is no hope.
                    }
                }
            }
            if (redColorField != null)
                redColorField.setAccessible(true);
        }
        if (colorStateObject == null || redColorField == null)
            return;
        try {
            redColorField.set(colorStateObject, -1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        setTexture(-1);
    }


}
