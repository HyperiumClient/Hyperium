/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mixins.gui;

import cc.hyperium.mixinsimp.gui.HyperiumGuiNewChat;
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

    private HyperiumGuiNewChat hyperiumGuiNewChat = new HyperiumGuiNewChat((GuiNewChat) (Object) this);

    /**
     * Invoked once a message is printed to the players chat
     *
     * @param chatComponent the message
     * @param ci            {@see org.spongepowered.asm.mixin.injection.callback.CallbackInfo}
     */
    @Inject(method = "printChatMessage", at = @At("HEAD"), cancellable = true)
    private void printChatMessage(IChatComponent chatComponent, CallbackInfo ci) {
        hyperiumGuiNewChat.printChatMessage(chatComponent,ci);
    }

    /**
     * Draws minecraft chat
     *
     * @author Mojang
     */
    @Overwrite
    public void drawChat(int p_146230_1_) {
        hyperiumGuiNewChat.drawChat(p_146230_1_,this.field_146253_i,this.scrollPos,this.isScrolled,this.mc);
    }


    /**
     * Hardcoded chat limit of 100 -> 300
     *
     * @author Mojang
     */
    @Overwrite
    private void setChatLine(IChatComponent chatComponent, int chatLineId, int p_146237_3_, boolean p_146237_4_) {
        hyperiumGuiNewChat.setChatLine(chatComponent,chatLineId,p_146237_3_,p_146237_4_,this.scrollPos,this.isScrolled,this.field_146253_i,this.chatLines,this.mc);
    }
}
