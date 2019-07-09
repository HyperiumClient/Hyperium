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
