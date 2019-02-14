package cc.hyperium.handlers.handlers.hud;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.GuiDrawScreenEvent;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.RenderHUDEvent;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
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

import java.util.List;
import java.util.Map;

public class VanillaEnhancementsHud {


    /*
    Most of code adapted from Orange Marshall's Vanilla Enhancements
     */
    private Minecraft mc = Minecraft.getMinecraft();
    private String lastMessage = "";

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
    public void renderEnchantments(final RenderHUDEvent e) {
        if (Settings.ENCHANTMENTS_ABOVE_HOTBAR) {
            final ItemStack heldItemStack = this.mc.thePlayer.inventory.getCurrentItem();
            if (heldItemStack != null) {
                String toDraw;
                if (heldItemStack.getItem() instanceof ItemPotion) {
                    toDraw = this.getPotionEffectString(heldItemStack);
                } else {
                    toDraw = this.getEnchantmentString(heldItemStack);
                }
                GL11.glPushMatrix();
                GL11.glScalef(0.5f, 0.5f, 0.5f);
                final ScaledResolution res = ResolutionUtil.current();
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
    }

    @InvokeEvent
    public void renderDamage(RenderHUDEvent e) {
        if (Settings.DAMAGE_ABOVE_HOTBAR) {
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
    }

    @InvokeEvent
    public void onRenderArmor(GuiDrawScreenEvent e) {
        if (Settings.ARMOR_PROT_POTENTIONAL || Settings.ARMOR_PROJ_POTENTIONAL) {
            if (e.getScreen() instanceof GuiInventory || e.getScreen() instanceof GuiContainerCreative) {
                ScaledResolution res = new ScaledResolution(mc);
                int white = 16777215;
                String message = this.getArmorString();
                lastMessage = message;
                mc.currentScreen.drawString(mc.fontRendererObj, message, 10, res.getScaledHeight() - 16, white);
            }
        }
    }

    @InvokeEvent
    public void renderHotbarNumbers(RenderHUDEvent event) {
        if (Settings.HOTBAR_KEYS) {
            ScaledResolution resolution = ResolutionUtil.current();
            int x = resolution.getScaledWidth() / 2 - 87;
            int y = resolution.getScaledHeight() - 18;
            int[] hotbarKeys = getHotbarKeys();
            for (int slot = 0; slot < 9; ++slot) {
                mc.fontRendererObj.drawString(getKeyString(hotbarKeys[slot]), x + slot * 20, y, 16777215);
            }
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
        final ItemPotion potion = (ItemPotion) heldItemStack.getItem();
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

    private String getArmorString() {
        double ap = roundDecimals(getArmorPotentional(false), 2);
        double app = roundDecimals(getArmorPotentional(true), 2);
        if (Settings.ARMOR_PROT_POTENTIONAL || Settings.ARMOR_PROJ_POTENTIONAL) {
            String lastMessage;
            String str = Settings.ARMOR_PROT_POTENTIONAL ? (lastMessage = ap + "%") : (lastMessage = app + "%");
            this.lastMessage = lastMessage;
            return str;
        }
        if (ap == app) {
            return this.lastMessage = ap + "%";
        }
        return this.lastMessage = ap + "% | " + app + "%";
    }

    private double roundDecimals(double num, int a) {
        if (num == 0.0) {
            return num;
        }
        num = (int) (num * Math.pow(10.0, a));
        num /= Math.pow(10.0, a);
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
        epf = ((epf < 25) ? epf : 25);
        double avgDef = addArmorProtResistance(armor, calcProtection(epf), resistance);
        return roundDouble(avgDef * 100.0);
    }

    private int getEffProtPoints(int level) {
        return (level != 0) ? ((int) Math.floor((6 + level * level) * 0.75 / 3.0)) : 0;
    }

    private double calcProtection(int armorEpf) {
        double protection = 0.0;
        for (int i = 50; i <= 100; ++i) {
            protection += ((Math.ceil(armorEpf * i / 100.0) < 20.0) ? Math.ceil(armorEpf * i / 100.0) : 20.0);
        }
        return protection / 51.0;
    }

    private double addArmorProtResistance(double armor, double prot, int resistance) {
        double protTotal = armor + (1.0 - armor) * prot * 0.04;
        protTotal += (1.0 - protTotal) * resistance * 0.2;
        return (protTotal < 1.0) ? protTotal : 1.0;
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
        for (int i = 0; i < Math.min(result.length, hotbarBindings.length); ++i) {
            result[i] = hotbarBindings[i].getKeyCode();
          }

        return result;
    }

    private GameSettings getGameSettings() {
        return mc.gameSettings;
    }
}
