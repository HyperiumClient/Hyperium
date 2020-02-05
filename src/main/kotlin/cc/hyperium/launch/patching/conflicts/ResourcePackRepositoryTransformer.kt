package cc.hyperium.launch.patching.conflicts

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class ResourcePackRepositoryTransformer : ConflictTransformer {
    override fun getClassName() = "bnm"

    override fun transform(original: ClassNode): ClassNode {
        for (field in original.fields) {
            if (field.name == "repositoryEntries") {
                field.access = Opcodes.ACC_PUBLIC
            }
        }

        return original
    }
}
