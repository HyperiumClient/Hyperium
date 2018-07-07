package cc.hyperium.mixins.gui;

import cc.hyperium.mixinsimp.gui.HyperiumGuiIngameMenu;
import cc.hyperium.utils.JsonHolder;
import java.text.DecimalFormat;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngameMenu.class)
public class MixinGuiIngameMenu extends GuiScreen {

    private static JsonHolder data = new JsonHolder();
    private final DecimalFormat formatter = new DecimalFormat("#,###");
    private long lastUpdate = 0L;
    private int cooldown = 0;
    private int baseAngle;

    private HyperiumGuiIngameMenu hyperiumGuiIngameMenu;

    @Inject(method = "initGui", at = @At("RETURN"))
    public void initGui(CallbackInfo ci) {
        hyperiumGuiIngameMenu.initGui(this.buttonList);
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    public void actionPerformed(GuiButton button, CallbackInfo ci) {
        hyperiumGuiIngameMenu.actionPerformed(button);
    }

    @Inject(method = "updateScreen", at = @At("HEAD"))
    public void update(CallbackInfo info) {
        cooldown++;
        if (cooldown > 40) {
            baseAngle += 9;
            if (cooldown >= 50) {
                cooldown = 0;
            }
        }
    }

    @Inject(method = "drawScreen", at = @At("HEAD"))
    public void draw(int mouseX, int mouseY, float partialTicks, CallbackInfo info) {
        hyperiumGuiIngameMenu.draw(mouseX, mouseY, partialTicks, lastUpdate, baseAngle, fontRendererObj, data, formatter);
    }

}
