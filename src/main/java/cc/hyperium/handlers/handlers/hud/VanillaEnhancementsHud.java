package cc.hyperium.handlers.handlers.hud;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderHUDEvent;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.WorldSettings;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Map;

public class VanillaEnhancementsHud {


    /*
    Most of code adapted from Orange Marshall's Vanilla Enhancements
     */
    private Minecraft mc = Minecraft.getMinecraft();

    public VanillaEnhancementsHud() {
        EventBus.INSTANCE.register(new NetworkInfo());
    }

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

    @InvokeEvent
    public void enchantments(final RenderHUDEvent e) {
        if (!Settings.ENCHANTMENTS_ABOVE_HOTBAR) {
            return;
        }
        final ItemStack heldItemStack = this.mc.thePlayer.inventory.getCurrentItem();
        if (heldItemStack != null) {
            String toDraw = "";
            if (heldItemStack.getItem() instanceof ItemPotion) {
                toDraw = this.getPotionEffectString(heldItemStack);
            }
            else {
                toDraw = this.getEnchantmentString(heldItemStack);
            }
            GL11.glPushMatrix();
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            final ScaledResolution res =ResolutionUtil.current();
            int y = res.getScaledHeight() - 59;
            y += (this.mc.playerController.shouldDrawHUD() ? -2 : 14);
            y = y + this.mc.fontRendererObj.FONT_HEIGHT;
            y <<= 1;
            final int x = res.getScaledWidth() - (this.mc.fontRendererObj.getStringWidth(toDraw) >> 1);
            this.mc.fontRendererObj.drawString(toDraw, x, y, 13421772);
            GL11.glScalef(2.0f, 2.0f, 2.0f);
            GL11.glPopMatrix();
        }
    }

    @InvokeEvent
    public void onRenderGameOverlayText(RenderHUDEvent e) {
        if (!Settings.DAMAGE_ABOVE_HOTBAR) {
            return;
        }
        final ItemStack heldItemStack = this.mc.thePlayer.inventory.getCurrentItem();
        if (heldItemStack != null) {
            GL11.glPushMatrix();
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            final ScaledResolution res = ResolutionUtil.current();
            final String attackDamage = this.getAttackDamageString(heldItemStack);
            int y = res.getScaledHeight() - 59;
            y += (this.mc.playerController.shouldDrawHUD() ? -1 : 14);
            y = y + this.mc.fontRendererObj.FONT_HEIGHT;
            y <<= 1;
            y += this.mc.fontRendererObj.FONT_HEIGHT;
            final int x = res.getScaledWidth() - (this.mc.fontRendererObj.getStringWidth(attackDamage) >> 1);
            this.mc.fontRendererObj.drawString(attackDamage, x, y, 13421772);
            GL11.glScalef(2.0f, 2.0f, 2.0f);
            GL11.glPopMatrix();
        }
    }

    private String getAttackDamageString(final ItemStack stack) {
        for (final String entry : stack.getTooltip(this.mc.thePlayer, true)) {
            if (entry.endsWith("Attack Damage")) {
                return entry.split(" ", 2)[0].substring(2);
            }
        }
        return "";
    }
    private String getPotionEffectString(final ItemStack heldItemStack) {
        final ItemPotion potion = (ItemPotion)heldItemStack.getItem();
        final List<PotionEffect> effects = potion.getEffects(heldItemStack);
        if (effects == null) {
            return "";
        }
        final StringBuilder potionBuilder = new StringBuilder();
        for (final PotionEffect entry : effects) {
            final int duration = entry.getDuration() / 20;
            potionBuilder.append(EnumChatFormatting.BOLD.toString());
            potionBuilder.append(StatCollector.translateToLocal(entry.getEffectName()));
            potionBuilder.append(" ");
            potionBuilder.append(entry.getAmplifier() + 1);
            potionBuilder.append(" ");
            potionBuilder.append("(");
            potionBuilder.append(duration / 60).append(String.format(":%02d", duration % 60));
            potionBuilder.append(") ");
        }
        return potionBuilder.toString().trim();
    }

    private String getEnchantmentString(final ItemStack heldItemStack) {
        final StringBuilder enchantBuilder = new StringBuilder();
        final Map<Integer, Integer> en = EnchantmentHelper.getEnchantments(heldItemStack);
        for (final Map.Entry<Integer, Integer> entry : en.entrySet()) {
            enchantBuilder.append(EnumChatFormatting.BOLD.toString());
            enchantBuilder.append(cc.hyperium.handlers.handlers.hud.Maps.ENCHANTMENT_SHORT_NAME.get(entry.getKey()));
            enchantBuilder.append(" ");
            enchantBuilder.append(entry.getValue());
            enchantBuilder.append(" ");
        }
        return enchantBuilder.toString().trim();
    }
}
