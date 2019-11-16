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

package cc.hyperium.cosmetics.butt;

import cc.hyperium.cosmetics.AbstractCosmetic;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.model.PreCopyPlayerModelAnglesEvent;
import cc.hyperium.event.Priority;
import cc.hyperium.utils.model.IModelPlayer;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;

public class ButtCosmetic extends AbstractCosmetic {
    public ButtCosmetic() {
        super(false, EnumPurchaseType.BUTT);
    }

    @InvokeEvent(priority = Priority.HIGH)
    public void preCopy(PreCopyPlayerModelAnglesEvent event) {
        if (!(event.getModel() instanceof IModelPlayer)) return;

        if (!isPurchasedBy(event.getEntity().getUniqueID())) {
            ((IModelPlayer) event.getModel()).getButt().showModel = false;
            return;
        }

        HyperiumPurchase packageIfReady = PurchaseApi.getInstance().getPackageIfReady(event.getEntity().getUniqueID());
        if (packageIfReady == null) return;

        ((IModelPlayer) event.getModel()).getButt().showModel = !packageIfReady.getCachedSettings().isButtDisabled();
        ((IModelPlayer) event.getModel()).getButt().offsetY = event.getEntity().isSneaking() ? -.45F : -.4F;
        ((IModelPlayer) event.getModel()).getButt().offsetZ = event.getEntity().isSneaking() ? .3F : 0F;
    }
}
