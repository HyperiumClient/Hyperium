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

package cc.hyperium.mixinsimp.client.renderer.entity;

import cc.hyperium.event.EventBus;
import cc.hyperium.event.render.RenderPlayerEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;

public class HyperiumRenderPlayer {

    private RenderPlayer parent;

    public HyperiumRenderPlayer(RenderPlayer parent) {
        this.parent = parent;
    }

    public void doRender(AbstractClientPlayer entity, double x, double y, double z, float partialTicks, RenderManager renderManager) {
        EventBus.INSTANCE.post(new RenderPlayerEvent(entity, renderManager, x, y, z, partialTicks));
    }

    public void onUpdateTimer() {
        ModelPlayer modelplayer = parent.getMainModel();
        modelplayer.isRiding = modelplayer.isSneak = false;
    }
}
