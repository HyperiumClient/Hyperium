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

package cc.hyperium.mixinsimp.entity;

import cc.hyperium.mixins.entity.IMixinEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class HyperiumEntity {

    private Entity parent;
    private long nameCacheTime = System.currentTimeMillis();
    private IChatComponent cachedName;

    public HyperiumEntity(Entity parent) {
        this.parent = parent;
    }

    public IChatComponent getDisplayName() {
        if (cachedName == null || System.currentTimeMillis() - nameCacheTime > 50L) {
            ChatComponentText chatcomponenttext = new ChatComponentText(parent.getName());
            //not needed otherwise
            if (Minecraft.getMinecraft().isIntegratedServerRunning())
                chatcomponenttext.getChatStyle().setChatHoverEvent(((IMixinEntity) parent).callGetHoverEvent());
            chatcomponenttext.getChatStyle().setInsertion(parent.getUniqueID().toString());
            cachedName = chatcomponenttext;
            nameCacheTime = System.currentTimeMillis();
        }
        return cachedName;
    }
}
