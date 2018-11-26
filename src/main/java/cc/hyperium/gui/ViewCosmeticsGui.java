package cc.hyperium.gui;

import cc.hyperium.gui.hyperium.HyperiumMainGui;
import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.JsonHolder;
import com.google.gson.JsonElement;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

import java.awt.Color;
import java.text.DecimalFormat;

public class ViewCosmeticsGui extends HyperiumGui {

    private int cooldownTicks = 0;
    private DecimalFormat formatter = new DecimalFormat("#,###");

    @Override
    protected void pack() {
        reg("VISIT", new GuiButton(nextId(), width / 2 - 100, 5, I18n.format("gui.cosmetics.purchase")), guiButton -> {
            HyperiumMainGui.INSTANCE.setTab(2);
            mc.displayGuiScreen(HyperiumMainGui.INSTANCE);
        }, guiButton -> {

        });
        reg("REFRESH", new GuiButton(nextId(), width / 2 - 100, 30, I18n.format("gui.cosmetics.refresh")), guiButton -> {
            cooldownTicks = 0;
            Multithreading.runAsync(() -> PurchaseApi.getInstance().refreshSelf());
        }, guiButton -> {
            cooldownTicks++;
            guiButton.enabled = cooldownTicks >= 20;
        });
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (PurchaseApi.getInstance() == null || PurchaseApi.getInstance().getSelf() == null || PurchaseApi.getInstance().getSelf().getResponse() == null) {
            return;
        }

        JsonHolder response = PurchaseApi.getInstance().getSelf().getResponse();

        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        String credits = I18n.format("gui.cosmetics.credits");
        fontRendererObj.drawString(credits, (width / 2 - fontRendererObj.getStringWidth(credits)) / 2, 65 / 2, Color.RED.getRGB(), true);

        GlStateManager.scale(.5F, .5F, .5F);

        String total_credits = I18n.format("gui.cosmetics.totalcredits", formatter.format(response.optInt("total_credits")));
        fontRendererObj.drawString(total_credits, width / 2 - fontRendererObj.getStringWidth(total_credits) / 2, 85, Color.RED.getRGB(), true);
        String rem_credits = I18n.format("gui.cosmetics.remainingcredits", formatter.format(response.optInt("remaining_credits")));
        fontRendererObj.drawString(rem_credits, width / 2 - fontRendererObj.getStringWidth(rem_credits) / 2, 95, Color.RED.getRGB(), true);

        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        String purchased = I18n.format("gui.cosmetics.purchased");
        fontRendererObj.drawString(purchased, (width / 2 - fontRendererObj.getStringWidth(purchased)) / 2, 115 / 2, Color.RED.getRGB(), true);
        GlStateManager.scale(.5F, .5F, .5F);

        int i = 0;
        for (JsonElement hyperium : response.optJSONArray("hyperium")) {
            String raw = hyperium.getAsString();
            EnumPurchaseType parse = EnumPurchaseType.parse(raw);
            if (parse == EnumPurchaseType.UNKNOWN || parse == EnumPurchaseType.LEVEL_HEAD)
                continue;
            String tmp = parse.getDisplayName();
            fontRendererObj.drawString(tmp, width / 2 - fontRendererObj.getStringWidth(tmp) / 2, 135 + 10 * i, Color.RED.getRGB(), true);
            i++;
        }

    }

}
