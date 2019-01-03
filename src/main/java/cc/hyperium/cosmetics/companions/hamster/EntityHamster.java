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

        this.setSize(0.4F, 0.2F);

        ((PathNavigateGround) this.getNavigator()).setAvoidsWater(true);
        this.tasks.addTask(1, new EntityAIFollowOwner(this, 1f, 10f, 2f));
        this.tasks.addTask(2, new EntityAIWander(this, 1));
        this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayerSP.class, 8f));
        this.tasks.addTask(3, new EntityAILookIdle(this));

        setTamed(true);

        this.preventEntitySpawning = false;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        this.updateEntityActionState();
    }

    @Override
    public void moveEntityWithHeading(float strafe, float forward) {
        if (!this.isInWater()) {
            if (!this.isInLava()) {
                float f4 = 0.91F;

                if (this.onGround) {
                    f4 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.91F;
                }

                float f = 0.16277136F / (f4 * f4 * f4);
                float f5;

                if (this.onGround) {
                    f5 = this.getAIMoveSpeed() * f;
                } else {
                    f5 = this.jumpMovementFactor;
                }

                this.moveFlying(strafe, forward, f5);
                f4 = 0.91F;

                if (this.onGround) {
                    f4 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.91F;
                }

                if (this.isOnLadder()) {
                    float f6 = 0.15F;
                    this.motionX = MathHelper.clamp_double(this.motionX, (double) (-f6), (double) f6);
                    this.motionZ = MathHelper.clamp_double(this.motionZ, (double) (-f6), (double) f6);
                    this.fallDistance = 0.0F;

                    if (this.motionY < -0.15D) {
                        this.motionY = -0.15D;
                    }
                }

                this.moveEntity(this.motionX, this.motionY, this.motionZ);

                if (this.isCollidedHorizontally && this.isOnLadder()) {
                    this.motionY = 0.2D;
                }

                if (this.worldObj.isRemote && (!this.worldObj.isBlockLoaded(new BlockPos((int) this.posX, 0, (int) this.posZ)) || !this.worldObj.getChunkFromBlockCoords(new BlockPos((int) this.posX, 0, (int) this.posZ)).isLoaded())) {
                    if (this.posY > 0.0D) {
                        this.motionY = -0.1D;
                    } else {
                        this.motionY = 0.0D;
                    }
                } else {
                    this.motionY -= 0.08D;
                }

                this.motionY *= 0.9800000190734863D;
                this.motionX *= (double) f4;
                this.motionZ *= (double) f4;
            } else {
                double d1 = this.posY;
                this.moveFlying(strafe, forward, 0.02F);
                this.moveEntity(this.motionX, this.motionY, this.motionZ);
                this.motionX *= 0.5D;
                this.motionY *= 0.5D;
                this.motionZ *= 0.5D;
                this.motionY -= 0.02D;

                if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d1, this.motionZ)) {
                    this.motionY = 0.30000001192092896D;
                }
            }
        } else {
            double d0 = this.posY;
            float f1 = 0.8F;
            float f2 = 0.02F;
            float f3 = (float) EnchantmentHelper.getDepthStriderModifier(this);

            if (f3 > 3.0F) {
                f3 = 3.0F;
            }

            if (!this.onGround) {
                f3 *= 0.5F;
            }

            if (f3 > 0.0F) {
                f1 += (0.54600006F - f1) * f3 / 3.0F;
                f2 += (this.getAIMoveSpeed() * 1.0F - f2) * f3 / 3.0F;
            }

            this.moveFlying(strafe, forward, f2);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= (double) f1;
            this.motionY *= 0.800000011920929D;
            this.motionZ *= (double) f1;
            this.motionY -= 0.02D;

            if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d0, this.motionZ)) {
                this.motionY = 0.30000001192092896D;
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
