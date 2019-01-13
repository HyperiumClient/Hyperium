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

package cc.hyperium.mixins.world;

import cc.hyperium.mixinsimp.world.HyperiumChunk;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(Chunk.class)
public class MixinChunk {

    @Shadow
    @Final
    private static Logger logger;

    @Shadow
    @Final
    public int xPosition;

    @Shadow
    @Final
    public int zPosition;

    @Shadow
    private boolean isChunkLoaded;

    @Shadow
    @Final
    private World worldObj;

    @Shadow
    @Final
    private Map<BlockPos, TileEntity> chunkTileEntityMap;

    @Shadow
    @Final
    private ClassInheritanceMultiMap<Entity>[] entityLists;

    @Shadow
    private boolean hasEntities;

    private HyperiumChunk hyperiumChunk = new HyperiumChunk();

    /**
     * Used in fullbright module
     *
     * @param ci
     * @param type
     * @param pos
     */
    @Inject(method = "getLightFor", at = @At("HEAD"), cancellable = true)
    private void getLightFor(EnumSkyBlock type, BlockPos pos, CallbackInfoReturnable<Integer> ci) {
        hyperiumChunk.getLightFor(type, pos, ci);
    }


    /**
     * Used in fullbright module
     *
     * @param pos
     * @param amount
     */
    @Inject(method = "getLightSubtracted", at = @At("HEAD"), cancellable = true)
    private void getLightSubtracted(BlockPos pos, int amount, CallbackInfoReturnable<Integer> ci) {
        hyperiumChunk.getLightSubtracted(pos, amount, ci);
    }

    /**
     * @author Sk1er
     * @reason Add concurrent
     */
    @Overwrite
    public void onChunkLoad() {
        this.isChunkLoaded = true;
        this.worldObj.addTileEntities(this.chunkTileEntityMap.values());

        synchronized (entityLists) {
            for (ClassInheritanceMultiMap<Entity> entityList : this.entityLists) {
                for (Entity entity : entityList) {
                    entity.onChunkLoad();
                }

                this.worldObj.loadEntities(entityList);
            }
        }
    }

    /**
     * @author Sk1er
     * @reason Add concurrent
     */
    @Overwrite
    public void onChunkUnload() {
        synchronized (entityLists) {
            this.isChunkLoaded = false;

            for (TileEntity tileentity : this.chunkTileEntityMap.values()) {
                this.worldObj.markTileEntityForRemoval(tileentity);
            }

            for (int i = 0; i < this.entityLists.length; ++i) {
                this.worldObj.unloadEntities(this.entityLists[i]);
            }
        }
    }

    /**
     * @author Sk1er
     * @reason Add concurrent
     */
    @Overwrite
    public void addEntity(Entity entityIn) {
        synchronized (entityLists) {
            this.hasEntities = true;
            int i = MathHelper.floor_double(entityIn.posX / 16.0D);
            int j = MathHelper.floor_double(entityIn.posZ / 16.0D);

            if (i != this.xPosition || j != this.zPosition) {
                logger.warn("Wrong location! (" + i + ", " + j + ") should be (" + this.xPosition + ", " + this.zPosition + "), " + entityIn, entityIn);
                entityIn.setDead();
            }

            int k = MathHelper.floor_double(entityIn.posY / 16.0D);

            if (k < 0) {
                k = 0;
            }

            if (k >= this.entityLists.length) {
                k = this.entityLists.length - 1;
            }

            entityIn.addedToChunk = true;
            entityIn.chunkCoordX = this.xPosition;
            entityIn.chunkCoordY = k;
            entityIn.chunkCoordZ = this.zPosition;
            this.entityLists[k].add(entityIn);
        }
    }

    /**
     * @author Sk1er
     * @reason Add concurrent
     */
    @Overwrite
    public void removeEntityAtIndex(Entity entityIn, int p_76608_2_) {
        synchronized (entityLists) {
            if (p_76608_2_ < 0) {
                p_76608_2_ = 0;
            }

            if (p_76608_2_ >= this.entityLists.length) {
                p_76608_2_ = this.entityLists.length - 1;
            }

            this.entityLists[p_76608_2_].remove(entityIn);
        }
    }

    /**
     * @author Sk1er
     * @reason Add concurrent
     */
    @Overwrite
    public void getEntitiesWithinAABBForEntity(Entity entityIn, AxisAlignedBB aabb, List<Entity> listToFill, com.google.common.base.Predicate<? super Entity> p_177414_4_) {
        synchronized (entityLists) {
            int i = MathHelper.floor_double((aabb.minY - 2.0D) / 16.0D);
            int j = MathHelper.floor_double((aabb.maxY + 2.0D) / 16.0D);
            i = MathHelper.clamp_int(i, 0, this.entityLists.length - 1);
            j = MathHelper.clamp_int(j, 0, this.entityLists.length - 1);

            for (int k = i; k <= j; ++k) {
                if (!this.entityLists[k].isEmpty()) {
                    for (Entity entity : this.entityLists[k]) {
                        if (entity.getEntityBoundingBox().intersectsWith(aabb) && entity != entityIn) {
                            if (p_177414_4_ == null || p_177414_4_.apply(entity)) {
                                listToFill.add(entity);
                            }

                            Entity[] aentity = entity.getParts();

                            if (aentity != null) {
                                for (int l = 0; l < aentity.length; ++l) {
                                    entity = aentity[l];

                                    if (entity != entityIn && entity.getEntityBoundingBox().intersectsWith(aabb) && (p_177414_4_ == null || p_177414_4_.apply(entity))) {
                                        listToFill.add(entity);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @author Sk1er
     * @reason Add concurrent
     */
    @Overwrite
    public <T extends Entity> void getEntitiesOfTypeWithinAAAB(Class<? extends T> entityClass, AxisAlignedBB aabb, List<T> listToFill, Predicate<? super T> p_177430_4_) {
        synchronized (entityLists) {
            int i = MathHelper.floor_double((aabb.minY - 2.0D) / 16.0D);
            int j = MathHelper.floor_double((aabb.maxY + 2.0D) / 16.0D);
            i = MathHelper.clamp_int(i, 0, this.entityLists.length - 1);
            j = MathHelper.clamp_int(j, 0, this.entityLists.length - 1);

            for (int k = i; k <= j; ++k) {
                for (T t : this.entityLists[k].getByClass(entityClass)) {
                    if (t.getEntityBoundingBox().intersectsWith(aabb) && (p_177430_4_ == null || p_177430_4_.apply(t))) {
                        listToFill.add(t);
                    }
                }
            }
        }
    }
}
