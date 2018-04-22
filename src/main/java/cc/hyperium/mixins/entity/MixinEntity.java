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

package cc.hyperium.mixins.entity;


import net.minecraft.entity.Entity;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(Entity.class)
public abstract class MixinEntity {

    private IChatComponent cachedName;
    private long nameCacheTime = System.currentTimeMillis();

    @Shadow
    public abstract String getName();

    @Shadow
    protected abstract HoverEvent getHoverEvent();

    @Shadow
    public abstract UUID getUniqueID();

    /**
     * @author Sk1er
     */
    @Overwrite
    public IChatComponent getDisplayName() {
        if (cachedName == null || System.currentTimeMillis() - nameCacheTime > 50L) {
            ChatComponentText chatcomponenttext = new ChatComponentText(this.getName());
            chatcomponenttext.getChatStyle().setChatHoverEvent(this.getHoverEvent());
            chatcomponenttext.getChatStyle().setInsertion(this.getUniqueID().toString());
            this.cachedName = chatcomponenttext;
        }
        return cachedName;
    }

    @Shadow
    public Vec3 getLook(float particalTicks) {
        return null;
    }
}