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

import cc.hyperium.mixinsimp.world.HyperiumWorld;
import net.minecraft.entity.Entity;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IntHashMap;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
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

import java.util.List;

@Mixin(World.class)
public abstract class MixinWorld {

    @Shadow
    @Final
    public List<Entity> loadedEntityList;

    @Shadow
    @Final
    public Profiler theProfiler;

    @Shadow
    @Final
    public List<Entity> weatherEffects;

    @Shadow
    @Final
    public List<TileEntity> tickableTileEntities;

    @Shadow
    @Final
    public List<TileEntity> loadedTileEntityList;

    @Shadow
    @Final
    protected List<Entity> unloadedEntityList;

    @Shadow
    @Final
    protected IntHashMap<Entity> entitiesById;

    @Shadow
    private WorldInfo worldInfo;

    @Shadow
    private boolean processingLoadedTiles;

    @Shadow
    @Final
    private WorldBorder worldBorder;

    @Shadow
    @Final
    private List<TileEntity> tileEntitiesToBeRemoved;

    @Shadow
    @Final
    private List<TileEntity> addedTileEntityList;

    private HyperiumWorld hyperiumWorld = new HyperiumWorld((World) (Object) this);

    /**
     * Invoked once the server changes the players spawn point
     *
     * @param pos the new spawn position
     * @param ci  {@see org.spongepowered.asm.mixin.injection.callback.CallbackInfo}
     */
    @Inject(method = "setSpawnPoint", at = @At("HEAD"))
    private void setSpawnPoint(BlockPos pos, CallbackInfo ci) {
        hyperiumWorld.setSpawnPoint(pos, ci);
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
        hyperiumWorld.checkLightFor(lightType, pos, ci);
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
        hyperiumWorld.getLightFromNeighborsFor(type, pos, ci);
    }

    /**
     * @author prplz/2pi
     * @reason VoidFlickerFix
     */
    @Overwrite
    public double getHorizon() {
        return hyperiumWorld.getHorizon(worldInfo);
    }

    /**
     * Removes lightupdates
     *
     * @param pos
     * @param ci
     */
    @Inject(method = "getLightFromNeighbors", at = @At("HEAD"), cancellable = true)
    private void getLightFromNeighbor(BlockPos pos, CallbackInfoReturnable<Integer> ci) {
        hyperiumWorld.getLightFromNeighbor(pos, ci);
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
        hyperiumWorld.getRawLight(pos, lightType, ci);
    }

    /**
     * Removes lightupdates
     *
     * @param pos
     * @param ci
     */
    @Inject(method = "getLight(Lnet/minecraft/util/BlockPos;)I", at = @At("HEAD"), cancellable = true)
    private void getLight(BlockPos pos, CallbackInfoReturnable<Integer> ci) {
        hyperiumWorld.getLight(pos, ci);
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
        hyperiumWorld.getLight(pos, checkNeighbors, ci);
    }

    /**
     * @author Sk1er
     * @reason Make async option
     */
    @Overwrite
    public void updateEntities() {
        hyperiumWorld.updateEntities(theProfiler, weatherEffects, loadedEntityList, unloadedEntityList, tickableTileEntities, worldBorder, loadedTileEntityList, tileEntitiesToBeRemoved, addedTileEntityList);
    }
}
