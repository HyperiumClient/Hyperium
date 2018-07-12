package cc.hyperium.mixinsimp.world;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EntityJoinWorldEvent;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.SpawnpointChangeEvent;
import cc.hyperium.mixins.world.IMixinWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

public class HyperiumWorld {

    private World parent;

    public HyperiumWorld(World parent) {
        this.parent = parent;
    }

    public void setSpawnPoint(BlockPos pos, CallbackInfo ci) {
        EventBus.INSTANCE.post(new SpawnpointChangeEvent(pos));
    }

    public void checkLightFor(EnumSkyBlock lightType, BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && Settings.FULLBRIGHT) {
            ci.setReturnValue(false);
        }
    }

    public void getLightFromNeighborsFor(EnumSkyBlock type, BlockPos pos, CallbackInfoReturnable<Integer> ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && Settings.FULLBRIGHT) {
            ci.setReturnValue(15);
        }
    }

    public double getHorizon(WorldInfo worldInfo) {
        if (Settings.VOID_FLICKER_FIX) {
            return 0.0;
        }
        return worldInfo.getTerrainType() == WorldType.FLAT ? 0.0D : 63.0D;
    }

    public void getLightFromNeighbor(BlockPos pos, CallbackInfoReturnable<Integer> ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && Settings.FULLBRIGHT) {
            ci.setReturnValue(15);
        }
    }

    public void getRawLight(BlockPos pos, EnumSkyBlock lightType, CallbackInfoReturnable<Integer> ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && Settings.FULLBRIGHT) {
            ci.setReturnValue(15);
        }
    }

    public void getLight(BlockPos pos, CallbackInfoReturnable<Integer> ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && Settings.FULLBRIGHT) {
            ci.setReturnValue(15);
        }
    }

    public void getLight(BlockPos pos, boolean checkNeighbors, CallbackInfoReturnable<Integer> ci) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && Settings.FULLBRIGHT) {
            ci.setReturnValue(15);
        }
    }

    public void joinEntityInSurroundings(Entity entity) {
        int lvt_2_1_ = MathHelper.floor_double(entity.posX / 16.0D);
        int lvt_3_1_ = MathHelper.floor_double(entity.posZ / 16.0D);
        int lvt_4_1_ = 2;

        for (int lvt_5_1_ = lvt_2_1_ - lvt_4_1_; lvt_5_1_ <= lvt_2_1_ + lvt_4_1_; ++lvt_5_1_) {
            for (int lvt_6_1_ = lvt_3_1_ - lvt_4_1_; lvt_6_1_ <= lvt_3_1_ + lvt_4_1_; ++lvt_6_1_) {
                parent.getChunkFromChunkCoords(lvt_5_1_, lvt_6_1_);
            }
        }

        if (!parent.loadedEntityList.contains(entity)) {
            parent.loadedEntityList.add(entity);
            EventBus.INSTANCE.post(new EntityJoinWorldEvent(entity));
        }
    }


    public boolean spawnEntityInWorld(Entity entity) {
        int lvt_2_1_ = MathHelper.floor_double(entity.posX / 16.0D);
        int lvt_3_1_ = MathHelper.floor_double(entity.posZ / 16.0D);
        boolean lvt_4_1_ = entity.forceSpawn;
        if (entity instanceof EntityPlayer) {
            lvt_4_1_ = true;
        }

        if (!lvt_4_1_ && !((IMixinWorld) parent).callIsChunkLoaded(lvt_2_1_, lvt_3_1_, true)) {
            return false;
        } else {
            if (entity instanceof EntityPlayer) {
                EntityPlayer lvt_5_1_ = (EntityPlayer) entity;
                parent.playerEntities.add(lvt_5_1_);
                parent.updateAllPlayersSleepingFlag();
            }

            EventBus.INSTANCE.post(new EntityJoinWorldEvent(entity));

            parent.getChunkFromChunkCoords(lvt_2_1_, lvt_3_1_).addEntity(entity);
            parent.loadedEntityList.add(entity);
            ((IMixinWorld) parent).callOnEntityAdded(entity);
            return true;
        }
    }
    public void loadEntities(Collection<Entity> entityCollection) {
        for (Entity lvt_3_1_ : entityCollection) {
            EventBus.INSTANCE.post(new EntityJoinWorldEvent(lvt_3_1_));
        }
    }
}
