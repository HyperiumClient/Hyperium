package cc.hyperium.mixinsimp.renderer;

import cc.hyperium.mixins.renderer.MixinModelPlayer;
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
public interface IMixinModelPlayer {

	/* Right leg wrappers */
	ModelRenderer getBipedRightUpperLeg();

	ModelRenderer getBipedRightUpperLegwear();

	ModelRenderer getBipedRightLowerLeg();

	ModelRenderer getBipedRightLowerLegwear();

	/* Left leg wrappers */
	ModelRenderer getBipedLeftUpperLeg();

	ModelRenderer getBipedLeftUpperLegwear();

	ModelRenderer getBipedLeftLowerLeg();

	ModelRenderer getBipedLeftLowerLegwear();

	/* Right arm wrappers */
	ModelRenderer getBipedRightUpperArm();

	ModelRenderer getBipedRightUpperArmwear();

	ModelRenderer getBipedRightForeArm();

	ModelRenderer getBipedRightForeArmwear();

	/* Left arm wrappers */
	ModelRenderer getBipedLeftUpperArm();

	ModelRenderer getBipedLeftUpperArmwear();

	ModelRenderer getBipedLeftForeArm();

	ModelRenderer getBipedLeftForeArmwear();

	/* Body wrappers */
	ModelRenderer getBipedBody();

	ModelRenderer getBipedBodywear();

	/* Head wrappers */
	ModelRenderer getBipedHead();

	ModelRenderer getBipedHeadwear();
}
