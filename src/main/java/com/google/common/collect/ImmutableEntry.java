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

import java.io.Serializable;

import javax.annotation.Nullable;

/**
 * @see Maps#immutableEntry(Object, Object)
 */
@GwtCompatible(serializable = true)
class ImmutableEntry<K, V> extends AbstractMapEntry<K, V>
    implements Serializable {
  final K key;
  final V value;

  ImmutableEntry(@Nullable K key, @Nullable V value) {
    this.key = key;
    this.value = value;
  }

  @Nullable @Override public final K getKey() {
    return key;
  }

  @Nullable @Override public final V getValue() {
    return value;
  }

  @Override public final V setValue(V value) {
    throw new UnsupportedOperationException();
  }

  private static final long serialVersionUID = 0;
}
