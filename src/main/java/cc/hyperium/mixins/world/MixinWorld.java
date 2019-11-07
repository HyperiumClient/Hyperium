/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mixins.world;

import cc.hyperium.config.Settings;
import cc.hyperium.event.world.EntityJoinWorldEvent;
import cc.hyperium.event.EventBus;
import cc.hyperium.mixinsimp.world.HyperiumWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;

@Mixin(World.class)
public abstract class MixinWorld {

    @Shadow protected WorldInfo worldInfo;
    @Shadow @Final public List<Entity> loadedEntityList;
    @Shadow protected abstract void onEntityAdded(Entity entityIn);

    private HyperiumWorld hyperiumWorld = new HyperiumWorld();

    /**
     * Invoked once the server changes the players spawn point
     *
     * @param pos the new spawn position
     */
    @Inject(method = "setSpawnPoint", at = @At("HEAD"))
    private void setSpawnPoint(BlockPos pos, CallbackInfo ci) {
        hyperiumWorld.setSpawnPoint(pos);
    }

    /**
     * @author prplz/2pi
     * @reason VoidFlickerFix
     */
    @Overwrite
    public double getHorizon() {
        return hyperiumWorld.getHorizon(worldInfo);
    }

    @Inject(method = "checkLightFor", at = @At("HEAD"), cancellable = true)
    private void checkLightFor(CallbackInfoReturnable<Boolean> ci) {
        setLightValueBoolean(ci);
    }

    @Inject(method = "getLightFromNeighborsFor", at = @At("HEAD"), cancellable = true)
    private void getLightFromNeighborsFor(CallbackInfoReturnable<Integer> ci) {
        setLightValueInt(ci);
    }

    @Inject(method = "getLightFromNeighbors", at = @At("HEAD"), cancellable = true)
    private void getLightFromNeighbor(CallbackInfoReturnable<Integer> ci) {
        setLightValueInt(ci);
    }

    @Inject(method = "getRawLight", at = @At("HEAD"), cancellable = true)
    private void getRawLight(CallbackInfoReturnable<Integer> ci) {
        setLightValueInt(ci);
    }

    @Inject(method = "getLight(Lnet/minecraft/util/BlockPos;)I", at = @At("HEAD"), cancellable = true)
    private void getLightInteger(CallbackInfoReturnable<Integer> ci) {
        setLightValueInt(ci);
    }

    @Inject(method = "getLight(Lnet/minecraft/util/BlockPos;Z)I", at = @At("HEAD"), cancellable = true)
    private void getLightBoolean(CallbackInfoReturnable<Integer> ci) {
        setLightValueInt(ci);
    }

    private void setLightValueBoolean(CallbackInfoReturnable<Boolean> cir) {
        if (Settings.FULLBRIGHT && !Minecraft.getMinecraft().isIntegratedServerRunning()) cir.setReturnValue(false);
    }

    private void setLightValueInt(CallbackInfoReturnable<Integer> cir) {
        if (Settings.FULLBRIGHT && !Minecraft.getMinecraft().isIntegratedServerRunning()) cir.setReturnValue(15);
    }

    @Inject(method = "joinEntityInSurroundings", at = @At(remap = false, value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER))
    private void joinEntityInSurroundings(Entity entityIn, CallbackInfo ci) {
        EventBus.INSTANCE.post(new EntityJoinWorldEvent((World) (Object) this, entityIn));
    }

    @Inject(method = "spawnEntityInWorld", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/world/World;getChunkFromChunkCoords(II)Lnet/minecraft/world/chunk/Chunk;", shift = At.Shift.BEFORE))
    private void spawnEntityInWorld(Entity entityIn, CallbackInfoReturnable<Boolean> cir) {
        EventBus.INSTANCE.post(new EntityJoinWorldEvent((World) (Object) this, entityIn));
    }

    /**
     * @author asbyth
     * @reason Post event
     */
    @Overwrite
    public void loadEntities(Collection<Entity> entityCollection) {
        for (Entity entity : entityCollection) {
            EntityJoinWorldEvent event = new EntityJoinWorldEvent((World) (Object) this, entity);
            EventBus.INSTANCE.post(event);
            if (!event.isCancelled()) {
                loadedEntityList.add(entity);
                onEntityAdded(entity);
            }
        }
    }
}
