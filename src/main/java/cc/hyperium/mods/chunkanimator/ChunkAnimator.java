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

package cc.hyperium.mods.chunkanimator;

import cc.hyperium.Hyperium;
import cc.hyperium.mods.AbstractMod;

/*
 * Created by Cubxity on 12/1/2018
 */

public class ChunkAnimator extends AbstractMod {

    private final Metadata meta;
    private final AnimationHandler animationHandler = new AnimationHandler();
    private final ChunkAnimatorConfig config;

    public ChunkAnimator() {
        meta = new Metadata(this, "ChunkAnimator", "1.1", "Lumien");
        Hyperium.CONFIG.register(config = new ChunkAnimatorConfig());
    }

    @Override
    public AbstractMod init() {
        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return meta;
    }

    public AnimationHandler getAnimationHandler() {
        return animationHandler;
    }

    public ChunkAnimatorConfig getConfig() {
        return config;
    }
}
