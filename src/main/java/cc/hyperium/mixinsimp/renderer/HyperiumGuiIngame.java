package cc.hyperium.mixinsimp.renderer;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.RenderHUDEvent;
import cc.hyperium.mods.chromahud.displayitems.hyperium.ScoreboardDisplay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.ScoreObjective;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class HyperiumGuiIngame {


    public void renderGameOverlay(float partialTicks, CallbackInfo ci) {
        EventBus.INSTANCE.post(new RenderHUDEvent(partialTicks));
    }

    public void renderScoreboard(ScoreObjective objective, ScaledResolution resolution) {
        //For *extra* scoreboards
        ScoreboardDisplay.p_180475_1_ = objective;
        ScoreboardDisplay.p_180475_2_ = resolution;

        Hyperium.INSTANCE.getHandlers().getScoreboardRenderer().render(objective, resolution);
    }
}
