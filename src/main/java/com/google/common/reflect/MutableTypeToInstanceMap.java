/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.google.common.reflect;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ForwardingMapEntry;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

/**
 * A mutable type-to-instance map.
 * See also {@link ImmutableTypeToInstanceMap}.
 *
 * @author Ben Yu
 * @since 13.0
 */
@Beta
public final class MutableTypeToInstanceMap<B> extends ForwardingMap<TypeToken<? extends B>, B>
    implements TypeToInstanceMap<B> {

  private final Map<TypeToken<? extends B>, B> backingMap = Maps.newHashMap();

  @Nullable
  @Override
  public <T extends B> T getInstance(Class<T> type) {
    return trustedGet(TypeToken.of(type));
  }

  @Nullable
  @Override
  public <T extends B> T putInstance(Class<T> type, @Nullable T value) {
    return trustedPut(TypeToken.of(type), value);
  }

  @Nullable
  @Override
  public <T extends B> T getInstance(TypeToken<T> type) {
    return trustedGet(type.rejectTypeVariables());
  }

  @Nullable
  @Override
  public <T extends B> T putInstance(TypeToken<T> type, @Nullable T value) {
    return trustedPut(type.rejectTypeVariables(), value);
  }

  /** Not supported. Use {@link #putInstance} instead. */
  @Override public B put(TypeToken<? extends B> key, B value) {
    throw new UnsupportedOperationException("Please use putInstance() instead.");
  }

  /** Not supported. Use {@link #putInstance} instead. */
  @Override public void putAll(Map<? extends TypeToken<? extends B>, ? extends B> map) {
    throw new UnsupportedOperationException("Please use putInstance() instead.");
  }

  @Override public Set<Entry<TypeToken<? extends B>, B>> entrySet() {
    return UnmodifiableEntry.transformEntries(super.entrySet());
  }

  @Override protected Map<TypeToken<? extends B>, B> delegate() {
    return backingMap;
  }

  @SuppressWarnings("unchecked") // value could not get in if not a T
  @Nullable
  private <T extends B> T trustedPut(TypeToken<T> type, @Nullable T value) {
    return (T) backingMap.put(type, value);
  }

  @SuppressWarnings("unchecked") // value could not get in if not a T
  @Nullable
  private <T extends B> T trustedGet(TypeToken<T> type) {
    return (T) backingMap.get(type);
  }

  private static final class UnmodifiableEntry<K, V> extends ForwardingMapEntry<K, V> {

    private final Entry<K, V> delegate;

    static <K, V> Set<Entry<K, V>> transformEntries(final Set<Entry<K, V>> entries) {
      return new ForwardingSet<Entry<K, V>>() {
        @Override protected Set<Entry<K, V>> delegate() {
          return entries;
        }
        @Override public Iterator<Entry<K, V>> iterator() {
          return UnmodifiableEntry.transformEntries(super.iterator());
        }
        @Override public Object[] toArray() {
          return standardToArray();
        }
        @Override public <T> T[] toArray(T[] array) {
          return standardToArray(array);
        }
      };
    }
  
    private static <K, V> Iterator<Entry<K, V>> transformEntries(Iterator<Entry<K, V>> entries) {
      return Iterators.transform(entries, new Function<Entry<K, V>, Entry<K, V>>() {
        @Override public Entry<K, V> apply(Entry<K, V> entry) {
          return new UnmodifiableEntry<K, V>(entry);
        }
      });
    }

    private UnmodifiableEntry(Entry<K, V> delegate) {
      this.delegate = checkNotNull(delegate);
    }

    @Override protected Entry<K, V> delegate() {
      return delegate;
    }

    @Override public V setValue(V value) {
      throw new UnsupportedOperationException();
    }
  }
}
