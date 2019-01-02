package cc.hyperium.mixinsimp.gui;

import cc.hyperium.config.Settings;
import cc.hyperium.event.ChatEvent;
import cc.hyperium.event.EventBus;
import cc.hyperium.mixins.gui.IMixinGuiNewChat;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class HyperiumGuiNewChat {
    private GuiNewChat parent;

    public HyperiumGuiNewChat(GuiNewChat parent) {
        this.parent = parent;
    }

    public void printChatMessage(IChatComponent chatComponent, CallbackInfo ci) {
        ChatEvent event = new ChatEvent(chatComponent);
        EventBus.INSTANCE.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        } else {
            if (event.getChat() != chatComponent) {
                parent.printChatMessageWithOptionalDeletion(event.getChat(), 0);
                ci.cancel();
            }
        }
    }

    public void drawChat(int p_146230_1_, List<ChatLine> field_146253_i, int scrollPos, boolean isScrolled, Minecraft mc) {
        if (mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            int i = parent.getLineCount();
            boolean flag = false;
            int j = 0;
            int k = field_146253_i.size();
            float f = mc.gameSettings.chatOpacity * 0.9F + 0.1F;

            if (k > 0) {
                if (parent.getChatOpen()) {
                    flag = true;
                }

                float f1 = parent.getChatScale();
                int l = MathHelper.ceiling_float_int((float) parent.getChatWidth() / f1);
                GlStateManager.pushMatrix();
                GlStateManager.translate(2.0F, 20.0F, 0.0F);
                GlStateManager.scale(f1, f1, 1.0F);

                for (int i1 = 0; i1 + scrollPos < field_146253_i.size() && i1 < i; ++i1) {
                    ChatLine chatline = field_146253_i.get(i1 + scrollPos);

                    if (chatline != null) {
                        int j1 = p_146230_1_ - chatline.getUpdatedCounter();

                        if (j1 < 200 || flag) {
                            double d0 = (double) j1 / 200.0D;
                            d0 = 1.0D - d0;
                            d0 = d0 * 10.0D;
                            d0 = MathHelper.clamp_double(d0, 0.0D, 1.0D);
                            d0 = d0 * d0;
                            int l1 = (int) (255.0D * d0);

                            if (flag) {
                                l1 = 255;
                            }

                            l1 = (int) ((float) l1 * f);
                            ++j;

                            if (l1 > 3) {
                                int i2 = 0;
                                int j2 = -i1 * 9;
                                if (!Settings.FASTCHAT)
                                    Gui.drawRect(i2, j2 - 9, i2 + l + 4, j2, l1 / 2 << 24);
                                String s = chatline.getChatComponent().getFormattedText();
                                GlStateManager.enableBlend();
                                mc.fontRendererObj
                                    .drawStringWithShadow(s, (float) i2, (float) (j2 - 8),
                                        16777215 + (l1 << 24));
                                GlStateManager.disableAlpha();
                                GlStateManager.disableBlend();
                            }
                        }
                    }
                }

                if (flag) {
                    int k2 = mc.fontRendererObj.FONT_HEIGHT;
                    GlStateManager.translate(-3.0F, 0.0F, 0.0F);
                    int l2 = k * k2 + k;
                    int i3 = j * k2 + j;
                    int j3 = scrollPos * i3 / k;
                    int k1 = i3 * i3 / l2;

                    if (l2 != i3) {
                        int k3 = j3 > 0 ? 170 : 96;
                        int l3 = isScrolled ? 13382451 : 3355562;
                        Gui.drawRect(0, -j3, 2, -j3 - k1, l3 + (k3 << 24));
                        Gui.drawRect(2, -j3, 1, -j3 - k1, 13421772 + (k3 << 24));
                    }
                }

                GlStateManager.popMatrix();
            }
        }
    }

    public void setChatLine(IChatComponent chatComponent, int chatLineId, int p_146237_3_, boolean p_146237_4_, int scrollPos, boolean isScrolled, List<ChatLine> field_146253_i, List<ChatLine> chatLines, Minecraft mc) {
        if (chatLineId != 0) {
            parent.deleteChatLine(chatLineId);
        }

        int i = MathHelper.floor_float((float) parent.getChatWidth() / parent.getChatScale());
        List<IChatComponent> list = GuiUtilRenderComponents
            .func_178908_a(chatComponent, i, mc.fontRendererObj, false, false);
        boolean flag = parent.getChatOpen();

        for (IChatComponent ichatcomponent : list) {
            if (flag && scrollPos > 0) {
                ((IMixinGuiNewChat) parent).setIsScrolled(isScrolled);
                parent.scroll(1);
            }

            field_146253_i.add(0, new ChatLine(p_146237_3_, ichatcomponent, chatLineId));
        }

        while (field_146253_i.size() > 500) {
            field_146253_i.remove(field_146253_i.size() - 1);
        }

        if (!p_146237_4_) {
            chatLines.add(0, new ChatLine(p_146237_3_, chatComponent, chatLineId));

            while (chatLines.size() > 500) {
                chatLines.remove(chatLines.size() - 1);
            }
        }
    }
}
