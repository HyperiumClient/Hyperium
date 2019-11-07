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

package cc.hyperium.mixinsimp.client.gui;

import cc.hyperium.config.Settings;
import cc.hyperium.event.network.chat.ChatEvent;
import cc.hyperium.event.EventBus;
import cc.hyperium.mixins.client.gui.IMixinGuiNewChat;

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

    public void drawChat(int updateCounter, List<ChatLine> drawnChatLines, int scrollPos, boolean isScrolled, Minecraft mc) {
        if (mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            int i = parent.getLineCount();
            boolean flag = false;
            int j = 0;
            int k = drawnChatLines.size();
            float f = mc.gameSettings.chatOpacity * 0.9F + 0.1F;

            if (k > 0) {
                if (parent.getChatOpen()) flag = true;

                float f1 = parent.getChatScale();
                int l = MathHelper.ceiling_float_int((float) parent.getChatWidth() / f1);
                GlStateManager.pushMatrix();
                GlStateManager.translate(2.0F, 20.0F, 0.0F);
                GlStateManager.scale(f1, f1, 1.0F);

                for (int i1 = 0; i1 + scrollPos < drawnChatLines.size() && i1 < i; ++i1) {
                    ChatLine chatline = drawnChatLines.get(i1 + scrollPos);

                    if (chatline != null) {
                        int j1 = updateCounter - chatline.getUpdatedCounter();

                        if (j1 < 200 || flag) {
                            double d0 = (double) j1 / 200.0D;
                            d0 = 1.0D - d0;
                            d0 = d0 * 10.0D;
                            d0 = MathHelper.clamp_double(d0, 0.0D, 1.0D);
                            d0 = d0 * d0;
                            int l1 = (int) (255.0D * d0);

                            if (flag) l1 = 255;

                            l1 = (int) ((float) l1 * f);
                            ++j;

                            if (l1 > 3) {
                                int i2 = 0;
                                int j2 = -i1 * 9;
                                if (!Settings.FASTCHAT)
                                    Gui.drawRect(i2, j2 - 9, i2 + l + 4, j2, l1 / 2 << 24);
                                String s = chatline.getChatComponent().getFormattedText();
                                GlStateManager.enableBlend();
                                mc.fontRendererObj.drawStringWithShadow(s, (float) i2, (float) (j2 - 8), 16777215 + (l1 << 24));
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

    public void setChatLine(IChatComponent chatComponent, int chatLineId, int updateCounter, boolean displayOnly, int scrollPos,
                            boolean isScrolled, List<ChatLine> chatLineList, List<ChatLine> chatLines, Minecraft mc) {
        if (chatLineId != 0) {
            parent.deleteChatLine(chatLineId);
        }

        int i = MathHelper.floor_float((float) parent.getChatWidth() / parent.getChatScale());
        List<IChatComponent> list = GuiUtilRenderComponents.splitText(chatComponent, i, mc.fontRendererObj, false, false);
        boolean flag = parent.getChatOpen();

        list.forEach(ichatcomponent -> {
            if (flag && scrollPos > 0) {
                ((IMixinGuiNewChat) parent).setIsScrolled(isScrolled);
                parent.scroll(1);
            }

            chatLineList.add(0, new ChatLine(updateCounter, ichatcomponent, chatLineId));
        });

        while (chatLineList.size() > 500) {
            chatLineList.remove(chatLineList.size() - 1);
        }

        if (!displayOnly) {
            chatLines.add(0, new ChatLine(updateCounter, chatComponent, chatLineId));

            while (chatLines.size() > 500) {
                chatLines.remove(chatLines.size() - 1);
            }
        }
    }
}
