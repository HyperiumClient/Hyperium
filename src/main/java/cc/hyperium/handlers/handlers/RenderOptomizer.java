/*
 *  Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  Hyperium Dev Team
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

package cc.hyperium.handlers.handlers;

import cc.hyperium.config.ConfigOpt;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderHUDEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class RenderOptomizer {

    private List<Entity> rendered = new ArrayList<>();

    @ConfigOpt
    private boolean limitArmourStands = true;

    @InvokeEvent
    public void renderHUD(RenderHUDEvent event) {
        rendered.clear();
    }

    public boolean shouldRender(Entity entity) {
        if (entity instanceof EntityArmorStand && limitArmourStands) {
            return isSimilar(entity);

        }
        return true;
    }

    private boolean isSimilar(Entity other) {

        String text1 = EnumChatFormatting.getTextWithoutFormattingCodes(other.getDisplayName().getUnformattedText());
        double posX = other.posX;
        double posY = other.posY;
        double posZ = other.posZ;
        for (Entity entity : rendered) {
            String text = EnumChatFormatting.getTextWithoutFormattingCodes(entity.getDisplayName().getUnformattedText());
            if (text.equalsIgnoreCase(text1) && posX == entity.posX && posY == entity.posY && entity.posZ == posZ) {
                return true;
            }

        }
        return false;
    }
}
