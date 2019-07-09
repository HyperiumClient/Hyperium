package cc.hyperium.mixins.utils;

import com.chattriggers.ctjs.utils.Utils;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableListIterator;
import net.minecraft.util.ClassInheritanceMultiMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Mixin(ClassInheritanceMultiMap.class)
public abstract class MixinMultiMap<T> {
    @Shadow
    @Final
    private List<T> values;

    @Shadow
    @Final
    private Map<Class<?>, List<T>> map;

    @Shadow
    protected abstract Class<?> initializeClassLookup(Class<?> clazz);

    /**
     * @author FalseHonesty
     * @reason ChatTriggers
     */
    @Overwrite
    public Iterator<T> iterator() {
        return values.isEmpty() ? (UnmodifiableListIterator<T>) Utils.EMPTY_ITERATOR : Iterators.unmodifiableIterator(this.values.iterator());
    }

    /**
     * @author FalseHonesty
     * @reason ChatTriggers
     */
    @Overwrite
    public <S> Iterable<S> getByClass(final Class<S> clazz) {
        return () -> {
            List<T> list = map.get(initializeClassLookup(clazz));

            if (list == null) {
                return (UnmodifiableListIterator<S>) Utils.EMPTY_ITERATOR;
            } else {
                Iterator<T> iterator = list.iterator();
                return Iterators.filter(iterator, clazz);
            }
        };
    }
}
