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

package cc.hyperium.cosmetics.companions.hamster;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityHamster extends EntityTameable {

    public EntityHamster(World worldIn) {
        super(worldIn);
        setSize(0.4F, 0.2F);
        ((PathNavigateGround) getNavigator()).setAvoidsWater(true);
        tasks.addTask(1, new EntityAIFollowOwner(this, 1f, 10f, 2f));
        tasks.addTask(2, new EntityAIWander(this, 1));
        tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayerSP.class, 8f));
        tasks.addTask(3, new EntityAILookIdle(this));
        setTamed(true);
        preventEntitySpawning = false;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        updateEntityActionState();
    }

    @Override
    public void moveEntityWithHeading(float strafe, float forward) {
        if (!isInWater()) {
            if (!isInLava()) {
                float f4 = 0.91F;

                if (onGround) {
                    f4 = worldObj.getBlockState(new BlockPos(MathHelper.floor_double(posX), MathHelper.floor_double(getEntityBoundingBox().minY) - 1,
                        MathHelper.floor_double(posZ))).getBlock().slipperiness * 0.91F;
                }

                float f = 0.16277136F / (f4 * f4 * f4);
                float f5 = onGround ? getAIMoveSpeed() * f : jumpMovementFactor;
                moveFlying(strafe, forward, f5);
                f4 = 0.91F;

                if (onGround) {
                    f4 = worldObj.getBlockState(new BlockPos(MathHelper.floor_double(posX), MathHelper.floor_double(getEntityBoundingBox().minY) - 1,
                        MathHelper.floor_double(posZ))).getBlock().slipperiness * 0.91F;
                }

                if (isOnLadder()) {
                    float f6 = 0.15F;
                    motionX = MathHelper.clamp_double(motionX, -f6, f6);
                    motionZ = MathHelper.clamp_double(motionZ, -f6, f6);
                    fallDistance = 0.0F;
                    if (motionY < -0.15D) motionY = -0.15D;
                }

                moveEntity(motionX, motionY, motionZ);

                if (isCollidedHorizontally && isOnLadder()) motionY = 0.2D;

                if (worldObj.isRemote && (!worldObj.isBlockLoaded(new BlockPos((int) posX, 0, (int) posZ)) ||
                    !worldObj.getChunkFromBlockCoords(new BlockPos((int) posX, 0, (int) posZ)).isLoaded())) {
                    motionY = posY > 0.0D ? -0.1D : 0.0D;
                } else {
                    motionY -= 0.08D;
                }

                motionY *= 0.9800000190734863D;
                motionX *= f4;
                motionZ *= f4;
            } else {
                double d1 = posY;
                moveFlying(strafe, forward, 0.02F);
                moveEntity(motionX, motionY, motionZ);
                motionX *= 0.5D;
                motionY *= 0.5D;
                motionZ *= 0.5D;
                motionY -= 0.02D;

                if (isCollidedHorizontally && isOffsetPositionInLiquid(motionX, motionY + 0.6000000238418579D - posY + d1, motionZ)) {
                    motionY = 0.30000001192092896D;
                }
            }
        } else {
            double d0 = posY;
            float f1 = 0.8F;
            float f2 = 0.02F;
            float f3 = (float) EnchantmentHelper.getDepthStriderModifier(this);
            if (f3 > 3.0F) f3 = 3.0F;
            if (!onGround) f3 *= 0.5F;

            if (f3 > 0.0F) {
                f1 += (0.54600006F - f1) * f3 / 3.0F;
                f2 += (getAIMoveSpeed() * 1.0F - f2) * f3 / 3.0F;
            }

            moveFlying(strafe, forward, f2);
            moveEntity(motionX, motionY, motionZ);
            motionX *= f1;
            motionY *= 0.800000011920929D;
            motionZ *= f1;
            motionY -= 0.02D;

            if (isCollidedHorizontally && isOffsetPositionInLiquid(motionX, motionY + 0.6000000238418579D - posY + d0, motionZ)) {
                motionY = 0.30000001192092896D;
            }
        }

        super.moveEntityWithHeading(strafe, forward);
    }

    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
    }

    @Override
    public boolean canMateWith(EntityAnimal otherAnimal) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
    }
}
