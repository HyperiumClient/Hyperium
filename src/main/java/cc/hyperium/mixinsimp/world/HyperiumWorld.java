package cc.hyperium.mixinsimp.world;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.SpawnpointChangeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
