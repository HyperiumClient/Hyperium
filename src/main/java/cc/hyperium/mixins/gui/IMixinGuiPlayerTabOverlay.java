package cc.hyperium.mixins.gui;

import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScoreObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiPlayerTabOverlay.class)
public interface IMixinGuiPlayerTabOverlay {

    @Invoker
    void callDrawScoreboardValues(ScoreObjective p_175247_1_, int p_175247_2_, String p_175247_3_, int p_175247_4_, int p_175247_5_, NetworkPlayerInfo p_175247_6_);

    @Invoker
    void callDrawPing(int p_175245_1_, int p_175245_2_, int yIn, NetworkPlayerInfo networkPlayerInfoIn);
}
