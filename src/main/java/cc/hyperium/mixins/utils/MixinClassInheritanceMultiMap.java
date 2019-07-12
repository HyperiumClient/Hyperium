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

    @Shadow @Final private Class<T> baseClass;

    private static Set<Class<?>> field_181158_a1 = ConcurrentHashMap.newKeySet();
    private ConcurrentLinkedQueue<T> queue = new ConcurrentLinkedQueue<>();
    private Set<Class<?>> knownKeys1 = ConcurrentHashMap.newKeySet();
    private ConcurrentHashMap<Class<?>, ConcurrentLinkedQueue<T>> map1 = new ConcurrentHashMap<>();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void create(Class<T> baseClassIn, CallbackInfo info) {
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
                this.addForClass(t, clazz);
            }
        }

        this.knownKeys1.add(clazz);
    }

    /**
     * @author Sk1er
     * @reason Add concurrent
     */
    @Overwrite
    protected Class<?> initializeClassLookup(Class<?> clazz) {
        if (this.baseClass.isAssignableFrom(clazz)) {
            if (!this.knownKeys1.contains(clazz)) {
                this.createLookup(clazz);
            }

            return clazz;
        } else {
            throw new IllegalArgumentException("Don\'t know how to search for " + clazz);
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
                this.addForClass(p_add_1_, oclass);
            }
        }

        return true;
    }

    /**
     * @author Sk1er
     * @reason Add concurrent
     */
    @Overwrite
    private void addForClass(T value, Class<?> parentClass) {
        ConcurrentLinkedQueue<T> queue = this.map1.get(parentClass);

        if (queue == null) {
            ConcurrentLinkedQueue<T> linkedValue = new ConcurrentLinkedQueue<>();
            linkedValue.add(value);
            this.map1.put(parentClass, linkedValue);
        } else {
            queue.add(value);
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
            ConcurrentLinkedQueue<T> list = map1.get(initializeClassLookup(clazz));

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
