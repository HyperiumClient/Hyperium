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

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

/**
 * A map entry which forwards all its method calls to another map entry.
 * Subclasses should override one or more methods to modify the behavior of the
 * backing map entry as desired per the <a
 * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.
 *
 * <p><i>Warning:</i> The methods of {@code ForwardingMapEntry} forward
 * <i>indiscriminately</i> to the methods of the delegate. For example,
 * overriding {@link #getValue} alone <i>will not</i> change the behavior of
 * {@link #equals}, which can lead to unexpected behavior. In this case, you
 * should override {@code equals} as well, either providing your own
 * implementation, or delegating to the provided {@code standardEquals} method.
 *
 * <p>Each of the {@code standard} methods, where appropriate, use {@link
 * Objects#equal} to test equality for both keys and values. This may not be
 * the desired behavior for map implementations that use non-standard notions of
 * key equality, such as the entry of a {@code SortedMap} whose comparator is
 * not consistent with {@code equals}.
 *
 * <p>The {@code standard} methods are not guaranteed to be thread-safe, even
 * when all of the methods that they depend on are thread-safe.
 *
 * @author Mike Bostock
 * @author Louis Wasserman
 * @since 2.0 (imported from Google Collections Library)
 */
@GwtCompatible
public abstract class ForwardingMapEntry<K, V>
    extends ForwardingObject implements Entry<K, V> {
  // TODO(user): identify places where thread safety is actually lost

  /** Constructor for use by subclasses. */
  protected ForwardingMapEntry() {}

  @Override protected abstract Entry<K, V> delegate();

  @Override
  public K getKey() {
    return delegate().getKey();
  }

  @Override
  public V getValue() {
    return delegate().getValue();
  }

  @Override
  public V setValue(V value) {
    return delegate().setValue(value);
  }

  @Override public boolean equals(@Nullable Object object) {
    return delegate().equals(object);
  }

  @Override public int hashCode() {
    return delegate().hashCode();
  }

  /**
   * A sensible definition of {@link #equals(Object)} in terms of {@link
   * #getKey()} and {@link #getValue()}. If you override either of these
   * methods, you may wish to override {@link #equals(Object)} to forward to
   * this implementation.
   *
   * @since 7.0
   */
  protected boolean standardEquals(@Nullable Object object) {
    if (object instanceof Entry) {
      Entry<?, ?> that = (Entry<?, ?>) object;
      return Objects.equal(this.getKey(), that.getKey())
          && Objects.equal(this.getValue(), that.getValue());
    }
    return false;
  }

  /**
   * A sensible definition of {@link #hashCode()} in terms of {@link #getKey()}
   * and {@link #getValue()}. If you override either of these methods, you may
   * wish to override {@link #hashCode()} to forward to this implementation.
   *
   * @since 7.0
   */
  protected int standardHashCode() {
    K k = getKey();
    V v = getValue();
    return ((k == null) ? 0 : k.hashCode()) ^ ((v == null) ? 0 : v.hashCode());
  }

  /**
   * A sensible definition of {@link #toString} in terms of {@link
   * #getKey} and {@link #getValue}. If you override either of these
   * methods, you may wish to override {@link #equals} to forward to this
   * implementation.
   *
   * @since 7.0
   */
  @Beta protected String standardToString() {
    return getKey() + "=" + getValue();
  }
}
