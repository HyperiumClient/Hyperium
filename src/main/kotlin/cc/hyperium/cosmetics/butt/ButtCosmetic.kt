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
package cc.hyperium.cosmetics.butt

import cc.hyperium.cosmetics.AbstractCosmetic
import cc.hyperium.event.InvokeEvent
import cc.hyperium.event.Priority
import cc.hyperium.event.model.PreCopyPlayerModelAnglesEvent
import cc.hyperium.purchases.EnumPurchaseType
import cc.hyperium.purchases.PurchaseApi
import cc.hyperium.utils.model.IModelPlayer

class ButtCosmetic : AbstractCosmetic(false, EnumPurchaseType.BUTT) {
    @InvokeEvent(priority = Priority.HIGH)
    fun preCopy(event: PreCopyPlayerModelAnglesEvent) {
        if (event.model !is IModelPlayer) return
        if (!isPurchasedBy(event.entity.uniqueID)) {
            (event.model as IModelPlayer).butt.showModel = false
            return
        }

        val packageIfReady = PurchaseApi.getInstance().getPackageIfReady(event.entity.uniqueID) ?: return
        (event.model as IModelPlayer).butt.showModel = !packageIfReady.cachedSettings.isButtDisabled
        (event.model as IModelPlayer).butt.offsetY = if (event.entity.isSneaking) -.45f else -.4f
        (event.model as IModelPlayer).butt.offsetZ = if (event.entity.isSneaking) .3f else 0f
    }
}
