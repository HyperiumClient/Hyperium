package cc.hyperium.handlers.handlers.hud;

import cc.hyperium.config.Settings;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderHUDEvent;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldSettings;

public class VanillaEnhancementsHud {


    /*
    Most of code adapted from Orange Marshall's Vanilla Enhancements
     */
    private Minecraft mc = Minecraft.getMinecraft();

    @InvokeEvent
    public void renderArrowCount(RenderHUDEvent event) {
        if (Settings.ARROW_COUNT) {
            EntityPlayerSP thePlayer = mc.thePlayer;
            if (thePlayer != null) {
                ItemStack heldItem = thePlayer.getHeldItem();
                if (heldItem != null) {
                    if (heldItem.getUnlocalizedName().equalsIgnoreCase("item.bow")) {
                        int c = 0;
                        for (ItemStack is : thePlayer.inventory.mainInventory) {
                            if (is != null) {
                                if (is.getUnlocalizedName().equalsIgnoreCase("item.arrow"))
                                    c += is.stackSize;
                            }
                        }
                        ScaledResolution current = ResolutionUtil.current();
                        FontRenderer fontRendererObj = mc.fontRendererObj;
                        final int offset = (mc.playerController.getCurrentGameType() == WorldSettings.GameType.CREATIVE) ? 10 : 0;
                        this.mc.fontRendererObj.drawString(Integer.toString(c), (float) (current.getScaledWidth() - fontRendererObj.getStringWidth(Integer.toString(c)) >> 1), (float) (current.getScaledHeight() - 46 - offset), 16777215, true);
                    }
                }
            }
        }
    }
}
