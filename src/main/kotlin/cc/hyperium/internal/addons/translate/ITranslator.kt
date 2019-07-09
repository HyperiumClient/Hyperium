package cc.hyperium.internal.addons.translate

import cc.hyperium.internal.addons.AddonManifest


interface ITranslator {

    fun translate(manifest: AddonManifest)

}