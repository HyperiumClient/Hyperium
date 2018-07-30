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
                    e1.printStackTrace();
                }
                e.printStackTrace();
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

    public void reset() {
        setTexture(-1);
    }


}
