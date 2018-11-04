package cc.hyperium.event;

import com.google.common.reflect.TypeToken;
import net.minecraft.client.Minecraft;
import net.minecraft.profiler.Profiler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("UnstableApiUsage")
public class EventBus {

    public static final EventBus INSTANCE = new EventBus();

    private HashMap<Class<?>, CopyOnWriteArrayList<EventSubscriber>> subscriptions = new HashMap<>();

    /**
     * Registers all methods of a class into the event system with
     * the {@link package me.kbrewster.blazeapi.api.event.InvokeEvent} annotation
     *
     * @param obj An instance of the class which you would like to register as an event
     */
    public void register(Object obj) {
        // also contains the class itself
        TypeToken<?> token = TypeToken.of(obj.getClass());

        Set superClasses = token.getTypes().rawTypes();

        // we also want to loop over the super classes, since declaredMethods only gets method in the class itself
        for (Object temp : superClasses) {
            Class<?> clazz = (Class<?>) temp;

            // iterates though all the methods in the class
            for (Method method : clazz.getDeclaredMethods()) {
                // all the information and error checking before the method is added such
                // as if it even is an event before the element even touches the HashMap
                if (method.getAnnotation(InvokeEvent.class) == null) {
                    continue;
                }

                if (method.getParameters()[0] == null) {
                    throw new IllegalArgumentException("Couldn't find parameter inside of " + method.getName() + "!");
                }

                Class<?> event = method.getParameters()[0].getType();

                Priority priority = method.getAnnotation(InvokeEvent.class).priority();
                method.setAccessible(true);

                // where the method gets added to the event key inside of the subscription hashmap
                // the arraylist is either sorted or created before the element is added
                if (this.subscriptions.containsKey(event)) {
                    // sorts array on insertion
                    this.subscriptions.get(event).add(new EventSubscriber(obj, method, priority));
                    this.subscriptions.get(event).sort(Comparator.comparingInt(a -> a.getPriority().value));
                } else {
                    // event hasn't been added before so it creates a new instance
                    // sorting does not matter here since there is no other elements to compete against
                    this.subscriptions.put(event, new CopyOnWriteArrayList<>());
                    this.subscriptions.get(event).add(new EventSubscriber(obj, method, priority));
                    this.subscriptions.get(event).sort(Comparator.comparingInt(a -> a.getPriority().value));
                }
            }
        }
    }

    /**
     * Unregisters all methods of the class instance from the event system
     * inside of {@link #subscriptions}
     *
     * @param obj An instance of the class which you would like to register as an event
     */
    public void unregister(Object obj) {
        this.subscriptions.values().forEach(map -> map.removeIf(it -> it.getInstance() == obj));
    }

    /**
     * Unregisters all methods of the class from the event system
     * inside of {@link #subscriptions}
     *
     * @param clazz An instance of the class which you would like to register as an event
     */
    public void unregister(Class<?> clazz) {
        this.subscriptions.values().forEach(map -> map.removeIf(it -> it.getInstance().getClass() == clazz));
    }


    /**
     * Invokes all of the methods which are inside of the classes
     * registered to the event
     *
     * @param event Event that is being posted
     */


    /**
     * Invokes all of the methods which are inside of the classes
     * registered to the event
     *
     * @param event Event that is being posted
     */
    public void post(Object event) {
        if (event == null) {
            return;
        }

        boolean profile = Minecraft.getMinecraft().isCallingFromMinecraftThread();
        Profiler mcProfiler = Minecraft.getMinecraft().mcProfiler;
        if (profile) {
            mcProfiler.startSection(event.getClass().getSimpleName());
        }
        this.subscriptions.getOrDefault(event.getClass(), new CopyOnWriteArrayList<>()).forEach((sub) -> {
            if (profile) {
                String name = sub.getObjName();
                mcProfiler.startSection(name);
                mcProfiler.startSection(sub.getMethodName());
            }
            try {
                sub.getMethod().invoke(sub.getInstance(), event);
            } catch (Exception e) {
                if (e instanceof InvocationTargetException) {
                    ((InvocationTargetException) e).getTargetException().printStackTrace();
                }
                e.printStackTrace();
            }
            if (profile) {
                mcProfiler.endSection();
                mcProfiler.endSection();
            }
        });
        if (profile)
            mcProfiler.endSection();
    }
}
