package cc.hyperium.launch.patching.conflicts

import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import net.minecraft.client.resources.ResourcePackRepository
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import java.io.File

class ResourcePackRepositoryTransformer : ConflictTransformer {
    override fun getClassName() = "bnm"

    override fun transform(original: ClassNode): ClassNode {
        original.fields.find {
            it.name == "repositoryEntries"
        }?.apply {
            access = Opcodes.ACC_PUBLIC
        }

        original.methods.find {
            it.name == "deleteOldServerResourcesPacks"
        }?.apply {
            val (createDirectory) = assembleBlock {
                aload_0
                getfield(ResourcePackRepository::class, "dirServerResourcepacks", File::class)
                invokevirtual(File::class, "exists", boolean)
                ifne(L["1"])
                aload_0
                getfield(ResourcePackRepository::class, "dirServerResourcepacks", File::class)
                invokevirtual(File::class, "mkdirs", boolean)
                pop
                +L["1"]
            }

            instructions.insertBefore(instructions.first, createDirectory)
        }

        return original
    }
}
