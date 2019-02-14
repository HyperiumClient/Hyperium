package cc.hyperium.mixins.utils;

import com.google.common.collect.Iterators;
import net.minecraft.util.ClassInheritanceMultiMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Mixin(ClassInheritanceMultiMap.class)
public abstract class MixinClassInheritanceMultiMap<T> extends AbstractSet<T> {

    private static Set<Class<?>> field_181158_a1 = ConcurrentHashMap.newKeySet();
    private ConcurrentLinkedQueue<T> queue = new ConcurrentLinkedQueue<>();

    private Set<Class<?>> knownKeys1 = ConcurrentHashMap.newKeySet();
    @Shadow
    @Final
    private Class<T> baseClass;
    private ConcurrentHashMap<Class<?>, ConcurrentLinkedQueue<T>> map1 = new ConcurrentHashMap<>();

    @Inject(method = "<init>", at = @At("RETURN"))
    public void create(Class<T> baseClassIn, CallbackInfo info) {
        this.knownKeys1.add(baseClassIn);
        map1.put(baseClassIn, this.queue);

        for (Class<?> oclass : field_181158_a1) {
            this.createLookup(oclass);
        }
    }

    /**
     * @author Sk1er
     * @reason Add concurrent
     */
    @Overwrite
    protected void createLookup(Class<?> clazz) {
        field_181158_a1.add(clazz);

        for (T t : this.queue) {
            if (clazz.isAssignableFrom(t.getClass())) {
                this.func_181743_a(t, clazz);
            }
        }

        this.knownKeys1.add(clazz);
    }

    /**
     * @author Sk1er
     * @reason Add concurrent
     */
    @Overwrite
    protected Class<?> func_181157_b(Class<?> p_181157_1_) {
        if (this.baseClass.isAssignableFrom(p_181157_1_)) {
            if (!this.knownKeys1.contains(p_181157_1_)) {
                this.createLookup(p_181157_1_);
            }

            return p_181157_1_;
        } else {
            throw new IllegalArgumentException("Don\'t know how to search for " + p_181157_1_);
        }
    }

    /**
     * @author Sk1er
     * @reason Add concurrent
     */
    @Overwrite
    public boolean add(T p_add_1_) {
        for (Class<?> oclass : this.knownKeys1) {
            if (oclass.isAssignableFrom(p_add_1_.getClass())) {
                this.func_181743_a(p_add_1_, oclass);
            }
        }

        return true;
    }

    /**
     * @author Sk1er
     * @reason Add concurrent
     */
    @Overwrite
    private void func_181743_a(T p_181743_1_, Class<?> p_181743_2_) {
        ConcurrentLinkedQueue<T> queue = this.map1.get(p_181743_2_);

        if (queue == null) {
            ConcurrentLinkedQueue<T> value = new ConcurrentLinkedQueue<>();
            value.add(p_181743_1_);
            this.map1.put(p_181743_2_, value);
        } else {
            queue.add(p_181743_1_);
        }
    }

    /**
     * @author Sk1er
     * @reason Add concurrent
     */
    @Overwrite
    public boolean remove(Object p_remove_1_) {
        T t = (T) p_remove_1_;
        boolean flag = false;

        for (Class<?> oclass : this.knownKeys1) {
            if (oclass.isAssignableFrom(t.getClass())) {
                ConcurrentLinkedQueue<T> list = this.map1.get(oclass);

                if (list != null && list.remove(t)) {
                    flag = true;
                }
            }
        }

        return flag;
    }

    /**
     * @author Sk1er
     * @reason Add concurrent
     */
    @Overwrite
    public boolean contains(Object p_contains_1_) {
        return Iterators.contains(this.getByClass(p_contains_1_.getClass()).iterator(), p_contains_1_);
    }

    /**
     * @author Sk1er
     * @reason Add concurrent
     */
    @Overwrite
    public <S> Iterable<S> getByClass(Class<S> clazz) {
        return () -> {
            ConcurrentLinkedQueue<T> list = map1.get(func_181157_b(clazz));

            if (list == null) {
                return Iterators.emptyIterator();
            } else {
                Iterator<T> iterator = list.iterator();
                return Iterators.filter(iterator, clazz);
            }
        };
    }

    /**
     * @author Sk1er
     * @reason Add concurrent
     */
    @Overwrite
    public Iterator<T> iterator() {
        return this.queue.isEmpty() ? Iterators.emptyIterator() : Iterators.unmodifiableIterator(this.queue.iterator());
    }

    /**
     * @author Sk1er
     * @reason Add concurrent
     */
    @Overwrite
    public int size() {
        return this.queue.size();
    }
}
