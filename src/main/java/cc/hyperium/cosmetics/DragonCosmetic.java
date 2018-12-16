/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.cosmetics;
import cc.hyperium.cosmetics.dragon.DragonHeadRenderer;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderPlayerEvent;
import cc.hyperium.purchases.EnumPurchaseType;
import net.minecraft.client.entity.AbstractClientPlayer;

public class DragonCosmetic extends AbstractCosmetic {

    private DragonHeadRenderer renderer;

    public DragonCosmetic() {
        super(false, EnumPurchaseType.DRAGON_HEAD);
        renderer = new DragonHeadRenderer(this);
        EventBus.INSTANCE.register(renderer);
    }


    @InvokeEvent
    public void renderPlayer(RenderPlayerEvent event) {
        AbstractClientPlayer player = event.getEntity();
    }
}
