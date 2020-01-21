package cc.hyperium.launch.patching.conflicts

import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import net.minecraft.client.resources.ResourcePackRepository
import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.IOFileFilter
import org.apache.commons.io.filefilter.TrueFileFilter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.LabelNode
import org.objectweb.asm.tree.TryCatchBlockNode
import java.io.File

class ResourcePackRepositoryTransformer : ConflictTransformer {
    override fun getClassName() = "bnm"

    override fun transform(original: ClassNode): ClassNode {
        for (field in original.fields) {
            if (field.name == "repositoryEntries") {
                field.access = Opcodes.ACC_PUBLIC
            }
        }

        for (method in original.methods) {
            if (method.name == "deleteOldServerResourcesPacks") {
                val listFiles = assembleBlock {
                    tryCatchBlocks.add(TryCatchBlockNode(LabelNode(), LabelNode(), LabelNode(), "java/lang/Exception"))
                    +L["0"]
                    aload_0
                    getfield(ResourcePackRepository::class, "dirServerResourcepacks", File::class)
                    getstatic(TrueFileFilter::class, "TRUE", IOFileFilter::class)
                    aconst_null
                    invokestatic(
                        FileUtils::class,
                        "listFiles",
                        Collection::class,
                        IOFileFilter::class,
                        IOFileFilter::class
                    )
                    pop
                    +L["1"]
                    goto(L["3"])
                    +L["2"]
                    astore_1
                    _return
                }.first

                method.instructions.insertBefore(method.instructions.first, listFiles)
            }
        }

        return original
    }
}
