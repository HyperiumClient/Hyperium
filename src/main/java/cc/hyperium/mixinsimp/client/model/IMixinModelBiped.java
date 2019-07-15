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

package cc.hyperium.mixinsimp.client.model;

import cc.hyperium.mixins.client.model.MixinModelBiped;
import net.minecraft.client.model.ModelRenderer;

/**
 * Interface used to get the {@link ModelRenderer} parts of
 * {@link MixinModelBiped}. A {@link MixinModelBiped} object can be casted to
 * this interface. After that the methods can be called to get the
 * {@link ModelRenderer} parts.<br>
 * <br>
 * <b>NOTE</b><br>
 * This interface is not in the mixins package. Everything in
 * <i>cc.hyperium.mixins</i> will be removed at runtime. Thats why it can't be
 * in that package.
 */
public interface IMixinModelBiped {

    /* Right leg wrappers */
    ModelRenderer getBipedRightUpperLeg();

    ModelRenderer getBipedRightLowerLeg();

    /* Left leg wrappers */
    ModelRenderer getBipedLeftUpperLeg();

    ModelRenderer getBipedLeftLowerLeg();

    /* Right arm wrappers */
    ModelRenderer getBipedRightUpperArm();

    ModelRenderer getBipedRightForeArm();

    /* Left arm wrappers */
    ModelRenderer getBipedLeftUpperArm();

    ModelRenderer getBipedLeftForeArm();

    /* Body wrappers */
    ModelRenderer getBipedBody();

    /* Head wrappers */
    ModelRenderer getBipedHead();

    ModelRenderer getBipedHeadwear();


}
