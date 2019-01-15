package cc.hyperium.mixinsimp.world;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.SpawnpointChangeEvent;
import cc.hyperium.mixins.world.IMixinWorld;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.util.ReportedException;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

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

    public void updateEntities(Profiler theProfiler, List<Entity> weatherEffects, List<Entity> loadedEntityList, List<Entity> unloadedEntityList, List<TileEntity> tickableTileEntities, WorldBorder worldBorder, List<TileEntity> loadedTileEntityList, List<TileEntity> tileEntitiesToBeRemoved, List<TileEntity> addedTileEntityList) {
        theProfiler.startSection("entities");
        theProfiler.startSection("global");

        for (int i = 0; i < weatherEffects.size(); ++i) {
            Entity entity = weatherEffects.get(i);

            try {
                ++entity.ticksExisted;
                entity.onUpdate();
            } catch (Throwable throwable2) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable2, "Ticking entity");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being ticked");

                entity.addEntityCrashInfo(crashreportcategory);

                throw new ReportedException(crashreport);
            }

            if (entity.isDead) {
                weatherEffects.remove(i--);
            }
        }

        theProfiler.endStartSection("remove");
        loadedEntityList.removeAll(unloadedEntityList);

        for (int k = 0; k < unloadedEntityList.size(); ++k) {
            Entity entity1 = unloadedEntityList.get(k);
            int j = entity1.chunkCoordX;
            int l1 = entity1.chunkCoordZ;

            if (entity1.addedToChunk && ((IMixinWorld) parent).callIsChunkLoaded(j, l1, true)) {
                parent.getChunkFromChunkCoords(j, l1).removeEntity(entity1);
            }
        }

        for (int l = 0; l < unloadedEntityList.size(); ++l) {
            ((IMixinWorld) parent).callOnEntityRemoved(unloadedEntityList.get(l));
        }


        unloadedEntityList.clear();
        updateDefaultEntities(theProfiler, loadedEntityList);
        theProfiler.startSection("blockEntities");
        ((IMixinWorld) parent).setProcessingLoadedTiles(true);
        Iterator<TileEntity> iterator = tickableTileEntities.iterator();

        while (iterator.hasNext()) {
            TileEntity tileentity = iterator.next();

            if (!tileentity.isInvalid() && tileentity.hasWorldObj()) {
                BlockPos blockpos = tileentity.getPos();

                if (parent.isBlockLoaded(blockpos) && worldBorder.contains(blockpos)) {
                    try {
                        ((ITickable) tileentity).update();
                    } catch (Throwable throwable) {
                        CrashReport crashreport2 = CrashReport.makeCrashReport(throwable, "Ticking block entity");
                        CrashReportCategory crashreportcategory1 = crashreport2.makeCategory("Block entity being ticked");
                        tileentity.addInfoToCrashReport(crashreportcategory1);
                        throw new ReportedException(crashreport2);
                    }
                }
            }

            if (tileentity.isInvalid()) {
                iterator.remove();
                loadedTileEntityList.remove(tileentity);

                if (parent.isBlockLoaded(tileentity.getPos())) {
                    parent.getChunkFromBlockCoords(tileentity.getPos()).removeTileEntity(tileentity.getPos());
                }
            }
        }

        ((IMixinWorld) parent).setProcessingLoadedTiles(false);

        if (!tileEntitiesToBeRemoved.isEmpty()) {
            tickableTileEntities.removeAll(tileEntitiesToBeRemoved);
            loadedTileEntityList.removeAll(tileEntitiesToBeRemoved);
            tileEntitiesToBeRemoved.clear();
        }

        theProfiler.endStartSection("pendingBlockEntities");

        if (!addedTileEntityList.isEmpty()) {
            for (int j1 = 0; j1 < addedTileEntityList.size(); ++j1) {
                TileEntity tileentity1 = addedTileEntityList.get(j1);

                if (!tileentity1.isInvalid()) {
                    if (!loadedTileEntityList.contains(tileentity1)) {
                        parent.addTileEntity(tileentity1);
                    }

                    if (parent.isBlockLoaded(tileentity1.getPos())) {
                        parent.getChunkFromBlockCoords(tileentity1.getPos()).addTileEntity(tileentity1.getPos(), tileentity1);
                    }

                    parent.markBlockForUpdate(tileentity1.getPos());
                }
            }

            addedTileEntityList.clear();
        }

        theProfiler.endSection();
        theProfiler.endSection();
    }

    private void updateDefaultEntities(Profiler theProfiler, List<Entity> loadedEntityList) {

        boolean improveEntityHandling = Settings.IMPROVE_ENTITY_HANDLING;
        if (improveEntityHandling) {
            theProfiler.endStartSection("hyperium_entity_async");
            boolean profilingEnabled = theProfiler.profilingEnabled;
            theProfiler.profilingEnabled = false;
            int total = loadedEntityList.size();
            int threads = total / 25 + 1;
            HashMap<Integer, List<Entity>> fx = new HashMap<>();
            int tmp = 0;
            for (int i = 0; i < threads; i++) {
                fx.computeIfAbsent(tmp, integer -> new ArrayList<>());
            }
            List<Integer> nulls = new ArrayList<>();
            for (Entity entity : loadedEntityList) {
                if (entity == null)
                    nulls.add(tmp);
                else
                    fx.computeIfAbsent(tmp, integer -> new ArrayList<>()).add(entity);
                tmp++;
                if (tmp > threads)
                    tmp = 0;
            }
            for (Integer aNull : nulls) {
                loadedEntityList.remove(aNull.intValue());
            }
            CountDownLatch latch = new CountDownLatch(fx.values().size());

            ConcurrentLinkedQueue<Entity> toRemove = new ConcurrentLinkedQueue<>();
            for (List<Entity> entityFXES : fx.values()) {
                Multithreading.runAsync(() -> {
                    try {
                        for (Entity entity : entityFXES) {
                            if (entity == null) {
                                System.out.println("Entity was null");
                                continue;
                            }
                            try {
                                if (entity.ridingEntity != null) {
                                    if (!entity.ridingEntity.isDead && entity.ridingEntity.riddenByEntity == entity) {
                                        continue;
                                    }

                                    entity.ridingEntity.riddenByEntity = null;
                                    entity.ridingEntity = null;
                                }
                                theProfiler.profilingEnabled = false;

                                updateEntity(entity);
                                if (entity.isDead) {
                                    toRemove.add(entity);
                                }
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                    latch.countDown();

                });
            }

            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (Entity entity : toRemove) {
                if (entity == null) {
                    System.out.println("Entity null");
                    continue;
                }
                int k1 = entity.chunkCoordX;
                int i2 = entity.chunkCoordZ;

                if (entity.addedToChunk && ((IMixinWorld) parent).callIsChunkLoaded(k1, i2, true)) {
                    parent.getChunkFromChunkCoords(k1, i2).removeEntity(entity);
                }
                loadedEntityList.remove(entity);
                ((IMixinWorld) parent).callOnEntityRemoved(entity);
            }
            theProfiler.profilingEnabled = profilingEnabled;
            theProfiler.endSection();
            return;

        }
        theProfiler.endStartSection("regular");
        for (int i1 = 0; i1 < loadedEntityList.size(); ++i1) {
            Entity entity2 = loadedEntityList.get(i1);

            if (entity2.ridingEntity != null) {
                if (!entity2.ridingEntity.isDead && entity2.ridingEntity.riddenByEntity == entity2) {
                    continue;
                }

                entity2.ridingEntity.riddenByEntity = null;
                entity2.ridingEntity = null;
            }

            theProfiler.startSection("tick");

            updateEntity(entity2);

            theProfiler.endSection();
            theProfiler.startSection("remove");

            if (entity2.isDead) {
                int k1 = entity2.chunkCoordX;
                int i2 = entity2.chunkCoordZ;

                if (entity2.addedToChunk && ((IMixinWorld) parent).callIsChunkLoaded(k1, i2, true)) {
                    parent.getChunkFromChunkCoords(k1, i2).removeEntity(entity2);
                }

                loadedEntityList.remove(i1--);
                ((IMixinWorld) parent).callOnEntityRemoved(entity2);
            }

            theProfiler.endSection();
        }
        theProfiler.endSection();


    }

    private void updateEntity(Entity entity) {
        if (!entity.isDead) {
            try {
                parent.updateEntity(entity);
            } catch (Throwable throwable1) {
                CrashReport crashreport1 = CrashReport.makeCrashReport(throwable1, "Ticking entity");
                CrashReportCategory crashreportcategory2 = crashreport1.makeCategory("Entity being ticked");
                entity.addEntityCrashInfo(crashreportcategory2);
                throw new ReportedException(crashreport1);
            }
        }
    }
}
