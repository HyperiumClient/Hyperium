package cc.hyperium.mixins.gui;

import cc.hyperium.gui.GuiHyperiumCredits;
import cc.hyperium.gui.GuiIngameMultiplayer;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.text.DecimalFormat;

@Mixin(GuiIngameMenu.class)
public class MixinGuiIngameMenu extends GuiScreen {

    private final DecimalFormat formatter = new DecimalFormat("#,###");
    private static JsonHolder data = new JsonHolder();
    private long lastUpdate = 0L;
    private int cooldown = 0;
    private int baseAngle;

    @Inject(method = "initGui", at = @At("RETURN"))
    public void initGui(CallbackInfo ci) {
        this.buttonList.add(new GuiButton(8, this.width - 200, this.height - 20, 200, 20, "Credits"));
//        this.buttonList.get(3).displayString = Minecraft.getMinecraft().getIntegratedServer().getPublic() ? "Change Server" : this.buttonList.get(3).displayString;
//        this.buttonList.get(3).enabled = Minecraft.getMinecraft().getIntegratedServer().getPublic() ? true : this.buttonList.get(3).enabled;
        if (Minecraft.getMinecraft().theWorld.isRemote) {
            GuiButton oldButton = buttonList.remove(3);
            GuiButton newButton = new GuiButton(10, oldButton.xPosition, oldButton.yPosition, oldButton.getButtonWidth(), 20, "Server List");
            buttonList.add(newButton);
        }
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    public void actionPerformed(GuiButton button, CallbackInfo ci) {
        if (button.id == 8)
            Minecraft.getMinecraft().displayGuiScreen(new GuiHyperiumCredits(Minecraft.getMinecraft().currentScreen));
        if (button.id == 10 && Minecraft.getMinecraft().theWorld.isRemote)
            Minecraft.getMinecraft().displayGuiScreen(new GuiIngameMultiplayer(Minecraft.getMinecraft().currentScreen));
    }

    private synchronized void refreshData() {
        lastUpdate = System.currentTimeMillis() * 2;
        Multithreading.runAsync(() -> {
            data = PurchaseApi.getInstance().get("https://api.hyperium.cc/users");
            lastUpdate = System.currentTimeMillis();
        });
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
        GlStateManager.pushMatrix();
        if (System.currentTimeMillis() - lastUpdate > 2000L) {
            refreshData();

        }
        baseAngle = baseAngle % 360;
        ScaledResolution current = ResolutionUtil.current();
        GlStateManager.translate(current.getScaledWidth() / 2, 10, 0);

        drawCenteredString(fontRendererObj, "Hyperium Stats", 0, 0, new Color(249 / 255F, 76 / 255F, 238 / 255F, 1.0F).getRGB());
        GlStateManager.translate(0F, 10F, 0F);
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        GlStateManager.rotate(baseAngle, 1.0F, 0.0F, 0.0F);
        GlStateManager.enableAlpha();
        float z = 4F;
        float e = 80;
        float i = 0;
        GlStateManager.translate(0.0F, 0.0F, z);
        if (baseAngle < e) {
            i = (e - Math.abs(baseAngle)) / e;
        } else if (baseAngle > 360 - e) {
            i = (e - (Math.abs((360) - baseAngle))) / e;
        }

        if (i > 0)
            drawCenteredString(fontRendererObj, "Online players: " + formatter.format(data.optInt("online")), 0, 0, new Color(249 / 255F, 76 / 255F, 238 / 255F, i).getRGB());


        GlStateManager.translate(0.0F, 0.0F, -z);
        GlStateManager.rotate(90, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, z);
        i = (e - Math.abs(270 - baseAngle)) / e;

        if (i > 0)
            drawCenteredString(fontRendererObj, "Last Day: " + formatter.format(data.optInt("day")), 0, 0, new Color(249 / 255F, 76 / 255F, 238 / 255F, i).getRGB());


        GlStateManager.translate(0.0F, 0.0F, -z);
        GlStateManager.rotate(90, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, z);
        i = (e - Math.abs(180 - baseAngle)) / e;

        if (i > 0)
            drawCenteredString(fontRendererObj, "Last Week: " + formatter.format(data.optInt("week")), 0, 0, new Color(249 / 255F, 76 / 255F, 238 / 255F, i).getRGB());


        GlStateManager.translate(0.0F, 0.0F, -z);
        GlStateManager.rotate(90, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, z);
        i = (e - Math.abs(90 - baseAngle)) / e;
        if (i > 0)
            drawCenteredString(fontRendererObj, "All time: " + formatter.format(data.optInt("all")), 0, 0, new Color(249 / 255F, 76 / 255F, 238 / 255F, i).getRGB());

        GlStateManager.popMatrix();
    }

}
