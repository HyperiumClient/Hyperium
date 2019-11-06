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

package cc.hyperium.mixins.client.renderer.entity;

import cc.hyperium.config.Settings;
import cc.hyperium.mixinsimp.client.renderer.entity.HyperiumRenderItem;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * A Mixin to the RenderItem class to provide ShinyPots support, not to be confused with the
 * ItemRenderer class, this class is entirely different
 *
 * @author boomboompower
 */
@Mixin(RenderItem.class)
public abstract class MixinRenderItem implements IResourceManagerReloadListener {

    private HyperiumRenderItem hyperiumRenderItem = new HyperiumRenderItem((RenderItem) (Object) this);
    private boolean wasJustRenderedAsPotion = false; // used to stop pots from rendering twice
    private boolean isInv = false; // can't pass arguments between 2 @Injects so we have to do this

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/renderer/tileentity/TileEntityItemStackRenderer;renderByItem(Lnet/minecraft/item/ItemStack;)V"))
    private void onRenderItem1(ItemStack stack, IBakedModel model, CallbackInfo ci) {
        hyperiumRenderItem.callHeadScale(stack, isInv, Settings.HEAD_SCALE_FACTOR);
    }

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderModel(Lnet/minecraft/client/resources/model/IBakedModel;Lnet/minecraft/item/ItemStack;)V"))
    private void onRenderItem2(ItemStack stack, IBakedModel model, CallbackInfo ci) {
        wasJustRenderedAsPotion = hyperiumRenderItem.renderShinyPot(stack, model, isInv);
        hyperiumRenderItem.callHeadScale(stack, isInv, Settings.HEAD_SCALE_FACTOR);
    }

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/renderer/GlStateManager;popMatrix()V"))
    private void onRenderItem3(ItemStack stack, IBakedModel model, CallbackInfo ci) {
        hyperiumRenderItem.callHeadScale(stack, isInv, 1.0 / Settings.HEAD_SCALE_FACTOR);
    }

    @Inject(method = "renderEffect", at = @At("HEAD"), cancellable = true)
    private void onRenderEffect(CallbackInfo ci) {
        if (wasJustRenderedAsPotion) {
            ci.cancel();
        }
        wasJustRenderedAsPotion = false; // cancel() != return so this still gets executed
    }

    @Inject(method = "renderItemIntoGUI", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V"))
    private void onRenderItemIntoGUI(CallbackInfo ci) {
        isInv = true;
    }

    @Inject(method = "renderItemIntoGUI", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/resources/model/IBakedModel;)V"))
    private void onRenderItemIntoGUIAfter(CallbackInfo ci) {
        isInv = false;
    }
}

