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
        int x = MathHelper.floor_double(entity.posX / 16.0D);
        int z = MathHelper.floor_double(entity.posZ / 16.0D);

        boolean forceSpawned = entity.forceSpawn;

        if (entity instanceof EntityPlayer) {
            forceSpawned = true;
        }

        if (!forceSpawned && !((IMixinWorld) parent).callIsChunkLoaded(x, z, true)) {
            return false;
        } else {
            if (entity instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer) entity;
                parent.playerEntities.add(entityplayer);
                parent.updateAllPlayersSleepingFlag();
            }

            EventBus.INSTANCE.post(new EntityJoinWorldEvent(entity));

            parent.getChunkFromChunkCoords(x, z).addEntity(entity);
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

//    public void removeEntity(Entity entity) {
//        if (entity == null)
//            return;
//        if (entity instanceof EntityPlayer) {
//            Hyperium.INSTANCE.getScheduler().schedule(2, () -> {
//                if (entity.equals(Minecraft.getMinecraft().thePlayer)) {
//                    return;
//                }
//                if(Minecraft.getMinecraft().theWorld == null){
//                    return;
//                }
//                NetworkPlayerInfo networkPlayerInfo = ((IMixinAbstractClientPlayer) ((AbstractClientPlayer) entity)).callGetPlayerInfo();
//                if (networkPlayerInfo == null)
//                    return;
//                if (Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().contains(networkPlayerInfo)) {
//                    return;
//                }
//                //Check to see if someone else has the same skin as the entity that left to stop unloading when someone still has it.
//                for (NetworkPlayerInfo playerInfo : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
//                    if(playerInfo !=null && playerInfo.hasLocationSkin() && playerInfo.getLocationCape().equals(networkPlayerInfo.getLocationCape()))
//                        return;
//                }
//
//                ((IMixinNetworkPlayerInfo) networkPlayerInfo).setPlayerTexturesLoaded(false);
//                ((IMixinNetworkPlayerInfo) networkPlayerInfo).setLocationCape(null);
//                ((IMixinNetworkPlayerInfo) networkPlayerInfo).setLocationSkin(null);
//
//                ResourceLocation locationSkin = ((AbstractClientPlayer) entity).getLocationSkin();
//
//                if (locationSkin != null) {
//                    Minecraft.getMinecraft().getTextureManager().deleteTexture(locationSkin);
//                }
//                ResourceLocation locationCape = ((AbstractClientPlayer) entity).getLocationCape();
//                if (locationCape != null) {
//                    CapeHandler capeHandler = Hyperium.INSTANCE.getHandlers().getCapeHandler();
//                    ResourceLocation cape = capeHandler.getCape(((AbstractClientPlayer) entity));
//                    if (cape != null && cape.equals(locationCape))
//                        return;
//                    Minecraft.getMinecraft().getTextureManager().deleteTexture(locationCape);
//                }
//            });
//
//        }
//    }
}
