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

package cc.hyperium.mods.oldanimations;

import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovingObjectPosition;

class AnimationEventHandler {
    private final Minecraft mc;

    AnimationEventHandler() {
        this.mc = Minecraft.getMinecraft();
    }

    @InvokeEvent
    public void onRenderFirstHand(final RenderEvent e) {
        if (this.mc.thePlayer.getHeldItem() == null) {
            return;
        }
        this.attemptSwing();
    }

    private void attemptSwing() {
        if (this.mc.thePlayer.getItemInUseCount() > 0) { //TODO: Config option for 1.7 swing animation
            final boolean mouseDown = this.mc.gameSettings.keyBindAttack.isKeyDown() && this.mc.gameSettings.keyBindUseItem.isKeyDown();
            if (mouseDown && this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                this.swingItem(this.mc.thePlayer);
            }
        }
    }

    private void swingItem(final EntityPlayerSP entityplayersp) {
        final int swingAnimationEnd = entityplayersp.isPotionActive(Potion.digSpeed) ? (6 - (1 + entityplayersp.getActivePotionEffect(Potion.digSpeed).getAmplifier())) : (entityplayersp.isPotionActive(Potion.digSlowdown) ? (6 + (1 + entityplayersp.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2) : 6);
        if (!entityplayersp.isSwingInProgress || entityplayersp.swingProgressInt >= swingAnimationEnd / 2 || entityplayersp.swingProgressInt < 0) {
            entityplayersp.swingProgressInt = -1;
            entityplayersp.isSwingInProgress = true;
        }
    }
}
