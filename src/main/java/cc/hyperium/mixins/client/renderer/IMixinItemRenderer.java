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

package cc.hyperium.mixins.client.renderer;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemRenderer.class)
public interface IMixinItemRenderer {
    @Invoker void callRotateArroundXAndY(float angle, float angleY);
    @Invoker void callSetLightMapFromPlayer(AbstractClientPlayer clientPlayer);
    @Invoker void callRotateWithPlayerRotations(EntityPlayerSP entityplayerspIn, float partialTicks);
    @Invoker void callRenderItemMap(AbstractClientPlayer clientPlayer, float pitch, float equipmentProgress, float swingProgress);
    @Invoker void callPerformDrinking(AbstractClientPlayer clientPlayer, float partialTicks);
    @Invoker void callDoBlockTransformations();
    @Invoker void callDoBowTransformations(float partialTicks, AbstractClientPlayer clientPlayer);
    @Invoker void callDoItemUsedTransformations(float swingProgress);
    @Invoker void callRenderPlayerArm(AbstractClientPlayer clientPlayer, float equipProgress, float swingProgress);
}
