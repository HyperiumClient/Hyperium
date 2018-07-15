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

package cc.hyperium.utils.eastereggs;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderPlayerEvent;
import cc.hyperium.handlers.handlers.HypixelDetector;
import cc.hyperium.utils.StaffUtils;
import net.minecraft.client.Minecraft;

import java.util.UUID;

/**
 * EasterEggs thingy
 *
 * @author SHARDcoder
 */
public class EasterEggs {

    @InvokeEvent
    private void onRender(RenderPlayerEvent event) {
        UUID id = event.getEntity().getUniqueID();
        if (!StaffUtils.isStaff(id) || !StaffUtils.hasEasterEggEntityPath(id) || StaffUtils.getEasterEggEntityPath(id).equals("none"))
            return;
        if (((HypixelDetector.getInstance().isHypixel() && Hyperium.INSTANCE.getHandlers().getLocationHandler().getLocation().contains("lobby")) || Settings.ALWAYS_SHOW_SUPER_SECRET_SETTINGS) && Settings.SUPERSECRETSETTINGSV2) {
            Minecraft.getMinecraft().getRenderManager().renderEntitySimple(EasterEggEntity.getEntity(event.getEntity(), StaffUtils.getEasterEggEntityPath(id)), event.getPartialTicks());
            event.getEntity().setInvisible(true);
        }
    }
}
