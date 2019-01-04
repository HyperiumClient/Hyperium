package cc.hyperium.event;

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
        return this.uuid;
    }

    @NotNull
    public final HyperiumPurchase getPurchase() {
        return this.purchase;
    }

    public final boolean getSelf() {
        return this.self;
    }
}
