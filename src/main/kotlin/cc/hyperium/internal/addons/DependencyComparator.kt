package cc.hyperium.internal.addons

class DependencyComparator : Comparator<AddonManifest> {

    override fun compare(o1: AddonManifest?, o2: AddonManifest?): Int = when {
        o1!!.dependencies.contains(o2!!.name) -> 1
        o2.dependencies.contains(o1.name) -> -1
        else -> 0
    }
}