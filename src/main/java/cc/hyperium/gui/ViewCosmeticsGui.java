package cc.hyperium.gui;

import cc.hyperium.mods.sk1ercommon.Multithreading;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.utils.JsonHolder;
import com.google.gson.JsonElement;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.text.DecimalFormat;

public class ViewCosmeticsGui extends HyperiumGui {

    private int cooldownTicks = 0;
    private DecimalFormat formatter = new DecimalFormat("#,###");

    @Override
    protected void pack() {
        reg("VISIT", new GuiButton(nextId(), width / 2 - 100, 5, "Purchase cosmetics"), guiButton -> {
           new ShopGui().show();
        }, guiButton -> {

        });
        reg("REFRESH", new GuiButton(nextId(), width / 2 - 100, 30, "Refresh purchases"), guiButton -> {
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
        JsonHolder response = PurchaseApi.getInstance().getSelf().getResponse();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        String credits = "Credits";
        fontRendererObj.drawString(credits, (width / 2 - fontRendererObj.getStringWidth(credits) ) / 2, 65 / 2, Color.RED.getRGB(), true);

        GlStateManager.scale(.5F, .5F, .5F);

        String total_credits = "Total credits: " + formatter.format(response.optInt("total_credits"));
        fontRendererObj.drawString(total_credits, width / 2 - fontRendererObj.getStringWidth(total_credits) / 2, 85, Color.RED.getRGB(), true);
        String rem_credits = "Remaining credits: " + formatter.format(response.optInt("remaining_credits"));
        fontRendererObj.drawString(rem_credits, width / 2 - fontRendererObj.getStringWidth(rem_credits) / 2, 95, Color.RED.getRGB(), true);

        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        fontRendererObj.drawString("Purchased Packages", (width / 2 - fontRendererObj.getStringWidth("Purchased Packages") ) / 2, 115 / 2, Color.RED.getRGB(), true);
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
