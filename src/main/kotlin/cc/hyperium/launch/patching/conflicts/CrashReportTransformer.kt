package cc.hyperium.launch.patching.conflicts

import cc.hyperium.commands.defaults.CommandDebug
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import codes.som.anthony.koffee.koffee
import com.chattriggers.ctjs.engine.ModuleManager
import net.minecraft.crash.CrashReport
import net.minecraft.crash.CrashReportCategory
import org.objectweb.asm.Handle
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import java.util.concurrent.Callable

class CrashReportTransformer : ConflictTransformer {
    override fun getClassName() = "b"

    override fun transform(original: ClassNode): ClassNode {
        original.version = 52
        original.koffee {
            method5(
                    public + static + synthetic,
                    "lambda\$populateEnvironment\$0",
                    String::class,
                    exceptions = arrayOf(Type.getType(Exception::class.java))
            ) {
                getstatic(ModuleManager::class, "INSTANCE", ModuleManager::class)
                invokevirtual(ModuleManager::class, "getCachedModules", List::class)
                invokevirtual(Object::class, "toString", String::class)
                areturn
                maxStack = 1
                maxLocals = 0
            }
        }

        for (method in original.methods) {
            if (method.name == "populateEnvironment") {
                val list = assembleBlock {
                    aload_0
                    getfield(CrashReport::class, "theReportCategory", CrashReportCategory::class)
                    ldc("ct.js modules")
                    invokedynamic(
                            "call",
                            Callable::class,
                            handle = Handle(
                                    Opcodes.H_INVOKESTATIC,
                                    "java/lang/invoke/LambdaMetafactory",
                                    "metafactory",
                                    "(Ljava/lang/invoke/MethodHandles\$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;"
                            ),
                            args = arrayOf(
                                    Type.getType("()Ljava/lang/Object;"),
                                    Handle(
                                            Opcodes.H_INVOKESTATIC,
                                            "net/minecraft/crash/CrashReport",
                                            "lambda\$populateEnvironment\$0",
                                            "()Ljava/lang/String;"
                                    ),
                                    Type.getType("()Ljava/lang/String;")
                            )
                    )
                    invokevirtual(
                            CrashReportCategory::class,
                            "addCrashSectionCallable",
                            void,
                            String::class,
                            Callable::class
                    )
                    aload_0
                    ldc("Affected level")
                    iconst_1
                    invokevirtual(
                            CrashReport::class,
                            "makeCategoryDepth",
                            CrashReportCategory::class,
                            String::class,
                            int
                    )
                    astore_1
                    aload_1
                    ldc("Hyperium Version")
                    new(StringBuilder::class)
                    dup
                    invokespecial(StringBuilder::class, "<init>", void)
                    invokestatic("cc/hyperium/Metadata", "getVersion", String::class)
                    invokevirtual(StringBuilder::class, "append", StringBuilder::class, String::class)
                    ldc(" (")
                    invokevirtual(StringBuilder::class, "append", StringBuilder::class, String::class)
                    invokestatic("cc/hyperium/Metadata", "getVersionID", int)
                    invokevirtual(StringBuilder::class, "append", StringBuilder::class, int)
                    ldc(")")
                    invokevirtual(StringBuilder::class, "append", StringBuilder::class, String::class)
                    invokevirtual(StringBuilder::class, "toString", String::class)
                    invokevirtual(CrashReportCategory::class, "addCrashSection", void, String::class, Object::class)
                    aload_1
                    ldc("Everything else")
                    invokestatic(CommandDebug::class, "get", String::class)
                    invokevirtual(CrashReportCategory::class, "addCrashSection", void, String::class, Object::class)
                }.first
                list.add(method.instructions)
                method.instructions = list
            }
        }
        return original
    }
}
