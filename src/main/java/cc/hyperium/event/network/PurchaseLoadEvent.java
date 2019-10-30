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

package cc.hyperium.event.network;

import cc.hyperium.event.Event;
import cc.hyperium.purchases.HyperiumPurchase;

import java.util.UUID;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public final class PurchaseLoadEvent extends Event {

    @NotNull
    private final UUID uuid;

    @NotNull
    private final HyperiumPurchase purchase;

    private final boolean self;

    public PurchaseLoadEvent(@NotNull UUID uuid, @NotNull HyperiumPurchase purchase, boolean self) {
        Preconditions.checkNotNull(uuid, "uuid");
        Preconditions.checkNotNull(purchase, "purchase");

        this.uuid = uuid;
        this.purchase = purchase;
        this.self = self;
    }

    @NotNull
    public final UUID getUuid() {
        return uuid;
    }

    @NotNull
    public final HyperiumPurchase getPurchase() {
        return purchase;
    }

    public final boolean getSelf() {
        return self;
    }
}
