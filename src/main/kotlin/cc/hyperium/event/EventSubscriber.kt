package cc.hyperium.event

import cc.hyperium.Hyperium
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type
import java.lang.reflect.Method

class EventSubscriber(val instance: Any, val method: Method, val priority: Priority) {

    var id = 0
    val loader: AsmClassLoader = AsmClassLoader()
    private val objName = instance.javaClass.simpleName.replace(".", "_")
    lateinit var handler: EventHandler

    init {
        try {
            handler = createHandler(method).getConstructor(Object::class.java).newInstance(instance) as EventHandler
        } catch (e: Exception) {
            Hyperium.LOGGER.error("Failed to register event handler $method")
            e.printStackTrace()
        }
    }

    fun invoke(event: Any) {
        handler.handle(event)
    }

    val methodName: String
        get() = method.name

    fun copy(instance: Any, method: Method, priority: Priority): EventSubscriber =
            EventSubscriber(instance, method, priority)

    override fun toString() = "EventSubscriber(instance=$instance, method = $method, priority = $priority"

    override fun equals(other: Any?): Boolean {
        return if (this !== other) {
            if (other is EventSubscriber) {
                val subscriber: EventSubscriber = other

                instance == subscriber.instance &&
                        method == subscriber.method &&
                        priority == subscriber.priority
            } else false
        } else {
            true
        }
    }

    private fun createHandler(callback: Method): Class<*> {
        val name = "$objName$${callback.name}_${callback.parameters[0].type.simpleName}_${id++}"
        val eventType = Type.getInternalName(callback.parameterTypes[0])

        val cw = ClassWriter(ClassWriter.COMPUTE_FRAMES)
        var mv: MethodVisitor
        val desc = name.replace(".", "/")
        val instanceClassName = instance.javaClass.name.replace(".", "/")

        cw.visit(
                V1_6,
                ACC_PUBLIC or ACC_SUPER,
                desc,
                null,
                "java/lang/Object",
                arrayOf("cc/hyperium/event/EventSubscriber\$EventHandler")
        )

        cw.visitSource(".dynamic", null)
        run {
            cw.visitField(ACC_PUBLIC, "instance", "Ljava/lang/Object;", null, null).visitEnd()
        }

        run {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/Object;)V", null, null)
            mv.visitCode()
            mv.visitVarInsn(ALOAD, 0)
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
            mv.visitVarInsn(ALOAD, 0)
            mv.visitVarInsn(ALOAD, 1)
            mv.visitFieldInsn(PUTFIELD, desc, "instance", "Ljava/lang/Object;")
            mv.visitInsn(RETURN)
            mv.visitMaxs(2, 2)
            mv.visitEnd()
        }

        run {
            mv = cw.visitMethod(ACC_PUBLIC, "handle", "(Ljava/lang/Object;)V", null, null)
            mv.visitCode()
            mv.visitVarInsn(ALOAD, 0)
            mv.visitFieldInsn(GETFIELD, desc, "instance", "Ljava/lang/Object;")
            mv.visitTypeInsn(CHECKCAST, instanceClassName)
            mv.visitVarInsn(ALOAD, 1)
            mv.visitTypeInsn(CHECKCAST, eventType)
            mv.visitMethodInsn(
                    INVOKEVIRTUAL,
                    instanceClassName,
                    callback.name,
                    Type.getMethodDescriptor(callback),
                    false
            )
            mv.visitInsn(RETURN)
            mv.visitMaxs(2, 2)
            mv.visitEnd()
        }

        cw.visitEnd()

        val handlerClassBytes = cw.toByteArray()
        return loader.define(name, handlerClassBytes)
    }

    override fun hashCode(): Int = (instance.hashCode() * 31 + method.hashCode()) * 31 + priority.hashCode()


    class AsmClassLoader : ClassLoader(AsmClassLoader::class.java.classLoader) {
        fun define(name: String, data: ByteArray): Class<*> {
            return defineClass(name, data, 0, data.size)
        }
    }

    interface EventHandler {
        fun handle(event: Any)
    }
}
