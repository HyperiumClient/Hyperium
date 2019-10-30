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

package cc.hyperium.handlers.handlers.animation;

import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.mixinsimp.client.model.IMixinModelBiped;
import cc.hyperium.mixinsimp.client.model.IMixinModelPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;

public class DabHandler extends AbstractPreCopyAnglesAnimationHandler {

    private long systemTime;

    @Override
    public float modifyState() {
        if (systemTime == 0) systemTime = Minecraft.getSystemTime();

        while (systemTime < Minecraft.getSystemTime() + (1000 / 120)) {
            state = HyperiumGui.clamp(HyperiumGui.easeOut(state, asc ? 100.0f : 0.0f, 0.01f, 5), 0.0f, 100.0f);

            systemTime += (1000 / 120);

            if (state <= 0) {
                asc = true;
                right = !right;
            } else if (state >= 100) {
                asc = false;
            }
        }

        return state;
    }

    @Override
    public void modifyPlayer(AbstractClientPlayer entity, IMixinModelPlayer player, float heldPercent) {
        if (right) {
            player.getBipedRightUpperArm().rotateAngleX = (float) Math.toRadians(-90.0f * heldPercent);
            player.getBipedRightUpperArm().rotateAngleY = (float) Math.toRadians(-35.0f * heldPercent);

            player.getBipedRightUpperArmwear().rotateAngleX = (float) Math.toRadians(-90.0f * heldPercent);
            player.getBipedRightUpperArmwear().rotateAngleY = (float) Math.toRadians(-35.0f * heldPercent);

            player.getBipedLeftUpperArm().rotateAngleX = (float) Math.toRadians(15.0f * heldPercent);
            player.getBipedLeftUpperArm().rotateAngleY = (float) Math.toRadians(15.0f * heldPercent);
            player.getBipedLeftUpperArm().rotateAngleZ = (float) Math.toRadians(-110.0f * heldPercent);

            player.getBipedLeftUpperArmwear().rotateAngleX = (float) Math.toRadians(15.0f * heldPercent);
            player.getBipedLeftUpperArmwear().rotateAngleY = (float) Math.toRadians(15.0f * heldPercent);
            player.getBipedLeftUpperArmwear().rotateAngleZ = (float) Math.toRadians(-110.0f * heldPercent);

            float rotationX = entity.rotationPitch;
            float rotationY = entity.renderYawOffset - entity.rotationYaw;

            player.getBipedHead().rotateAngleX = (float) Math.toRadians(-rotationX * heldPercent) + (float) Math.toRadians(45.0f * heldPercent + rotationX);
            player.getBipedHead().rotateAngleY = (float) Math.toRadians(rotationY * heldPercent) + (float) Math.toRadians(35.0f * heldPercent - rotationY);

            player.getBipedHeadwear().rotateAngleX = (float) Math.toRadians(-rotationX * heldPercent) + (float) Math.toRadians(45.0f * heldPercent + rotationX);
            player.getBipedHeadwear().rotateAngleY = (float) Math.toRadians(rotationY * heldPercent) + (float) Math.toRadians(35.0f * heldPercent - rotationY);

        } else {
            player.getBipedLeftUpperArm().rotateAngleX = (float) Math.toRadians(-90.0f * heldPercent);
            player.getBipedLeftUpperArm().rotateAngleY = (float) Math.toRadians(35.0f * heldPercent);

            player.getBipedLeftUpperArmwear().rotateAngleX = (float) Math.toRadians(-90.0f * heldPercent);
            player.getBipedLeftUpperArmwear().rotateAngleY = (float) Math.toRadians(35.0f * heldPercent);

            player.getBipedRightUpperArm().rotateAngleX = (float) Math.toRadians(-15.0f * heldPercent);
            player.getBipedRightUpperArm().rotateAngleY = (float) Math.toRadians(-15.0f * heldPercent);
            player.getBipedRightUpperArm().rotateAngleZ = (float) Math.toRadians(110.0f * heldPercent);

            player.getBipedRightUpperArmwear().rotateAngleX = (float) Math.toRadians(-15.0f * heldPercent);
            player.getBipedRightUpperArmwear().rotateAngleY = (float) Math.toRadians(-15.0f * heldPercent);
            player.getBipedRightUpperArmwear().rotateAngleZ = (float) Math.toRadians(110.0f * heldPercent);


            float rotationX = entity.rotationPitch;
            player.getBipedHead().rotateAngleX = (float) Math.toRadians(-rotationX * heldPercent) + (float) Math.toRadians(45.0f * heldPercent + rotationX);
            float rotationY = entity.renderYawOffset - entity.rotationYaw;

            player.getBipedHead().rotateAngleY = (float) Math.toRadians(rotationY * heldPercent) + (float) Math.toRadians(-35.0f * heldPercent - rotationY);

            player.getBipedHeadwear().rotateAngleX = (float) Math.toRadians(-rotationX * heldPercent) + (float) Math.toRadians(45.0f * heldPercent + rotationX);
            player.getBipedHeadwear().rotateAngleY = (float) Math.toRadians(rotationY * heldPercent) + (float) Math.toRadians(-35.0f * heldPercent - rotationY);

        }
    }

