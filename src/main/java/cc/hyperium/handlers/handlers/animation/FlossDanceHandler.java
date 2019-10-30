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

import java.util.Random;

public class FlossDanceHandler extends AbstractPreCopyAnglesAnimationHandler {

    private final Random random = new Random();
    private final float[] randomHeadMovement = new float[3]; // x, y, z values
    private ArmsDirection armsDirection = ArmsDirection.HORIZONTAL;
    private long systemTime;

    public FlossDanceHandler() {
        fillRandomHeadMovementArray();
    }

    @Override
    public float modifyState() {
        float speed = 6;
        if (systemTime == 0) systemTime = Minecraft.getSystemTime();

        while (systemTime < Minecraft.getSystemTime() + (1000 / 120)) {
            state = HyperiumGui.clamp(state + (asc ? speed : -speed), 0.0f, 100.0f);
            systemTime += (1000 / 120);

            if (state <= 0) {
                asc = true;
                right = !right;
                armsDirection = ArmsDirection.values()[armsDirection.ordinal() + 1 >= ArmsDirection.values().length ? 0 : armsDirection.ordinal() + 1];
                fillRandomHeadMovementArray();
            } else if (state >= 100) {
                asc = false;
            }
        }

        return state;
    }

    private void fillRandomHeadMovementArray() {
        randomHeadMovement[0] = (2.0f * random.nextFloat() - 1.0f) * 15.0f;
        randomHeadMovement[1] = (2.0f * random.nextFloat() - 1.0f) * 15.0f;
        randomHeadMovement[2] = (2.0f * random.nextFloat() - 1.0f) * 15.0f;
    }

    @Override
    public void modifyPlayer(AbstractClientPlayer entity, IMixinModelPlayer player, float heldPercent) {
        if (shouldNotModify(entity, player)) return;

        player.getBipedBody().rotateAngleZ = (float) Math.toRadians((right ? 10f : -10f) * heldPercent);
        player.getBipedBodywear().rotateAngleZ = (float) Math.toRadians((right ? 10f : -10f) * heldPercent);

        player.getBipedRightUpperLeg().rotateAngleZ = (float) Math.toRadians((right ? -10f : 10f) * heldPercent);
        player.getBipedRightUpperLegwear().rotateAngleZ = (float) Math.toRadians((right ? -10f : 10f) * heldPercent);
        player.getBipedLeftUpperLeg().rotateAngleZ = (float) Math.toRadians((right ? -10f : 10f) * heldPercent);
        player.getBipedLeftUpperLegwear().rotateAngleZ = (float) Math.toRadians((right ? -10f : 10f) * heldPercent);
        player.getBipedRightUpperLeg().offsetX = (right ? -0.17f : 0.17f) * heldPercent;
        player.getBipedRightUpperLegwear().offsetX = (right ? -0.17f : 0.17f) * heldPercent;
        player.getBipedLeftUpperLeg().offsetX = (right ? -0.17f : 0.17f) * heldPercent;
        player.getBipedLeftUpperLegwear().offsetX = (right ? -0.17f : 0.17f) * heldPercent;

        player.getBipedHead().rotateAngleX = (float) Math.toRadians(randomHeadMovement[0] * heldPercent);
        player.getBipedHeadwear().rotateAngleX = (float) Math.toRadians(randomHeadMovement[0] * heldPercent);
        player.getBipedHead().rotateAngleY = (float) Math.toRadians(randomHeadMovement[1] * heldPercent);
        player.getBipedHeadwear().rotateAngleY = (float) Math.toRadians(randomHeadMovement[1] * heldPercent);
        player.getBipedHead().rotateAngleZ = (float) Math.toRadians(randomHeadMovement[2] * heldPercent);
        player.getBipedHeadwear().rotateAngleZ = (float) Math.toRadians(randomHeadMovement[2] * heldPercent);

        player.getBipedRightUpperArm().rotateAngleZ = (float) Math.toRadians((right ? -50f : 50f) * heldPercent);
        player.getBipedRightUpperArmwear().rotateAngleZ = (float) Math.toRadians((right ? -50f : 50f) * heldPercent);
        player.getBipedRightUpperArm().rotateAngleX = (float) Math.toRadians((armsDirection == ArmsDirection.BACK ? 30.0f : -30.0f) * heldPercent);
        player.getBipedRightUpperArmwear().rotateAngleX = (float) Math.toRadians((armsDirection == ArmsDirection.BACK ? 30.0f : -30.0f) * heldPercent);

        player.getBipedLeftUpperArm().rotateAngleZ = (float) Math.toRadians((right ? -50f : 50f) * heldPercent);
        player.getBipedLeftUpperArmwear().rotateAngleZ = (float) Math.toRadians((right ? -50f : 50f) * heldPercent);
        player.getBipedLeftUpperArm().rotateAngleX = (float) Math.toRadians((armsDirection == ArmsDirection.BACK ? 30.0f : -30.0f) * heldPercent);
        player.getBipedLeftUpperArmwear().rotateAngleX = (float) Math.toRadians((armsDirection == ArmsDirection.BACK ? 30.0f : -30.0f) * heldPercent);
    }

