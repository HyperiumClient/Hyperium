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

import cc.hyperium.config.Settings;
import cc.hyperium.event.EntityJoinWorldEvent;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.SpawnpointChangeEvent;
import java.util.Collection;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class MixinWorld {

    @Shadow
    private WorldInfo worldInfo;

  @Shadow @Final public List<Entity> loadedEntityList;

  @Shadow public abstract Chunk getChunkFromChunkCoords(int p_getChunkFromChunkCoords_1_,
      int p_getChunkFromChunkCoords_2_);

  @Shadow protected abstract boolean isChunkLoaded(int p_isChunkLoaded_1_, int p_isChunkLoaded_2_,
      boolean p_isChunkLoaded_3_);

  @Shadow @Final public List<EntityPlayer> playerEntities;

  @Shadow public abstract void updateAllPlayersSleepingFlag();

  @Shadow protected abstract void onEntityAdded(Entity p_onEntityAdded_1_);

  /**
     * Invoked once the server changes the players spawn point
     *
     * @param pos the new spawn position
     * @param ci  {@see org.spongepowered.asm.mixin.injection.callback.CallbackInfo}
     */
    @Inject(method = "setSpawnPoint", at = @At("HEAD"))
    private void setSpawnPoint(BlockPos pos, CallbackInfo ci) {
        EventBus.INSTANCE.post(new SpawnpointChangeEvent(pos));
    }

    /**
     * Removes lightupdates
     *
     * @param lightType
     * @param pos
     * @param ci
     */
    @Inject(method = "checkLightFor", at = @At("HEAD"), cancellable = true)
    private void checkLightFor(EnumSkyBlock lightType, BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && Settings.FULLBRIGHT) {
            ci.setReturnValue(false);
        }
    }


    /**
     * Removes lightupdates
     *
     * @param type
     * @param pos
     * @param ci
     */
    @Inject(method = "getLightFromNeighborsFor", at = @At("HEAD"), cancellable = true)
    private void getLightFromNeighborsFor(EnumSkyBlock type, BlockPos pos, CallbackInfoReturnable<Integer> ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && Settings.FULLBRIGHT) {
            ci.setReturnValue(15);
        }
    }

    /**
     * Fixes Void Flicker
     *
     * @author prplz/2pi
     */
    @Overwrite
    public double getHorizon() {
        if (Settings.VOID_FLICKER_FIX) {
            return 0.0;
        }
        return this.worldInfo.getTerrainType() == WorldType.FLAT ? 0.0D : 63.0D;
    }

    /**
     * Removes lightupdates
     *
     * @param pos
     * @param ci
     */
    @Inject(method = "getLightFromNeighbors", at = @At("HEAD"), cancellable = true)
    private void getLightFromNeighbor(BlockPos pos, CallbackInfoReturnable<Integer> ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && Settings.FULLBRIGHT) {
            ci.setReturnValue(15);
        }
    }

    /**
     * Removes lightupdates
     *
     * @param pos
     * @param lightType
     * @param ci
     */
    @Inject(method = "getRawLight", at = @At("HEAD"), cancellable = true)
    private void getRawLight(BlockPos pos, EnumSkyBlock lightType, CallbackInfoReturnable<Integer> ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && Settings.FULLBRIGHT) {
            ci.setReturnValue(15);
        }
    }

    /**
     * Removes lightupdates
     *
     * @param pos
     * @param ci
     */
    @Inject(method = "getLight(Lnet/minecraft/util/BlockPos;)I", at = @At("HEAD"), cancellable = true)
    private void getLight(BlockPos pos, CallbackInfoReturnable<Integer> ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && Settings.FULLBRIGHT) {
            ci.setReturnValue(15);
        }
    }

    /**
     * Removes lightupdates
     *
     * @param pos
     * @param checkNeighbors
     * @param ci
     */
    @Inject(method = "getLight(Lnet/minecraft/util/BlockPos;Z)I", at = @At("HEAD"), cancellable = true)
    private void getLight(BlockPos pos, boolean checkNeighbors, CallbackInfoReturnable<Integer> ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && Settings.FULLBRIGHT) {
            ci.setReturnValue(15);
        }
    }

    /**
     * @author Amplifiable
     * @reason Events
     */
    @Overwrite
    public void joinEntityInSurroundings(Entity entity) {
      int lvt_2_1_ = MathHelper.floor_double(entity.posX / 16.0D);
      int lvt_3_1_ = MathHelper.floor_double(entity.posZ / 16.0D);
      int lvt_4_1_ = 2;

      for(int lvt_5_1_ = lvt_2_1_ - lvt_4_1_; lvt_5_1_ <= lvt_2_1_ + lvt_4_1_; ++lvt_5_1_) {
        for(int lvt_6_1_ = lvt_3_1_ - lvt_4_1_; lvt_6_1_ <= lvt_3_1_ + lvt_4_1_; ++lvt_6_1_) {
          this.getChunkFromChunkCoords(lvt_5_1_, lvt_6_1_);
        }
      }

      if (!this.loadedEntityList.contains(entity)) {
        this.loadedEntityList.add(entity);
        EventBus.INSTANCE.post(new EntityJoinWorldEvent(entity));
      }
    }

  /**
   * @author Amplifiable
   * @reason Events
   */
  @Overwrite
  public boolean spawnEntityInWorld(Entity entity) {
    int lvt_2_1_ = MathHelper.floor_double(entity.posX / 16.0D);
    int lvt_3_1_ = MathHelper.floor_double(entity.posZ / 16.0D);
    boolean lvt_4_1_ = entity.forceSpawn;
    if (entity instanceof EntityPlayer) {
      lvt_4_1_ = true;
    }

    if (!lvt_4_1_ && !this.isChunkLoaded(lvt_2_1_, lvt_3_1_, true)) {
      return false;
    } else {
      if (entity instanceof EntityPlayer) {
        EntityPlayer lvt_5_1_ = (EntityPlayer)entity;
        this.playerEntities.add(lvt_5_1_);
        this.updateAllPlayersSleepingFlag();
      }

      EventBus.INSTANCE.post(new EntityJoinWorldEvent(entity));

      this.getChunkFromChunkCoords(lvt_2_1_, lvt_3_1_).addEntity(entity);
      this.loadedEntityList.add(entity);
      this.onEntityAdded(entity);
      return true;
    }
  }
  /**
   * @author Amplifiable
   * @reason Events
   */
  @Overwrite
  public void loadEntities(Collection<Entity> entityCollection) {
    this.loadedEntityList.addAll(entityCollection);

    for (Entity lvt_3_1_ : entityCollection) {
      EventBus.INSTANCE.post(new EntityJoinWorldEvent(lvt_3_1_));
      this.onEntityAdded(lvt_3_1_);
    }
  }
}
