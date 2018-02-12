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

package com.google.common.collect;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.annotations.GwtCompatible;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;

/**
 * A skeleton {@code Multimap} implementation, not necessarily in terms of a {@code Map}.
 * 
 * @author Louis Wasserman
 */
@GwtCompatible
abstract class AbstractMultimap<K, V> implements Multimap<K, V> {
  @Override
  public boolean isEmpty() {
    return size() == 0;
  }

  @Override
  public boolean containsValue(@Nullable Object value) {
    for (Collection<V> collection : asMap().values()) {
      if (collection.contains(value)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean containsEntry(@Nullable Object key, @Nullable Object value) {
    Collection<V> collection = asMap().get(key);
    return collection != null && collection.contains(value);
  }
  
  @Override
  public boolean remove(@Nullable Object key, @Nullable Object value) {
    Collection<V> collection = asMap().get(key);
    return collection != null && collection.remove(value);
  }

  @Override
  public boolean put(@Nullable K key, @Nullable V value) {
    return get(key).add(value);
  }

  @Override
  public boolean putAll(@Nullable K key, Iterable<? extends V> values) {
    checkNotNull(values);
    // make sure we only call values.iterator() once
    // and we only call get(key) if values is nonempty
    if (values instanceof Collection) {
      Collection<? extends V> valueCollection = (Collection<? extends V>) values;
      return !valueCollection.isEmpty() && get(key).addAll(valueCollection);
    } else {
      Iterator<? extends V> valueItr = values.iterator();
      return valueItr.hasNext() && Iterators.addAll(get(key), valueItr);
    }
  }

  @Override
  public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
    boolean changed = false;
    for (Entry<? extends K, ? extends V> entry : multimap.entries()) {
      changed |= put(entry.getKey(), entry.getValue());
    }
    return changed;
  }

  @Override
  public Collection<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
    checkNotNull(values);
    Collection<V> result = removeAll(key);
    putAll(key, values);
    return result;
  }
  
  private transient Collection<Entry<K, V>> entries;

  @Override
  public Collection<Entry<K, V>> entries() {
    Collection<Entry<K, V>> result = entries;
    return (result == null) ? entries = createEntries() : result;
  }
  
  Collection<Entry<K, V>> createEntries() {
    if (this instanceof SetMultimap) {
      return new EntrySet();
    } else {
      return new Entries();
    }
  }
  
  private class Entries extends Multimaps.Entries<K, V> {
    @Override
    Multimap<K, V> multimap() {
      return AbstractMultimap.this;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
      return entryIterator();
    }
  }
  
  private class EntrySet extends Entries implements Set<Entry<K, V>> {
    @Override
    public int hashCode() {
      return Sets.hashCodeImpl(this);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
      return Sets.equalsImpl(this, obj);
    }    
  }
  
  abstract Iterator<Entry<K, V>> entryIterator();

  private transient Set<K> keySet;

  @Override
  public Set<K> keySet() {
    Set<K> result = keySet;
    return (result == null) ? keySet = createKeySet() : result;
  }

  Set<K> createKeySet() {
    return new Maps.KeySet<K, Collection<V>>(asMap());
  }
  
  private transient Multiset<K> keys;
  
  @Override
  public Multiset<K> keys() {
    Multiset<K> result = keys;
    return (result == null) ? keys = createKeys() : result;
  }
  
  Multiset<K> createKeys() {
    return new Multimaps.Keys<K, V>(this);
  }
  
  private transient Collection<V> values;
  
  @Override
  public Collection<V> values() {
    Collection<V> result = values;
    return (result == null) ? values = createValues() : result;
  }
  
  Collection<V> createValues() {
    return new Values();
  }

  class Values extends AbstractCollection<V> {
    @Override public Iterator<V> iterator() {
      return valueIterator();
    }

    @Override public int size() {
      return AbstractMultimap.this.size();
    }

    @Override public boolean contains(@Nullable Object o) {
      return AbstractMultimap.this.containsValue(o);
    }

    @Override public void clear() {
      AbstractMultimap.this.clear();
    }
  }
  
  Iterator<V> valueIterator() {
    return Maps.valueIterator(entries().iterator());
  }
  
  private transient Map<K, Collection<V>> asMap;
  
  @Override
  public Map<K, Collection<V>> asMap() {
    Map<K, Collection<V>> result = asMap;
    return (result == null) ? asMap = createAsMap() : result;
  }
  
  abstract Map<K, Collection<V>> createAsMap();

  // Comparison and hashing

  @Override public boolean equals(@Nullable Object object) {
    return Multimaps.equalsImpl(this, object);
  }

  /**
   * Returns the hash code for this multimap.
   *
   * <p>The hash code of a multimap is defined as the hash code of the map view,
   * as returned by {@link Multimap#asMap}.
   *
   * @see Map#hashCode
   */
  @Override public int hashCode() {
    return asMap().hashCode();
  }

  /**
   * Returns a string representation of the multimap, generated by calling
   * {@code toString} on the map returned by {@link Multimap#asMap}.
   *
   * @return a string representation of the multimap
   */
  @Override
  public String toString() {
    return asMap().toString();
  }
}
