package cc.hyperium.mixinsimp.renderer.model;

import cc.hyperium.mixins.renderer.model.MixinModelPlayer;
import net.minecraft.client.model.ModelRenderer;

/**
 * Interface used to get the {@link ModelRenderer} parts of
 * {@link MixinModelPlayer}. A {@link MixinModelPlayer} object can be casted to
 * this interface. After that the methods can be called to get the
 * {@link ModelRenderer} parts.<br>
 * <br>
 * <b>NOTE</b><br>
 * This interface is not in the mixins package. Everything in
 * <i>cc.hyperium.mixins</i> will be removed at runtime. Thats why it can't be
 * in that package.
 */
public interface IMixinModelPlayer extends IMixinModelBiped {

    /* Right leg wrappers */
    ModelRenderer getBipedRightUpperLegwear();

    ModelRenderer getBipedRightLowerLegwear();

    /* Left leg wrappers */
    ModelRenderer getBipedLeftUpperLegwear();

    ModelRenderer getBipedLeftLowerLegwear();

    /* Right arm wrappers */
    ModelRenderer getBipedRightUpperArmwear();

    ModelRenderer getBipedRightForeArmwear();

    /* Left arm wrappers */
    ModelRenderer getBipedLeftUpperArmwear();

    ModelRenderer getBipedLeftForeArmwear();

    /* Body wrappers */
    ModelRenderer getBipedBodywear();

    ModelRenderer getCape();


    ModelRenderer getButt();
}
