/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.event;

import cc.hyperium.Hyperium;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.RETURN;

/**
 * Used to store information about events and index them so they can be easily accessed by ASM
 */
public final class EventSubscriber {
    private static int ID = 0;
    private static final AsmClassLoader LOADER = new AsmClassLoader();

    @NotNull
    private final Object instance;
    @NotNull
    private final Method method;
    @NotNull
    private final Priority priority;
    private String objName;
    private String methodName;

    // ASM manufactured event handler.
    private EventHandler handler;

    public EventSubscriber(@NotNull Object instance, @NotNull Method method, @NotNull Priority priority) {
        Preconditions.checkNotNull(instance, "instance cannot be null");
        Preconditions.checkNotNull(method, "method cannot be null");
        Preconditions.checkNotNull(priority, "priority cannot be null");

        this.instance = instance;
        this.method = method;
        this.priority = priority;
        objName = this.instance.getClass().getSimpleName().replace(".", "_");
        methodName = this.method.getName();

        try {
            handler = (EventHandler) createHandler(method).getConstructor(Object.class).newInstance(instance);
        } catch (Exception e) {
            Hyperium.LOGGER.error("Failed to register event handler {}", method);
            e.printStackTrace();
        }
    }

    public void invoke(Object event) {
        handler.handle(event);
    }

    public String getObjName() {
        return objName;
    }

    @NotNull
    public final Object getInstance() {
        return instance;
    }

    @NotNull
    public final Method getMethod() {
        return method;
    }

    @NotNull
    public final Priority getPriority() {
        return priority;
    }

    @NotNull
    public final EventSubscriber copy(@NotNull Object instance, @NotNull Method method, @NotNull Priority priority) {
        Preconditions.checkNotNull(instance, "instance");
        Preconditions.checkNotNull(method, "method");
        Preconditions.checkNotNull(priority, "priority");

        return new EventSubscriber(instance, method, priority);
    }

    public String getMethodName() {
        return methodName;
    }

    @Override
    public String toString() {
        return "EventSubscriber(instance=" + instance + ", method=" + method + ", priority=" + priority + ")";
    }

    @Override
    public int hashCode() {
        return (instance.hashCode() * 31 + method.hashCode()) * 31 + priority.hashCode();
    }

    @Override
    public boolean equals(Object subscriberIn) {
        if (this != subscriberIn) {
            if (subscriberIn instanceof EventSubscriber) {
                EventSubscriber eventSubscriber = (EventSubscriber) subscriberIn;

                return instance.equals(eventSubscriber.instance) &&
                        method.equals(eventSubscriber.method) &&
                        priority.equals(eventSubscriber.priority);
            }

            return false;
        } else {
            return true;
        }

    }

    private Class<?> createHandler(Method callback) {
        // ClassName$methodName_EventClass_XXX
        String name = objName + "$" + callback.getName() + "_" + callback.getParameters()[0].getType().getSimpleName() + "_" + (ID++);
        String eventType = Type.getInternalName(callback.getParameterTypes()[0]);

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        MethodVisitor mv;
        String desc = name.replace(".", "/");
        String instanceClassName = instance.getClass().getName().replace(".", "/");

        cw.visit(V1_6, ACC_PUBLIC | ACC_SUPER, desc, null, "java/lang/Object", new String[]{ "cc/hyperium/event/EventSubscriber$EventHandler" });

        cw.visitSource(".dynamic", null);
        {
            cw.visitField(ACC_PUBLIC, "instance", "Ljava/lang/Object;", null, null).visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/Object;)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitFieldInsn(PUTFIELD, desc, "instance", "Ljava/lang/Object;");
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        {
            mv = cw.visitMethod(ACC_PUBLIC, "handle", "(Ljava/lang/Object;)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, desc, "instance", "Ljava/lang/Object;");
            mv.visitTypeInsn(CHECKCAST, instanceClassName);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitTypeInsn(CHECKCAST, eventType);
            mv.visitMethodInsn(INVOKEVIRTUAL, instanceClassName, callback.getName(), Type.getMethodDescriptor(callback), false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        cw.visitEnd();

        byte[] handlerClassBytes = cw.toByteArray();
        return LOADER.define(name, handlerClassBytes);
    }

    public interface EventHandler {
        void handle(Object event);
    }

    private static class AsmClassLoader extends ClassLoader {
        private AsmClassLoader()
        {
            super(AsmClassLoader.class.getClassLoader());
        }

        public Class<?> define(String name, byte[] data)
        {
            return defineClass(name, data, 0, data.length);
        }
    }
}
