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

package cc.hyperium.mods.chromahud.displayitems.hyperium;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.entity.PlayerAttackEntityEvent;
import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.text.DecimalFormat;

public class ReachDisplay extends DisplayItem {

    private final Minecraft mc = Minecraft.getMinecraft();
    private String rangeText = "Hasn't attacked";
    private long lastAttack;

    public ReachDisplay(JsonHolder data, int ordinal) {
        super(data, ordinal);
        EventBus.INSTANCE.register(this);
    }

    @InvokeEvent
    public void onAttack(PlayerAttackEntityEvent event) {
        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mc.objectMouseOver.entityHit.getEntityId() ==
            event.getEntity().getEntityId()) {
            Vec3 vec = mc.getRenderViewEntity().getPositionEyes(1.0f);
            double range = mc.objectMouseOver.hitVec.distanceTo(vec);
            rangeText = new DecimalFormat(".##").format(range) + " blocks";
        } else {
            rangeText = "Not on target?";
        }

        lastAttack = System.currentTimeMillis();
    }

    @Override
    public void draw(int x, double y, boolean config) {
        if (System.currentTimeMillis() - lastAttack > 2000L) rangeText = "Hasn't attacked";
        height = mc.fontRendererObj.FONT_HEIGHT;
        width = mc.fontRendererObj.getStringWidth(rangeText);
        ElementRenderer.draw(x, y, rangeText);
    }
}
