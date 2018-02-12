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

package com.google.common.util.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.Nullable;

/**
 * A callback for accepting the results of a {@link Future}
 * computation asynchronously.
 *
 * <p>To attach to a {@link ListenableFuture} use {@link Futures#addCallback}.
 *
 * @author Anthony Zana
 * @since 10.0
 */
public interface FutureCallback<V> {
  /**
   * Invoked with the result of the {@code Future} computation when it is
   * successful.
   */
  void onSuccess(@Nullable V result);

  /**
   * Invoked when a {@code Future} computation fails or is canceled.
   *
   * <p>If the future's {@link Future#get() get} method throws an {@link
   * ExecutionException}, then the cause is passed to this method. Any other
   * thrown object is passed unaltered.
   */
  void onFailure(Throwable t);
}
