package cc.hyperium.launch.patching.conflicts

import org.objectweb.asm.tree.ClassNode

class GuiMainMenuTransformer : ConflictTransformer {
    override fun getClassName() = "aya"

    override fun transform(original: ClassNode): ClassNode {
        return original
    }
}
