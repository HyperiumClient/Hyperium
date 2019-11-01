/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.handlers.handlers.hud;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.gui.GuiDrawScreenEvent;
import cc.hyperium.event.render.RenderHUDEvent;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.WorldSettings;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
                if (heldItem != null && heldItem.getUnlocalizedName().equalsIgnoreCase("item.bow")) {
                    int c = Arrays.stream(thePlayer.inventory.mainInventory).filter(Objects::nonNull).filter(is ->
                        is.getUnlocalizedName().equalsIgnoreCase("item.arrow")).mapToInt(is -> is.stackSize).sum();
                    ScaledResolution current = ResolutionUtil.current();
                    FontRenderer fontRendererObj = mc.fontRendererObj;
                    int offset = (mc.playerController.getCurrentGameType() == WorldSettings.GameType.CREATIVE) ? 10 : 0;
                    mc.fontRendererObj.drawString(Integer.toString(c), (float) (current.getScaledWidth() -
                        fontRendererObj.getStringWidth(Integer.toString(c)) >> 1), (float) (current.getScaledHeight() - 46 - offset), 16777215, true);
                }
            }
        }
    }

    @InvokeEvent
    public void renderEnchantments(RenderHUDEvent e) {
        if (Settings.ENCHANTMENTS_ABOVE_HOTBAR) {
            ItemStack heldItemStack = mc.thePlayer.inventory.getCurrentItem();
            if (heldItemStack != null) {
                String toDraw = heldItemStack.getItem() instanceof ItemPotion ? getPotionEffectString(heldItemStack) : getEnchantmentString(heldItemStack);
                GL11.glPushMatrix();
                GL11.glScalef(0.5f, 0.5f, 0.5f);
                ScaledResolution res = ResolutionUtil.current();
                int y = res.getScaledHeight() - 59;
                y += (mc.playerController.shouldDrawHUD() ? -2 : 14);
                y = y + mc.fontRendererObj.FONT_HEIGHT;
                y <<= 1;
                int x = res.getScaledWidth() - (mc.fontRendererObj.getStringWidth(toDraw) >> 1);
                mc.fontRendererObj.drawString(toDraw, x, y, 13421772);
                GL11.glScalef(2.0f, 2.0f, 2.0f);
                GL11.glPopMatrix();
            }
        }
    }

    @InvokeEvent
    public void renderDamage(RenderHUDEvent e) {
        if (Settings.DAMAGE_ABOVE_HOTBAR) {
            ItemStack heldItemStack = mc.thePlayer.inventory.getCurrentItem();
            if (heldItemStack != null) {
                GL11.glPushMatrix();
                GL11.glScalef(0.5f, 0.5f, 0.5f);
                ScaledResolution res = ResolutionUtil.current();
                String attackDamage = getAttackDamageString(heldItemStack);
                int y = res.getScaledHeight() - 59;
                y += (mc.playerController.shouldDrawHUD() ? -1 : 14);
                y = y + mc.fontRendererObj.FONT_HEIGHT;
                y <<= 1;
                y += mc.fontRendererObj.FONT_HEIGHT;
                int x = res.getScaledWidth() - (mc.fontRendererObj.getStringWidth(attackDamage) >> 1);
                mc.fontRendererObj.drawString(attackDamage, x, y, 13421772);
                GL11.glScalef(2.0f, 2.0f, 2.0f);
                GL11.glPopMatrix();
            }
        }
    }

    @InvokeEvent
    public void onRenderArmor(GuiDrawScreenEvent e) {
        if ((Settings.ARMOR_PROT_POTENTIONAL || Settings.ARMOR_PROJ_POTENTIONAL) && (e.getScreen() instanceof GuiInventory || e.getScreen() instanceof GuiContainerCreative)) {
            ScaledResolution res = new ScaledResolution(mc);
            String message = getArmorString();
            mc.currentScreen.drawString(mc.fontRendererObj, message, 10, res.getScaledHeight() - 16, -1);
        }
    }

    @InvokeEvent
    public void renderHotbarNumbers(RenderHUDEvent event) {
        if (Settings.HOTBAR_KEYS) {
            ScaledResolution resolution = ResolutionUtil.current();
            int x = resolution.getScaledWidth() / 2 - 87;
            int y = resolution.getScaledHeight() - 18;
            int[] hotbarKeys = getHotbarKeys();
            IntStream.range(0, 9).forEach(slot -> mc.fontRendererObj.drawString(getKeyString(hotbarKeys[slot]), x + slot * 20, y, -1));
        }
    }

    private String getAttackDamageString(ItemStack stack) {
        return stack.getTooltip(mc.thePlayer, true).stream().filter(entry ->
            entry.endsWith("Attack Damage")).findFirst().map(entry -> entry.split(" ", 2)[0].substring(2)).orElse("");
    }

    private String getPotionEffectString(ItemStack heldItemStack) {
        ItemPotion potion = (ItemPotion) heldItemStack.getItem();
        List<PotionEffect> effects = potion.getEffects(heldItemStack);
        if (effects == null) return "";

        StringBuilder potionBuilder = new StringBuilder();

        effects.forEach(entry -> {
            int duration = entry.getDuration() / 20;
            potionBuilder.append(EnumChatFormatting.BOLD.toString());
            potionBuilder.append(StatCollector.translateToLocal(entry.getEffectName()));
            potionBuilder.append(" ");
            potionBuilder.append(entry.getAmplifier() + 1);
            potionBuilder.append(" ");
            potionBuilder.append("(");
            potionBuilder.append(duration / 60).append(String.format(":%02d", duration % 60));
            potionBuilder.append(") ");
        });

        return potionBuilder.toString().trim();
    }

    private String getEnchantmentString(ItemStack heldItemStack) {
        String enchantBuilder;
        Map<Integer, Integer> en = EnchantmentHelper.getEnchantments(heldItemStack);
        enchantBuilder = en.entrySet().stream().map(entry -> ChatColor.BOLD.toString() + Maps.ENCHANTMENT_SHORT_NAME.get(entry.getKey()) +
            " " + entry.getValue() + " ").collect(Collectors.joining());
        return enchantBuilder.trim();
    }

    private String getArmorString() {
        double ap = roundDecimals(getArmorPotentional(false));
        double app = roundDecimals(getArmorPotentional(true));
        return Settings.ARMOR_PROT_POTENTIONAL || Settings.ARMOR_PROJ_POTENTIONAL ? Settings.ARMOR_PROT_POTENTIONAL ?
            ap + "%" : app + "%" : ap == app ? ap + "%" : ap + "% | " + app + "%";

    }

    private double roundDecimals(double num) {
        if (num == 0.0) return num;
        num = (int) (num * Math.pow(10.0, 2));
        num /= Math.pow(10.0, 2);
        return num;
    }

    private double getArmorPotentional(boolean getProj) {
        EntityPlayer player = mc.thePlayer;
        double armor = 0.0;
        int epf = 0;
        int resistance = 0;
        if (player.isPotionActive(Potion.resistance)) {
            resistance = player.getActivePotionEffect(Potion.resistance).getAmplifier() + 1;
        }

        for (ItemStack stack : player.inventory.armorInventory) {
            if (stack != null) {
                if (stack.getItem() instanceof ItemArmor) {
                    ItemArmor armorItem = (ItemArmor) stack.getItem();
                    armor += armorItem.damageReduceAmount * 0.04;
                }

                if (stack.isItemEnchanted()) {
                    epf += getEffProtPoints(EnchantmentHelper.getEnchantmentLevel(0, stack));
                }

                if (getProj && stack.isItemEnchanted()) {
                    epf += getEffProtPoints(EnchantmentHelper.getEnchantmentLevel(4, stack));
                }
            }
        }

        epf = (Math.min(epf, 25));
        double avgDef = addArmorProtResistance(armor, calcProtection(epf), resistance);
        return roundDouble(avgDef * 100.0);
    }

    private int getEffProtPoints(int level) {
        return (level != 0) ? ((int) Math.floor((6 + level * level) * 0.75 / 3.0)) : 0;
    }

    private double calcProtection(int armorEpf) {
        double protection = IntStream.rangeClosed(50, 100).mapToDouble(i -> (Math.min(Math.ceil(armorEpf * i / 100.0), 20.0))).sum();
        return protection / 51.0;
    }

    private double addArmorProtResistance(double armor, double prot, int resistance) {
        double protTotal = armor + (1.0 - armor) * prot * 0.04;
        protTotal += (1.0 - protTotal) * resistance * 0.2;
        return Math.min(protTotal, 1.0);
    }

    private double roundDouble(double number) {
        double x = Math.round(number * 10000.0);
        return x / 10000.0;
    }

    private String getKeyString(int key) {
        return (key < 0) ? ("M" + (key + 101)) : ((key < 256) ? Keyboard.getKeyName(key) : String.format("%c", (char) (key - 256)).toUpperCase());
    }

    private int[] getHotbarKeys() {
        int[] result = new int[9];
        KeyBinding[] hotbarBindings = getGameSettings().keyBindsHotbar;
        IntStream.range(0, Math.min(result.length, hotbarBindings.length)).forEach(i -> result[i] = hotbarBindings[i].getKeyCode());
        return result;
    }

    private GameSettings getGameSettings() {
        return mc.gameSettings;
    }
}
