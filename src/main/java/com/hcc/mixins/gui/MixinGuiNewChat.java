/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hcc.mixins.gui;

import com.hcc.event.ChatEvent;
import com.hcc.event.EventBus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat {

    @Shadow
    @Final
    private List<ChatLine> chatLines;
    @Shadow
    @Final
    private List<ChatLine> field_146253_i;
    @Shadow
    private boolean isScrolled;
    @Shadow
    private int scrollPos;
    @Shadow
    @Final
    private Minecraft mc;

    @Shadow
    public abstract void scroll(int p_146229_1_);

    @Shadow
    public abstract boolean getChatOpen();

    @Shadow
    public abstract float getChatScale();

    @Shadow
    public abstract int getChatWidth();

    @Shadow
    public abstract void deleteChatLine(int p_146242_1_);

    /**
     * Invoked once a message is printed to the players chat
     *
     * @param chatComponent the message
     * @param ci            {@see org.spongepowered.asm.mixin.injection.callback.CallbackInfo}
     */
    @Inject(method = "printChatMessage", at = @At("HEAD"), cancellable = true)
    private void printChatMessage(IChatComponent chatComponent, CallbackInfo ci) {
        ChatEvent event = new ChatEvent(chatComponent);
        EventBus.INSTANCE.post(event);
        if (event.isCancelled()) {
            ci.cancel();
        } else {
            if (event.getChat() != chatComponent) {
                printChatMessageWithOptionalDeletion(event.getChat(), 0);
                ci.cancel();
            }
        }
    }


    /**
     * Hardcoded chat limit of 100 -> 300
     *
     * @author Mojang
     */
    @Overwrite
    private void setChatLine(IChatComponent chatComponent, int chatLineId, int p_146237_3_, boolean p_146237_4_) {
        if (chatLineId != 0) {
            this.deleteChatLine(chatLineId);
        }

        int i = MathHelper.floor_float((float) this.getChatWidth() / this.getChatScale());
        List<IChatComponent> list = GuiUtilRenderComponents.func_178908_a(chatComponent, i, this.mc.fontRendererObj, false, false);
        boolean flag = this.getChatOpen();

        for (IChatComponent ichatcomponent : list) {
            if (flag && this.scrollPos > 0) {
                this.isScrolled = true;
                this.scroll(1);
            }

            this.field_146253_i.add(0, new ChatLine(p_146237_3_, ichatcomponent, chatLineId));
        }

        while (this.field_146253_i.size() > 500) {
            this.field_146253_i.remove(this.field_146253_i.size() - 1);
        }

        if (!p_146237_4_) {
            this.chatLines.add(0, new ChatLine(p_146237_3_, chatComponent, chatLineId));

            while (this.chatLines.size() > 500) {
                this.chatLines.remove(this.chatLines.size() - 1);
            }
        }
    }
    @Shadow
    public abstract void printChatMessageWithOptionalDeletion(IChatComponent component, int lineId);
}
