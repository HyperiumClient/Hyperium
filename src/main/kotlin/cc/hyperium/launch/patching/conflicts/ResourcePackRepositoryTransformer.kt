package cc.hyperium.launch.patching.conflicts

import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import net.minecraft.client.resources.ResourcePackRepository
import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.IOFileFilter
import org.apache.commons.io.filefilter.TrueFileFilter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import java.io.File
import java.lang.Exception

class ResourcePackRepositoryTransformer : ConflictTransformer {
    override fun getClassName() = "bnm"

    override fun transform(original: ClassNode): ClassNode {
        for (field in original.fields) {
            if (field.name == "k") {
                field.access = Opcodes.ACC_PUBLIC
                break
            }
        }
        for (method in original.methods) {
            if (method.name == "i" && method.desc == "()V") {
                val block = assembleBlock {
                    guard {
                        aload_0
                        getfield(ResourcePackRepository::class, "f", File::class)
                        getstatic(TrueFileFilter::class, "TRUE", IOFileFilter::class)
                        aconst_null
                        invokestatic(FileUtils::class, "listFiles", Collection::class, File::class, IOFileFilter::class, IOFileFilter::class)
                    }.handle(Exception::class) {
                        astore_1
                        _return
                    }
                }
                // block.first.add(method.instructions)
                // method.instructions = block.first
                // method.tryCatchBlocks.addAll(block.second)
            }
        }
        return original
    }
}