    @Override
    public void modifyPlayer(AbstractClientPlayer entity, IMixinModelBiped player, float heldPercent) {
        if (shouldNotModify(entity, player)) {
            return;
        }

        player.getBipedBody().rotateAngleZ = (float) Math.toRadians((right ? 10f : -10f) * heldPercent);

        player.getBipedRightUpperLeg().rotateAngleZ = (float) Math.toRadians((right ? -10f : 10f) * heldPercent);
        player.getBipedLeftUpperLeg().rotateAngleZ = (float) Math.toRadians((right ? -10f : 10f) * heldPercent);
        player.getBipedRightUpperLeg().offsetX = (right ? -0.17f : 0.17f) * heldPercent;
        player.getBipedLeftUpperLeg().offsetX = (right ? -0.17f : 0.17f) * heldPercent;

        player.getBipedHead().rotateAngleX = (float) Math.toRadians(randomHeadMovement[0] * heldPercent);
        player.getBipedHeadwear().rotateAngleX = (float) Math.toRadians(randomHeadMovement[0] * heldPercent);
        player.getBipedHead().rotateAngleY = (float) Math.toRadians(randomHeadMovement[1] * heldPercent);
        player.getBipedHeadwear().rotateAngleY = (float) Math.toRadians(randomHeadMovement[1] * heldPercent);
        player.getBipedHead().rotateAngleZ = (float) Math.toRadians(randomHeadMovement[2] * heldPercent);
        player.getBipedHeadwear().rotateAngleZ = (float) Math.toRadians(randomHeadMovement[2] * heldPercent);

        player.getBipedRightUpperArm().rotateAngleZ = (float) Math.toRadians((right ? -50f : 50f) * heldPercent);
        player.getBipedRightUpperArm().rotateAngleX = (float) Math.toRadians((armsDirection == ArmsDirection.BACK ? 30.0f : -30.0f) * heldPercent);

        player.getBipedLeftUpperArm().rotateAngleZ = (float) Math.toRadians((right ? -50f : 50f) * heldPercent);
        player.getBipedLeftUpperArm().rotateAngleX = (float) Math.toRadians((armsDirection == ArmsDirection.BACK ? 30.0f : -30.0f) * heldPercent);
    }

    private boolean shouldNotModify(AbstractClientPlayer entity, IMixinModelBiped player) {
        AnimationState animationState = get(entity.getUniqueID());
        int ticks = animationState.getFrames();

        if (ticks <= 2) {
            if (animationState.shouldReset()) {
                resetAnimation(player);
            }

            return true;
        }

        return false;
    }

    enum ArmsDirection {
        HORIZONTAL, BACK, FRONT
    }
}
