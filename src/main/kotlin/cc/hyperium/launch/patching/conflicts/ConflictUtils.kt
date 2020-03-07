package cc.hyperium.launch.patching.conflicts

import codes.som.anthony.koffee.ClassAssembly
import codes.som.anthony.koffee.MethodAssembly
import codes.som.anthony.koffee.modifiers.Modifiers
import codes.som.anthony.koffee.types.TypeLike
import org.objectweb.asm.Opcodes.ASM5
import org.objectweb.asm.Type
import org.objectweb.asm.tree.MethodNode

/**
 * We want to use ASM5 as that's what Launchwrapper uses.
 * Literally just the normal method but with ASM 5 instead of ASM 7
 */
fun ClassAssembly.method5(
    access: Modifiers, name: String, returnType: TypeLike, vararg parameterTypes: TypeLike,
    signature: String? = null, exceptions: Array<Type>? = null,
    routine: MethodAssembly.() -> Unit
): MethodNode {
    val descriptor = Type.getMethodDescriptor(coerceType(returnType), *parameterTypes.map(::coerceType).toTypedArray())

    val methodNode = MethodNode(
        ASM5,
        access.access,
        name,
        descriptor,
        signature,
        exceptions?.map { it.internalName }?.toTypedArray()
    )
    val methodAssembly = MethodAssembly(methodNode)
    routine(methodAssembly)

    node.methods.add(methodNode)

    return methodNode
}
