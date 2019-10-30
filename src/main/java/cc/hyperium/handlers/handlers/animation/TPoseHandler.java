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

public class TPoseHandler extends AbstractPreCopyAnglesAnimationHandler {

    @Override
    public void onRender() {
        if (!get(Minecraft.getMinecraft().thePlayer.getUniqueID()).isAnimating() && get(Minecraft.getMinecraft().thePlayer.getUniqueID()).shouldReset())
            state = 0.0f;
    }

    @Override
    public float modifyState() {
        return HyperiumGui.clamp(
            HyperiumGui.easeOut(
                    state,
                100.0f,
                0.01f,
                5
            ),
            0.0f,
            100.0f
        );
    }


    @Override
    public void modifyPlayer(AbstractClientPlayer entity, IMixinModelPlayer player, float heldPercent) {
        player.getBipedLeftUpperArm().rotateAngleZ = (float) Math.toRadians(-90.0f * heldPercent);
        player.getBipedLeftUpperArmwear().rotateAngleZ = (float) Math.toRadians(-90.0f * heldPercent);

        player.getBipedLeftUpperArm().rotateAngleX = 0;
        player.getBipedLeftUpperArmwear().rotateAngleX = 0;

        player.getBipedLeftUpperArm().rotateAngleY = 0;
        player.getBipedLeftUpperArmwear().rotateAngleY = 0;

        player.getBipedRightUpperArm().rotateAngleZ = (float) Math.toRadians(90.0f * heldPercent);
        player.getBipedRightUpperArmwear().rotateAngleZ = (float) Math.toRadians(90.0f * heldPercent);

        player.getBipedRightUpperArm().rotateAngleX = 0;
        player.getBipedRightUpperArmwear().rotateAngleX = 0;

        player.getBipedRightUpperArm().rotateAngleY = 0;
        player.getBipedRightUpperArmwear().rotateAngleY = 0;
    }

    @Override
    public void modifyPlayer(AbstractClientPlayer entity, IMixinModelBiped player, float heldPercent) {
        player.getBipedLeftUpperArm().rotateAngleZ = (float) Math.toRadians(-90.0f * heldPercent);
        player.getBipedLeftUpperArm().rotateAngleY = 0;
        player.getBipedLeftUpperArm().rotateAngleX = 0;

        player.getBipedRightUpperArm().rotateAngleZ = (float) Math.toRadians(90.0f * heldPercent);
        player.getBipedRightUpperArm().rotateAngleY = 0;
        player.getBipedRightUpperArm().rotateAngleX = 0;
    }
}
