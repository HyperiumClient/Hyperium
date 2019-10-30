package cc.hyperium.mixins.client.gui.achievement;

import cc.hyperium.config.Settings;
import net.minecraft.client.gui.achievement.GuiAchievement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiAchievement.class)
public class MixinGuiAchievement {

    @Inject(method = "updateAchievementWindow", at = @At("HEAD"), cancellable = true)
    private void cancelAchievementRender(CallbackInfo ci) {
        if (Settings.DISABLE_ACHIEVEMENTS) ci.cancel();
    }
}