    @Override
    public void modifyPlayer(AbstractClientPlayer entity, IMixinModelBiped player, float heldPercent) {
        if (right) {
            player.getBipedRightUpperArm().rotateAngleX = (float) Math.toRadians(-90.0f * heldPercent);
            player.getBipedRightUpperArm().rotateAngleY = (float) Math.toRadians(-35.0f * heldPercent);
            player.getBipedLeftUpperArm().rotateAngleX = (float) Math.toRadians(15.0f * heldPercent);
            player.getBipedLeftUpperArm().rotateAngleY = (float) Math.toRadians(15.0f * heldPercent);
            player.getBipedLeftUpperArm().rotateAngleZ = (float) Math.toRadians(-110.0f * heldPercent);
            float rotationX = entity.rotationPitch;
            player.getBipedHead().rotateAngleX = (float) Math.toRadians(-rotationX * heldPercent) + (float) Math.toRadians(45.0f * heldPercent + rotationX);
            float rotationY = entity.renderYawOffset - entity.rotationYaw;
            player.getBipedHead().rotateAngleY = (float) Math.toRadians(rotationY * heldPercent) + (float) Math.toRadians(35.0f * heldPercent - rotationY);
            player.getBipedHeadwear().rotateAngleX = (float) Math.toRadians(45.0f * heldPercent);
            player.getBipedHeadwear().rotateAngleY = (float) Math.toRadians(35.0f * heldPercent);
        } else {
            player.getBipedLeftUpperArm().rotateAngleX = (float) Math.toRadians(-90.0f * heldPercent);
            player.getBipedLeftUpperArm().rotateAngleY = (float) Math.toRadians(35.0f * heldPercent);
            player.getBipedRightUpperArm().rotateAngleX = (float) Math.toRadians(-15.0f * heldPercent);
            player.getBipedRightUpperArm().rotateAngleY = (float) Math.toRadians(-15.0f * heldPercent);
            player.getBipedRightUpperArm().rotateAngleZ = (float) Math.toRadians(110.0f * heldPercent);
            float rotationX = entity.rotationPitch;
            player.getBipedHead().rotateAngleX = (float) Math.toRadians(-rotationX * heldPercent) + (float) Math.toRadians(45.0f * heldPercent + rotationX);
            float rotationY = entity.renderYawOffset - entity.rotationYaw;
            player.getBipedHead().rotateAngleY = (float) Math.toRadians(rotationY * heldPercent) + (float) Math.toRadians(-35.0f * heldPercent - rotationY);
            player.getBipedHeadwear().rotateAngleX = (float) Math.toRadians(45.0f * heldPercent);
            player.getBipedHeadwear().rotateAngleY = (float) Math.toRadians(-35.0f * heldPercent);
        }
    }
}
