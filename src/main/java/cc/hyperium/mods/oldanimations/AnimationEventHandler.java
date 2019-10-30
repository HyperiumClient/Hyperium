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
import cc.hyperium.event.render.RenderEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovingObjectPosition;

public class AnimationEventHandler {
    private final Minecraft mc;

    AnimationEventHandler() {
        mc = Minecraft.getMinecraft();
    }

    @InvokeEvent
    public void onRenderFirstHand(RenderEvent e) {
        if (mc.thePlayer.getHeldItem() == null) {
            return;
        }

        attemptSwing();
    }

    private void attemptSwing() {
        if (mc.thePlayer.getItemInUseCount() > 0) { //TODO: Config option for 1.7 swing animation
            boolean mouseDown = mc.gameSettings.keyBindAttack.isKeyDown() && mc.gameSettings.keyBindUseItem.isKeyDown();
            if (mouseDown && mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                swingItem(mc.thePlayer);
            }
        }
    }

    private void swingItem(EntityPlayerSP entityplayersp) {
        int swingAnimationEnd = entityplayersp.isPotionActive(Potion.digSpeed) ? (6 - (1 +
            entityplayersp.getActivePotionEffect(Potion.digSpeed).getAmplifier())) : (entityplayersp.isPotionActive(Potion.digSlowdown) ? (6 + (1 +
            entityplayersp.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2) : 6);
        if (!entityplayersp.isSwingInProgress || entityplayersp.swingProgressInt >= swingAnimationEnd / 2 || entityplayersp.swingProgressInt < 0) {
            entityplayersp.swingProgressInt = -1;
            entityplayersp.isSwingInProgress = true;
        }
    }
}
