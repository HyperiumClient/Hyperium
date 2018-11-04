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
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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



}
