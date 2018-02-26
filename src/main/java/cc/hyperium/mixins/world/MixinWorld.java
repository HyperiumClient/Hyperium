/*
 * Hyperium Client, Free client with huds and popular mod
 *     Copyright (C) 2018  Hyperium Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mixins.world;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.SpawnpointChangeEvent;
import cc.hyperium.gui.settings.items.GeneralSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static cc.hyperium.gui.settings.items.GeneralSetting.fullbrightEnabled;

@Mixin(World.class)
public class MixinWorld {

    @Shadow protected WorldInfo worldInfo;

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
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && fullbrightEnabled) {
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
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && fullbrightEnabled) {
            ci.setReturnValue(15);
        }
    }

    /**
     * Fixes Void Flicker
     * @author prplz/2pi
     */
    @Overwrite
    public double getHorizon() {
        if(GeneralSetting.voidflickerfixEnabled) {
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
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && fullbrightEnabled) {
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
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && fullbrightEnabled) {
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
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && fullbrightEnabled) {
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
        if (!Minecraft.getMinecraft().isIntegratedServerRunning() && fullbrightEnabled) {
            ci.setReturnValue(15);
        }
    }
}
