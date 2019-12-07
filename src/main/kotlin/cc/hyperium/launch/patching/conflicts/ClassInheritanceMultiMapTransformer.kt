package cc.hyperium.launch.patching.conflicts

import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import com.chattriggers.ctjs.utils.Utils
import com.google.common.collect.Iterators
import com.google.common.collect.UnmodifiableIterator
import com.google.common.collect.UnmodifiableListIterator
import net.minecraft.util.ClassInheritanceMultiMap
import org.objectweb.asm.Handle
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import java.lang.invoke.*

class ClassInheritanceMultiMapTransformer : ConflictTransformer {
    override fun getClassName() = "ne"

    override fun transform(original: ClassNode): ClassNode {
        for (method in original.methods) {
            if (method.name == "getByClass") {
                method.instructions = assembleBlock {
                    aload_0
                    aload_1
                    invokedynamic(
                        "iterator",
                        java.lang.Iterable::class,
                        ClassInheritanceMultiMap::class,
                        Class::class,
                        handle = Handle(
                            Opcodes.H_INVOKESTATIC,
                            "java/lang/invoke/LambdaMetafactory",
                            "metafactory",
                            "(Ljava/lang/invoke/MethodHandles\$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;"
                        ),
                        args = arrayOf(
                            Type.getType("()Ljava/util/Iterator;"),
                            Handle(
                                Opcodes.H_INVOKESPECIAL,
                                "net/minecraft/util/ClassInheritanceMultiMap",
                                "lambda\$getByClass\$0",
                                "(Ljava/lang/Class;)Ljava/util/Iterator;"
                            ),
                            Type.getType("()Ljava/util/Iterator;")
                        )
                    )
                    areturn
                }.first
            }

            if (method.name == "iterator") {
                method.instructions = assembleBlock {
                    aload_0
                    getfield(ClassInheritanceMultiMap::class, "values", List::class)
                    invokeinterface(List::class, "isEmpty", boolean)
                    ifeq(L["1"])
                    getstatic(Utils::class, "EMPTY_ITERATOR", UnmodifiableListIterator::class)
                    goto(L["2"])
                    +L["1"]
                    aload_0
                    getfield(ClassInheritanceMultiMap::class, "values", List::class)
                    invokeinterface(List::class, "iterator", java.util.Iterator::class)
                    invokestatic(Iterators::class, "unmodifiableIterator", UnmodifiableIterator::class, java.util.Iterator::class)
                    +L["2"]
                    areturn
                }.first
            }
        }

        return original
    }
}
