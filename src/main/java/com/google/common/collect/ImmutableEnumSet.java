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
import java.util.Collection;
import java.util.EnumSet;

/**
 * Implementation of {@link ImmutableSet} backed by a non-empty {@link
 * EnumSet}.
 *
 * @author Jared Levy
 */
@GwtCompatible(serializable = true, emulated = true)
@SuppressWarnings("serial") // we're overriding default serialization
final class ImmutableEnumSet<E extends Enum<E>> extends ImmutableSet<E> {
  static <E extends Enum<E>> ImmutableSet<E> asImmutable(EnumSet<E> set) {
    switch (set.size()) {
      case 0:
        return ImmutableSet.of();
      case 1:
        return ImmutableSet.of(Iterables.getOnlyElement(set));
      default:
        return new ImmutableEnumSet<E>(set);
    }
  }

  /*
   * Notes on EnumSet and <E extends Enum<E>>:
   *
   * This class isn't an arbitrary ForwardingImmutableSet because we need to
   * know that calling {@code clone()} during deserialization will return an
   * object that no one else has a reference to, allowing us to guarantee
   * immutability. Hence, we support only {@link EnumSet}.
   */
  private final transient EnumSet<E> delegate;

  private ImmutableEnumSet(EnumSet<E> delegate) {
    this.delegate = delegate;
  }

  @Override boolean isPartialView() {
    return false;
  }

  @Override public UnmodifiableIterator<E> iterator() {
    return Iterators.unmodifiableIterator(delegate.iterator());
  }

  @Override
  public int size() {
    return delegate.size();
  }

  @Override public boolean contains(Object object) {
    return delegate.contains(object);
  }

  @Override public boolean containsAll(Collection<?> collection) {
    return delegate.containsAll(collection);
  }

  @Override public boolean isEmpty() {
    return delegate.isEmpty();
  }

  @Override public boolean equals(Object object) {
    return object == this || delegate.equals(object);
  }

  private transient int hashCode;

  @Override public int hashCode() {
    int result = hashCode;
    return (result == 0) ? hashCode = delegate.hashCode() : result;
  }

  @Override public String toString() {
    return delegate.toString();
  }

  // All callers of the constructor are restricted to <E extends Enum<E>>.
  @Override Object writeReplace() {
    return new EnumSerializedForm<E>(delegate);
  }

  /*
   * This class is used to serialize ImmutableEnumSet instances.
   */
  private static class EnumSerializedForm<E extends Enum<E>>
      implements Serializable {
    final EnumSet<E> delegate;
    EnumSerializedForm(EnumSet<E> delegate) {
      this.delegate = delegate;
    }
    Object readResolve() {
      // EJ2 #76: Write readObject() methods defensively.
      return new ImmutableEnumSet<E>(delegate.clone());
    }
    private static final long serialVersionUID = 0;
  }
}
