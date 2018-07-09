package cc.hyperium.mixinsimp.gui;

import cc.hyperium.config.Settings;
import cc.hyperium.mixins.gui.IMixinGuiResourcePack;
import java.awt.Color;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.ResourcePackListEntry;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class HyperiumGuiResourcePack {
    private ResourcePackListEntry parent;

    public HyperiumGuiResourcePack(ResourcePackListEntry parent) {
        this.parent = parent;
    }

    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight,
        int mouseX, int mouseY, boolean isSelected, Minecraft mc, CallbackInfo ci) {
        if (!Settings.LEGACY_RP) {
            boolean compact = true;
            if (compact) {
                ci.cancel();
                int i = ((IMixinGuiResourcePack) parent).callFunc_183019_a();

                if (i != 1) {
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    Gui.drawRect(x - 1, y - 1, x + listWidth - 9, y + slotHeight + 1, -8978432);
                }

                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                String s = ((IMixinGuiResourcePack) parent).callFunc_148312_b();
                String s1 = ((IMixinGuiResourcePack) parent).callFunc_148311_a();

                int i1 = mc.fontRendererObj.getStringWidth(s);

                if (i1 > 157) {
                    s = mc.fontRendererObj.trimStringToWidth(s, 157 - mc.fontRendererObj.getStringWidth("...")) + "...";
                }

                mc.fontRendererObj.drawStringWithShadow(s, (float) (x + 2), (float) (y + 4), 16777215);
                List<String> list = mc.fontRendererObj.listFormattedStringToWidth(s1, 157);

                GL11.glPushMatrix();
                GL11.glScalef(0.7F, 0.7F, 0.7F);

                StringBuilder msg = new StringBuilder();
                list.forEach(b -> msg.append(b).append(" "));

                double max = 157 / 0.7;
                int len = mc.fontRendererObj.getStringWidth(msg.toString());

                String info = msg.toString();
                if (len > max) {
                    info = mc.fontRendererObj.trimStringToWidth(msg.toString(), (int) (max - mc.fontRendererObj.getStringWidth("..."))) + "...";
                }
                mc.fontRendererObj.drawStringWithShadow(info, (float) ((x + 5) / 0.7), (float) ((y + 6 + 10) / 0.7), 8421504);


                GL11.glPopMatrix();

                Gui.drawRect(x + 2, y + 12 + 17,
                    x + listWidth - 10, y + 12 + 18, new Color(94, 98, 94, 235).getRGB());

            }
        } else {
            GL11.glPushMatrix();
            GL11.glPopMatrix();
        }
    }
}
