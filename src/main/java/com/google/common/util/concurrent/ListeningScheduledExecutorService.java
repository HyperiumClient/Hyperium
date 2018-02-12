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

import com.google.common.annotations.Beta;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A {@link ScheduledExecutorService} that returns {@link ListenableFuture}
 * instances from its {@code ExecutorService} methods. To create an instance
 * from an existing {@link ScheduledExecutorService}, call
 * {@link MoreExecutors#listeningDecorator(ScheduledExecutorService)}.
 *
 * @author Chris Povirk
 * @since 10.0
 */
@Beta
public interface ListeningScheduledExecutorService
    extends ScheduledExecutorService, ListeningExecutorService {

  /** @since 15.0 (previously returned ScheduledFuture) */
  @Override
  ListenableScheduledFuture<?> schedule(
          Runnable command, long delay, TimeUnit unit);

  /** @since 15.0 (previously returned ScheduledFuture) */
  @Override
  <V> ListenableScheduledFuture<V> schedule(
          Callable<V> callable, long delay, TimeUnit unit);

  /** @since 15.0 (previously returned ScheduledFuture) */
  @Override
  ListenableScheduledFuture<?> scheduleAtFixedRate(
          Runnable command, long initialDelay, long period, TimeUnit unit);

  /** @since 15.0 (previously returned ScheduledFuture) */
  @Override
  ListenableScheduledFuture<?> scheduleWithFixedDelay(
          Runnable command, long initialDelay, long delay, TimeUnit unit);
}
