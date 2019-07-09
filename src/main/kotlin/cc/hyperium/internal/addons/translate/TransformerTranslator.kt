package cc.hyperium.internal.addons.translate

import cc.hyperium.internal.addons.AddonManifest
import net.minecraft.launchwrapper.Launch

class TransformerTranslator : ITranslator {
    override fun translate(manifest: AddonManifest) {
        val transformerClass = manifest.transformerClass ?: return
        Launch.classLoader.registerTransformer(transformerClass)
    }
}
