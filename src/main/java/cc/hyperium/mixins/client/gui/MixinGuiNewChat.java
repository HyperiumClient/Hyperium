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

package cc.hyperium.mixins.client.gui;

import cc.hyperium.mixinsimp.client.gui.HyperiumGuiNewChat;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat {

    @Shadow @Final private List<ChatLine> chatLines;
    @Shadow @Final private List<ChatLine> drawnChatLines;
    @Shadow @Final private Minecraft mc;
    @Shadow private boolean isScrolled;
    @Shadow private int scrollPos;

    private HyperiumGuiNewChat hyperiumGuiNewChat = new HyperiumGuiNewChat((GuiNewChat) (Object) this);

    /**
     * Invoked once a message is printed to the players chat
     *
     * @param chatComponent the message
     * @param ci            {@see org.spongepowered.asm.mixin.injection.callback.CallbackInfo}
     */
    @Inject(method = "printChatMessage", at = @At("HEAD"), cancellable = true)
    private void printChatMessage(IChatComponent chatComponent, CallbackInfo ci) {
        hyperiumGuiNewChat.printChatMessage(chatComponent, ci);
    }

    /**
     *
     * @author Mojang
     * @reason Draws minecraft chat
     */
    @Overwrite
    public void drawChat(int updateCounter) {
        hyperiumGuiNewChat.drawChat(updateCounter, drawnChatLines, scrollPos, isScrolled, mc);
    }


    /**
     *
     * @author Mojang
     * @reason Change chat limit of 100 to 500
     */
    @Overwrite
    private void setChatLine(IChatComponent chatComponent, int chatLineId, int p_146237_3_, boolean p_146237_4_) {
        hyperiumGuiNewChat.setChatLine(chatComponent, chatLineId, p_146237_3_, p_146237_4_, scrollPos, isScrolled, drawnChatLines, chatLines, mc);
    }
}
