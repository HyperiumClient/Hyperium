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

import com.google.common.annotations.GwtCompatible;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

/**
 * A {@code Multimap} that cannot hold duplicate key-value pairs. Adding a
 * key-value pair that's already in the multimap has no effect. See the {@link
 * Multimap} documentation for information common to all multimaps.
 *
 * <p>The {@link #get}, {@link #removeAll}, and {@link #replaceValues} methods
 * each return a {@link Set} of values, while {@link #entries} returns a {@code
 * Set} of map entries. Though the method signature doesn't say so explicitly,
 * the map returned by {@link #asMap} has {@code Set} values.
 *
 * <p>If the values corresponding to a single key should be ordered according to
 * a {@link java.util.Comparator} (or the natural order), see the
 * {@link SortedSetMultimap} subinterface.
 * 
 * <p>Since the value collections are sets, the behavior of a {@code SetMultimap}
 * is not specified if key <em>or value</em> objects already present in the 
 * multimap change in a manner that affects {@code equals} comparisons.  
 * Use caution if mutable objects are used as keys or values in a 
 * {@code SetMultimap}.
 *
 * <p>See the Guava User Guide article on <a href=
 * "http://code.google.com/p/guava-libraries/wiki/NewCollectionTypesExplained#Multimap">
 * {@code Multimap}</a>.
 *
 * @author Jared Levy
 * @since 2.0 (imported from Google Collections Library)
 */
@GwtCompatible
public interface SetMultimap<K, V> extends Multimap<K, V> {
  /**
   * {@inheritDoc}
   *
   * <p>Because a {@code SetMultimap} has unique values for a given key, this
   * method returns a {@link Set}, instead of the {@link Collection}
   * specified in the {@link Multimap} interface.
   */
  @Override
  Set<V> get(@Nullable K key);

  /**
   * {@inheritDoc}
   *
   * <p>Because a {@code SetMultimap} has unique values for a given key, this
   * method returns a {@link Set}, instead of the {@link Collection}
   * specified in the {@link Multimap} interface.
   */
  @Override
  Set<V> removeAll(@Nullable Object key);

  /**
   * {@inheritDoc}
   *
   * <p>Because a {@code SetMultimap} has unique values for a given key, this
   * method returns a {@link Set}, instead of the {@link Collection}
   * specified in the {@link Multimap} interface.
   *
   * <p>Any duplicates in {@code values} will be stored in the multimap once.
   */
  @Override
  Set<V> replaceValues(K key, Iterable<? extends V> values);

  /**
   * {@inheritDoc}
   *
   * <p>Because a {@code SetMultimap} has unique values for a given key, this
   * method returns a {@link Set}, instead of the {@link Collection}
   * specified in the {@link Multimap} interface.
   */
  @Override
  Set<Map.Entry<K, V>> entries();

  /**
   * {@inheritDoc}
   *
   * <p><b>Note:</b> The returned map's values are guaranteed to be of type
   * {@link Set}. To obtain this map with the more specific generic type
   * {@code Map<K, Set<V>>}, call {@link Multimaps#asMap(SetMultimap)} instead.
   */
  @Override
  Map<K, Collection<V>> asMap();

  /**
   * Compares the specified object to this multimap for equality.
   *
   * <p>Two {@code SetMultimap} instances are equal if, for each key, they
   * contain the same values. Equality does not depend on the ordering of keys
   * or values.
   *
   * <p>An empty {@code SetMultimap} is equal to any other empty {@code
   * Multimap}, including an empty {@code ListMultimap}.
   */
  @Override
  boolean equals(@Nullable Object obj);
}
