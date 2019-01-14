/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mixins.gui;

import cc.hyperium.config.Settings;
import cc.hyperium.mixinsimp.gui.HyperiumGuiIngame;
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

@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame extends Gui {

    @Shadow
    @Final
    private Minecraft mc;
    private HyperiumGuiIngame hyperiumGuiIngame = new HyperiumGuiIngame((GuiIngame) (Object) this);

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

    @Inject(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
    private void renderGameOverlay(float partialTicks, CallbackInfo ci) {
        hyperiumGuiIngame.renderGameOverlay(partialTicks, ci);
    }

    @Inject(method = "renderSelectedItem", at = @At(value = "RETURN", target = "Lnet/minecraft/client/renderer/GlStateManager;popMatrix()V"))
    private void onRenderSelectedItem(ScaledResolution p_181551_1_, CallbackInfo ci) {
        hyperiumGuiIngame.renderSelectedItem(p_181551_1_);
    }

    /**
     * @author SiroQ
     * @reason Add 1.7 health
     */
    @Overwrite
    private void renderPlayerStats(ScaledResolution p_180477_1_) {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) this.mc.getRenderViewEntity();
            int currentHealth = MathHelper.ceiling_float_int(entityplayer.getHealth());
            boolean isGettingDamage = this.healthUpdateCounter > (long) this.updateCounter && (this.healthUpdateCounter - (long) this.updateCounter) / 3L % 2L == 1L;

            if (currentHealth < this.playerHealth && entityplayer.hurtResistantTime > 0) {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.healthUpdateCounter = (long) (this.updateCounter + 20);
            } else if (currentHealth > this.playerHealth && entityplayer.hurtResistantTime > 0) {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.healthUpdateCounter = (long) (this.updateCounter + 10);
            }

            if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L) {
                this.playerHealth = currentHealth;
                this.lastPlayerHealth = currentHealth;
                this.lastSystemTime = Minecraft.getSystemTime();
            }

            this.playerHealth = currentHealth;
            int lastPlayerHealth = this.lastPlayerHealth;
            this.rand.setSeed((long) (this.updateCounter * 312871));
            boolean alwaysFalseFlagWhatIsThis = false;
            FoodStats foodstats = entityplayer.getFoodStats();
            int foodLevel = foodstats.getFoodLevel();
            int prevFoodLevel = foodstats.getPrevFoodLevel();
            IAttributeInstance iattributeinstance = entityplayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
            int widthLeft = p_180477_1_.getScaledWidth() / 2 - 91;
            int widthRight = p_180477_1_.getScaledWidth() / 2 + 91;
            int height = p_180477_1_.getScaledHeight() - 39;
            float attributeValue = (float) iattributeinstance.getAttributeValue();
            float absorptionAmount = entityplayer.getAbsorptionAmount();
            int extraHealth = MathHelper.ceiling_float_int((attributeValue + absorptionAmount) / 2.0F / 10.0F);
            int extraHeart = Math.max(10 - (extraHealth - 2), 3);
            int heartHeight = height - (extraHealth - 1) * extraHeart - 10;
            float tempAbsorptionAmount = absorptionAmount;
            int armorValue = entityplayer.getTotalArmorValue();
            int regeneration = -1;

            if (entityplayer.isPotionActive(Potion.regeneration)) {
                regeneration = this.updateCounter % MathHelper.ceiling_float_int(attributeValue + 5.0F);
            }

            this.mc.mcProfiler.startSection("armor");

            for (int armorPosition = 0; armorPosition < 10; ++armorPosition) {
                if (armorValue > 0) {
                    int j3 = widthLeft + armorPosition * 8;
                    if (armorPosition * 2 + 1 < armorValue) {
                        this.drawTexturedModalRect(j3, heartHeight, 34, 9, 9, 9);
                    }

                    if (armorPosition * 2 + 1 == armorValue) {
                        this.drawTexturedModalRect(j3, heartHeight, 25, 9, 9, 9);
                    }

                    if (armorPosition * 2 + 1 > armorValue) {
                        this.drawTexturedModalRect(j3, heartHeight, 16, 9, 9, 9);
                    }
                }
            }

            this.mc.mcProfiler.endStartSection("health");

            for (int healthHeartAmount = MathHelper.ceiling_float_int((attributeValue + absorptionAmount) / 2.0F) - 1; healthHeartAmount >= 0; --healthHeartAmount) {
                int baseTextureX = 16;

                if (entityplayer.isPotionActive(Potion.poison)) {
                    baseTextureX += 36;
                } else if (entityplayer.isPotionActive(Potion.wither)) {
                    baseTextureX += 72;
                }
                int gettingDamage = 0;
                if (isGettingDamage) {
                    gettingDamage = 1;
                }
                int healthInt = MathHelper.ceiling_float_int((float) (healthHeartAmount + 1) / 10.0F) - 1;
                int healthWidth = widthLeft + healthHeartAmount % 10 * 8;
                int healthHeight = height - healthInt * extraHeart;
                if (currentHealth <= 4) {
                    healthHeight += this.rand.nextInt(2);
                }
                if (healthHeartAmount == regeneration) {
                    healthHeight -= 2;
                }
                int hardCore = 0;
                if (entityplayer.worldObj.getWorldInfo().isHardcoreModeEnabled()) {
                    hardCore = 5;
                }
                this.drawTexturedModalRect(healthWidth, healthHeight, 16 + gettingDamage * 9, 9 * hardCore, 9, 9);
                if (!Settings.OLD_HEALTH) {
                    if (isGettingDamage) {
                        if (healthHeartAmount * 2 + 1 < lastPlayerHealth) {
                            this.drawTexturedModalRect(healthWidth, healthHeight, baseTextureX + 54, 9 * hardCore, 9, 9);
                        }

                        if (healthHeartAmount * 2 + 1 == lastPlayerHealth) {
                            this.drawTexturedModalRect(healthWidth, healthHeight, baseTextureX + 63, 9 * hardCore, 9, 9);
                        }
                    }
                }

                if (tempAbsorptionAmount > 0.0F) {
                    if (tempAbsorptionAmount == absorptionAmount && absorptionAmount % 2.0F == 1.0F) {
                        this.drawTexturedModalRect(healthWidth, healthHeight, baseTextureX + 153, 9 * hardCore, 9, 9);
                    } else {
                        this.drawTexturedModalRect(healthWidth, healthHeight, baseTextureX + 144, 9 * hardCore, 9, 9);
                    }
                    tempAbsorptionAmount -= 2.0F;
                } else {
                    if (healthHeartAmount * 2 + 1 < currentHealth) {
                        this.drawTexturedModalRect(healthWidth, healthHeight, baseTextureX + 36, 9 * hardCore, 9, 9);
                    }

                    if (healthHeartAmount * 2 + 1 == currentHealth) {
                        this.drawTexturedModalRect(healthWidth, healthHeight, baseTextureX + 45, 9 * hardCore, 9, 9);
                    }
                }
            }
            Entity entity = entityplayer.ridingEntity;
            if (entity == null) {
                this.mc.mcProfiler.endStartSection("food");
                for (int foodPostion = 0; foodPostion < 10; ++foodPostion) {
                    int foodHeight = height;
                    int textureXT = 16;
                    int textureX = 0;
                    if (entityplayer.isPotionActive(Potion.hunger)) {
                        textureXT += 36;
                        textureX = 13;
                    }
                    if (entityplayer.getFoodStats().getSaturationLevel() <= 0.0F && this.updateCounter % (foodLevel * 3 + 1) == 0) {
                        foodHeight = height + (this.rand.nextInt(3) - 1);
                    }
                    if (alwaysFalseFlagWhatIsThis) {
                        textureX = 1;
                    }
                    int foodPositionX = widthRight - foodPostion * 8 - 9;
                    this.drawTexturedModalRect(foodPositionX, foodHeight, 16 + textureX * 9, 27, 9, 9);
                    if (alwaysFalseFlagWhatIsThis) {
                        if (foodPostion * 2 + 1 < prevFoodLevel) {
                            this.drawTexturedModalRect(foodPositionX, foodHeight, textureXT + 54, 27, 9, 9);
                        }
                        if (foodPostion * 2 + 1 == prevFoodLevel) {
                            this.drawTexturedModalRect(foodPositionX, foodHeight, textureXT + 63, 27, 9, 9);
                        }
                    }

                    if (foodPostion * 2 + 1 < foodLevel) {
                        this.drawTexturedModalRect(foodPositionX, foodHeight, textureXT + 36, 27, 9, 9);
                    }

                    if (foodPostion * 2 + 1 == foodLevel) {
                        this.drawTexturedModalRect(foodPositionX, foodHeight, textureXT + 45, 27, 9, 9);
                    }
                }
            } else if (entity instanceof EntityLivingBase) {
                this.mc.mcProfiler.endStartSection("mountHealth");
                EntityLivingBase entitylivingbase = (EntityLivingBase) entity;
                int tempHealth = (int) Math.ceil((double) entitylivingbase.getHealth());
                float maxHealth = entitylivingbase.getMaxHealth();
                int maxHeart = (int) (maxHealth + 0.5F) / 2;
                if (maxHeart > 30) {
                    maxHeart = 30;
                }
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
                        this.drawTexturedModalRect(mountHealthPositionX, mountHealthHeight, textureX + additionalX * 9, 9, 9, 9);
                        if (mountHealth * 2 + 1 + tempInt < tempHealth) {
                            this.drawTexturedModalRect(mountHealthPositionX, mountHealthHeight, textureX + 36, 9, 9, 9);
                        }
                        if (mountHealth * 2 + 1 + tempInt == tempHealth) {
                            this.drawTexturedModalRect(mountHealthPositionX, mountHealthHeight, textureX + 45, 9, 9, 9);
                        }
                    }
                    mountHealthHeight -= 10;
                }
            }
            this.mc.mcProfiler.endStartSection("air");
            if (entityplayer.isInsideOfMaterial(Material.water)) {
                int air = this.mc.thePlayer.getAir();
                int airCheck = MathHelper.ceiling_double_int((double) (air - 2) * 10.0D / 300.0D);
                int air2 = MathHelper.ceiling_double_int((double) air * 10.0D / 300.0D) - airCheck;
                for (int airPosition = 0; airPosition < airCheck + air2; ++airPosition) {
                    if (airPosition < airCheck) {
                        this.drawTexturedModalRect(widthRight - airPosition * 8 - 9, heartHeight, 16, 18, 9, 9);
                    } else {
                        this.drawTexturedModalRect(widthRight - airPosition * 8 - 9, heartHeight, 25, 18, 9, 9);
                    }
                }
            }
            this.mc.mcProfiler.endSection();
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
     *
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
    protected void showCrosshair(CallbackInfoReturnable<Boolean> ci) {
        hyperiumGuiIngame.showCrosshair(ci);
    }

    /**
     * @author asbyth
     * @reason Option to disable Titles & Subtitles (will be replacing the Clear Titles keybind)
     */
    @Inject(method = "displayTitle", at = @At("HEAD"), cancellable = true)
    private void displayTitle(String title, String subTitle, int timeFadeIn, int displayTime, int timeFadeOut, CallbackInfo ci) {
        if (Settings.HIDE_TITLES) {
            ci.cancel();
        }
    }
}
