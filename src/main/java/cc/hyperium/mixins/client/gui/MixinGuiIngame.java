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

package cc.hyperium.mixins.client.gui;

import cc.hyperium.config.Settings;
import cc.hyperium.mixinsimp.client.gui.HyperiumGuiIngame;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.FoodStats;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;
import java.util.stream.IntStream;

@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame extends Gui {

    @Shadow
    @Final
    private Minecraft mc;

    @Shadow
    public abstract FontRenderer getFontRenderer();

    @Shadow
    @Final
    private Random rand;
    @Shadow
    private int updateCounter;
    @Shadow
    private int lastPlayerHealth;
    @Shadow
    private int playerHealth;
    @Shadow
    private long lastSystemTime;
    @Shadow
    private long healthUpdateCounter;

    private HyperiumGuiIngame hyperiumGuiIngame = new HyperiumGuiIngame((GuiIngame) (Object) this);

    @Inject(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
    private void renderGameOverlay(float partialTicks, CallbackInfo ci) {
        hyperiumGuiIngame.renderGameOverlay(partialTicks);
    }

    @Inject(method = "renderSelectedItem", at = @At(value = "RETURN", target = "Lnet/minecraft/client/renderer/GlStateManager;popMatrix()V"))
    private void onRenderSelectedItem(ScaledResolution resolution, CallbackInfo ci) {
        hyperiumGuiIngame.renderSelectedItem(resolution);
    }

    /**
     * @author SiroQ
     * @reason Add 1.7 health
     */
    @Overwrite
    private void renderPlayerStats(ScaledResolution resolution) {
        if (mc.getRenderViewEntity() instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) mc.getRenderViewEntity();
            int currentHealth = MathHelper.ceiling_float_int(entityplayer.getHealth());
            boolean isGettingDamage = healthUpdateCounter > (long) updateCounter && (healthUpdateCounter - (long) updateCounter) / 3L % 2L == 1L;

            if (currentHealth < playerHealth && entityplayer.hurtResistantTime > 0) {
                lastSystemTime = Minecraft.getSystemTime();
                healthUpdateCounter = updateCounter + 20;
            } else if (currentHealth > playerHealth && entityplayer.hurtResistantTime > 0) {
                lastSystemTime = Minecraft.getSystemTime();
                healthUpdateCounter = updateCounter + 10;
            }

            if (Minecraft.getSystemTime() - lastSystemTime > 1000L) {
                playerHealth = currentHealth;
                lastPlayerHealth = currentHealth;
                lastSystemTime = Minecraft.getSystemTime();
            }

            playerHealth = currentHealth;
            int lastPlayerHealth = this.lastPlayerHealth;
            rand.setSeed(updateCounter * 312871);
            boolean alwaysFalseFlagWhatIsThis = false;
            FoodStats foodstats = entityplayer.getFoodStats();
            int foodLevel = foodstats.getFoodLevel();
            int prevFoodLevel = foodstats.getPrevFoodLevel();
            IAttributeInstance iattributeinstance = entityplayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
            int widthLeft = resolution.getScaledWidth() / 2 - 91;
            int widthRight = resolution.getScaledWidth() / 2 + 91;
            int height = resolution.getScaledHeight() - 39;
            float attributeValue = (float) iattributeinstance.getAttributeValue();
            float absorptionAmount = entityplayer.getAbsorptionAmount();
            int extraHealth = MathHelper.ceiling_float_int((attributeValue + absorptionAmount) / 2.0F / 10.0F);
            int extraHeart = Math.max(10 - (extraHealth - 2), 3);
            int heartHeight = height - (extraHealth - 1) * extraHeart - 10;
            float tempAbsorptionAmount = absorptionAmount;
            int armorValue = entityplayer.getTotalArmorValue();
            int regeneration = -1;

            if (entityplayer.isPotionActive(Potion.regeneration)) {
                regeneration = updateCounter % MathHelper.ceiling_float_int(attributeValue + 5.0F);
            }

            mc.mcProfiler.startSection("armor");

            if (HyperiumGuiIngame.renderArmor) {
                for (int armorPosition = 0; armorPosition < 10; armorPosition++) {
                    if (armorValue > 0) {
                        int j3 = widthLeft + armorPosition * 8;
                        if (armorPosition * 2 + 1 < armorValue) {
                            drawTexturedModalRect(j3, heartHeight, 34, 9, 9, 9);
                        }

                        if (armorPosition * 2 + 1 == armorValue) {
                            drawTexturedModalRect(j3, heartHeight, 25, 9, 9, 9);
                        }

                        if (armorPosition * 2 + 1 > armorValue) {
                            drawTexturedModalRect(j3, heartHeight, 16, 9, 9, 9);
                        }
                    }
                }
            }

            mc.mcProfiler.endStartSection("health");

            for (int healthHeartAmount = MathHelper.ceiling_float_int((attributeValue + absorptionAmount) / 2.0F) - 1; healthHeartAmount >= 0; --healthHeartAmount) {
                int baseTextureX = 16;

                if (entityplayer.isPotionActive(Potion.poison)) {
                    baseTextureX += 36;
                } else if (entityplayer.isPotionActive(Potion.wither)) {
                    baseTextureX += 72;
                }

                int gettingDamage = 0;
                if (isGettingDamage) gettingDamage = 1;

                int healthInt = MathHelper.ceiling_float_int((float) (healthHeartAmount + 1) / 10.0F) - 1;
                int healthWidth = widthLeft + healthHeartAmount % 10 * 8;
                int healthHeight = height - healthInt * extraHeart;
                if (currentHealth <= 4) healthHeight += rand.nextInt(2);
                if (healthHeartAmount == regeneration) healthHeight -= 2;
                int hardCore = 0;

                if (entityplayer.worldObj.getWorldInfo().isHardcoreModeEnabled()) hardCore = 5;

                if (HyperiumGuiIngame.renderHealth) {
                    drawTexturedModalRect(healthWidth, healthHeight, 16 + gettingDamage * 9, 9 * hardCore, 9, 9);
                    if (!Settings.OLD_HEALTH && isGettingDamage) {
                        if (healthHeartAmount * 2 + 1 < lastPlayerHealth) {
                            drawTexturedModalRect(healthWidth, healthHeight, baseTextureX + 54, 9 * hardCore, 9, 9);
                        }

                        if (healthHeartAmount * 2 + 1 == lastPlayerHealth) {
                            drawTexturedModalRect(healthWidth, healthHeight, baseTextureX + 63, 9 * hardCore, 9, 9);
                        }
                    }

                    if (tempAbsorptionAmount > 0.0F) {
                        drawTexturedModalRect(healthWidth, healthHeight, tempAbsorptionAmount == absorptionAmount && absorptionAmount % 2.0F
                            == 1.0F ? baseTextureX + 153 : baseTextureX + 144, 9 * hardCore, 9, 9);
                        tempAbsorptionAmount -= 2.0F;
                    } else {
                        if (healthHeartAmount * 2 + 1 < currentHealth) {
                            drawTexturedModalRect(healthWidth, healthHeight, baseTextureX + 36, 9 * hardCore, 9, 9);
                        }

                        if (healthHeartAmount * 2 + 1 == currentHealth) {
                            drawTexturedModalRect(healthWidth, healthHeight, baseTextureX + 45, 9 * hardCore, 9, 9);
                        }
                    }
                }
            }

            if (HyperiumGuiIngame.renderFood) {
                Entity entity = entityplayer.ridingEntity;
                if (entity == null) {
                    mc.mcProfiler.endStartSection("food");
                    for (int foodPostion = 0; foodPostion < 10; ++foodPostion) {
                        int foodHeight = height;
                        int textureXT = 16;
                        int textureX = 0;
                        if (entityplayer.isPotionActive(Potion.hunger)) {
                            textureXT += 36;
                            textureX = 13;
                        }

                        if (entityplayer.getFoodStats().getSaturationLevel() <= 0.0F && updateCounter % (foodLevel * 3 + 1) == 0) {
                            foodHeight = height + (rand.nextInt(3) - 1);
                        }

                        if (alwaysFalseFlagWhatIsThis) textureX = 1;

                        int foodPositionX = widthRight - foodPostion * 8 - 9;
                        drawTexturedModalRect(foodPositionX, foodHeight, 16 + textureX * 9, 27, 9, 9);

                        if (alwaysFalseFlagWhatIsThis) {
                            if (foodPostion * 2 + 1 < prevFoodLevel) {
                                drawTexturedModalRect(foodPositionX, foodHeight, textureXT + 54, 27, 9, 9);
                            }

                            if (foodPostion * 2 + 1 == prevFoodLevel) {
                                drawTexturedModalRect(foodPositionX, foodHeight, textureXT + 63, 27, 9, 9);
                            }
                        }

                        if (foodPostion * 2 + 1 < foodLevel) {
                            drawTexturedModalRect(foodPositionX, foodHeight, textureXT + 36, 27, 9, 9);
                        }

                        if (foodPostion * 2 + 1 == foodLevel) {
                            drawTexturedModalRect(foodPositionX, foodHeight, textureXT + 45, 27, 9, 9);
                        }
                    }
                } else if (entity instanceof EntityLivingBase) {
                    mc.mcProfiler.endStartSection("mountHealth");
                    EntityLivingBase entitylivingbase = (EntityLivingBase) entity;
                    int tempHealth = (int) Math.ceil(entitylivingbase.getHealth());
                    float maxHealth = entitylivingbase.getMaxHealth();
                    int maxHeart = (int) (maxHealth + 0.5F) / 2;
                    if (maxHeart > 30) maxHeart = 30;
                    int mountHealthHeight = height;

                    for (int tempInt = 0; maxHeart > 0; tempInt += 20) {
                        int heartInt = Math.min(maxHeart, 10);
                        maxHeart -= heartInt;

                        for (int mountHealth = 0; mountHealth < heartInt; ++mountHealth) {
                            int textureX = 52;
                            int additionalX = 0;

                            if (alwaysFalseFlagWhatIsThis) {
                                additionalX = 1;
                            }

                            int mountHealthPositionX = widthRight - mountHealth * 8 - 9;
                            drawTexturedModalRect(mountHealthPositionX, mountHealthHeight, textureX + additionalX * 9, 9, 9, 9);

                            if (mountHealth * 2 + 1 + tempInt < tempHealth) {
                                drawTexturedModalRect(mountHealthPositionX, mountHealthHeight, textureX + 36, 9, 9, 9);
                            }

                            if (mountHealth * 2 + 1 + tempInt == tempHealth) {
                                drawTexturedModalRect(mountHealthPositionX, mountHealthHeight, textureX + 45, 9, 9, 9);
                            }
                        }

                        mountHealthHeight -= 10;
                    }
                }
            }

            mc.mcProfiler.endStartSection("air");
            if (entityplayer.isInsideOfMaterial(Material.water)) {
                int air = mc.thePlayer.getAir();
                int airCheck = MathHelper.ceiling_double_int((double) (air - 2) * 10.0D / 300.0D);
                int air2 = MathHelper.ceiling_double_int((double) air * 10.0D / 300.0D) - airCheck;
                int bound = airCheck + air2;
                for (int airPosition = 0; airPosition < bound; airPosition++) {
                    if (airPosition < airCheck) {
                        drawTexturedModalRect(widthRight - airPosition * 8 - 9, heartHeight, 16, 18, 9, 9);
                    } else {
                        drawTexturedModalRect(widthRight - airPosition * 8 - 9, heartHeight, 25, 18, 9, 9);
                    }
                }
            }

            mc.mcProfiler.endSection();
        }
    }


    /**
     * @author ?
     * @reason For extra scoreboards
     */
    @Overwrite
    private void renderScoreboard(ScoreObjective objective, ScaledResolution resolution) {
        hyperiumGuiIngame.renderScoreboard(objective, resolution);
    }

    /**
     * @author - boomboompower
     * @reason - Add toggle for boss bar texture
     */
    @Overwrite
    private void renderBossHealth() {
        hyperiumGuiIngame.renderBossHealth();
    }

    /**
     * Disables the normal crosshair if custom crosshair is active.
     */
    @Inject(method = "showCrosshair", at = @At("HEAD"), cancellable = true)
    private void showCrosshair(CallbackInfoReturnable<Boolean> ci) {
        hyperiumGuiIngame.showCrosshair(ci);
    }

    /**
     * @author asbyth
     * @reason Option to disable Titles & Subtitles
     */
    @Inject(method = "displayTitle", at = @At("HEAD"), cancellable = true)
    private void displayTitle(String title, String subTitle, int timeFadeIn, int displayTime, int timeFadeOut, CallbackInfo ci) {
        if (Settings.HIDE_TITLES) ci.cancel();
    }
}
