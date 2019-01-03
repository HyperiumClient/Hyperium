package cc.hyperium.mixinsimp.renderer.model;

import cc.hyperium.mixins.renderer.model.MixinModelBiped;
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
