package cc.hyperium.event;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

/**
 * Used to store information about events and index them so they can be easily accessed by ASM
 */
public final class EventSubscriber {

    @NotNull
    private final Object instance;
    @NotNull
    private final Method method;
    @NotNull
    private final Priority priority;
    private String objName;
    private String methodName;

    public EventSubscriber(@NotNull Object instance, @NotNull Method method, @NotNull Priority priority) {
        Preconditions.checkNotNull(instance, "instance cannot be null");
        Preconditions.checkNotNull(method, "method cannot be null");
        Preconditions.checkNotNull(priority, "priority cannot be null");

        this.instance = instance;
        this.method = method;
        this.priority = priority;
        this.objName = getInstance().getClass().getSimpleName().replace(".", "_");
        methodName = getMethod().getName();
    }

    public String getObjName() {
        return objName;
    }

    @NotNull
    public final Object getInstance() {
        return this.instance;
    }

    @NotNull
    public final Method getMethod() {
        return this.method;
    }

    @NotNull
    public final Priority getPriority() {
        return this.priority;
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
        return "EventSubscriber(instance=" + this.instance + ", method=" + this.method + ", priority=" + this.priority + ")";
    }

    @Override
    public int hashCode() {
        return (this.instance.hashCode() * 31 + this.method.hashCode()) * 31 + this.priority.hashCode();
    }

    @Override
    public boolean equals(Object subscriberIn) {
        if (this != subscriberIn) {
            if (subscriberIn instanceof EventSubscriber) {
                EventSubscriber eventSubscriber = (EventSubscriber) subscriberIn;

                return this.instance.equals(eventSubscriber.instance) &&
                        this.method.equals(eventSubscriber.method) &&
                        this.priority.equals(eventSubscriber.priority);
            }

            return false;
        } else {
            return true;
        }

    }
}
