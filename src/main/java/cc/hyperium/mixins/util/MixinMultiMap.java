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

package cc.hyperium.mixins.util;

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

    @Shadow @Final private List<T> values;
    @Shadow @Final private Map<Class<?>, List<T>> map;
    @Shadow protected abstract Class<?> initializeClassLookup(Class<?> clazz);

    /**
     * @author FalseHonesty
     * @reason ChatTriggers
     */
    @Overwrite
    public Iterator<T> iterator() {
        return values.isEmpty() ? (UnmodifiableListIterator<T>) Utils.EMPTY_ITERATOR : Iterators.unmodifiableIterator(values.iterator());
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
