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

    @Shadow @Final private Random rand;

    @Shadow private int updateCounter;

    @Shadow private int lastPlayerHealth;

    @Shadow private int playerHealth;

    @Shadow private long lastSystemTime;

    @Shadow private long healthUpdateCounter;

    @Inject(method = "renderGameOverlay", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
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
    private void renderPlayerStats(ScaledResolution p_180477_1_){
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            int i = MathHelper.ceiling_float_int(entityplayer.getHealth());
            boolean flag = this.healthUpdateCounter > (long)this.updateCounter && (this.healthUpdateCounter - (long)this.updateCounter) / 3L % 2L == 1L;

            if (i < this.playerHealth && entityplayer.hurtResistantTime > 0)
            {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.healthUpdateCounter = (long)(this.updateCounter + 20);
            }
            else if (i > this.playerHealth && entityplayer.hurtResistantTime > 0)
            {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.healthUpdateCounter = (long)(this.updateCounter + 10);
            }

            if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L)
            {
                this.playerHealth = i;
                this.lastPlayerHealth = i;
                this.lastSystemTime = Minecraft.getSystemTime();
            }

            this.playerHealth = i;
            int j = this.lastPlayerHealth;
            this.rand.setSeed((long)(this.updateCounter * 312871));
            boolean flag1 = false;
            FoodStats foodstats = entityplayer.getFoodStats();
            int k = foodstats.getFoodLevel();
            int l = foodstats.getPrevFoodLevel();
            IAttributeInstance iattributeinstance = entityplayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
            int i1 = p_180477_1_.getScaledWidth() / 2 - 91;
            int j1 = p_180477_1_.getScaledWidth() / 2 + 91;
            int k1 = p_180477_1_.getScaledHeight() - 39;
            float f = (float)iattributeinstance.getAttributeValue();
            float f1 = entityplayer.getAbsorptionAmount();
            int l1 = MathHelper.ceiling_float_int((f + f1) / 2.0F / 10.0F);
            int i2 = Math.max(10 - (l1 - 2), 3);
            int j2 = k1 - (l1 - 1) * i2 - 10;
            float f2 = f1;
            int k2 = entityplayer.getTotalArmorValue();
            int l2 = -1;

            if (entityplayer.isPotionActive(Potion.regeneration))
            {
                l2 = this.updateCounter % MathHelper.ceiling_float_int(f + 5.0F);
            }

            this.mc.mcProfiler.startSection("armor");

            for (int i3 = 0; i3 < 10; ++i3)
            {
                if (k2 > 0)
                {
                    int j3 = i1 + i3 * 8;

                    if (i3 * 2 + 1 < k2)
                    {
                        this.drawTexturedModalRect(j3, j2, 34, 9, 9, 9);
                    }

                    if (i3 * 2 + 1 == k2)
                    {
                        this.drawTexturedModalRect(j3, j2, 25, 9, 9, 9);
                    }

                    if (i3 * 2 + 1 > k2)
                    {
                        this.drawTexturedModalRect(j3, j2, 16, 9, 9, 9);
                    }
                }
            }

            this.mc.mcProfiler.endStartSection("health");

            for (int i6 = MathHelper.ceiling_float_int((f + f1) / 2.0F) - 1; i6 >= 0; --i6)
            {
                int j6 = 16;

                if (entityplayer.isPotionActive(Potion.poison))
                {
                    j6 += 36;
                }
                else if (entityplayer.isPotionActive(Potion.wither))
                {
                    j6 += 72;
                }

                int k3 = 0;

                if (flag)
                {
                    k3 = 1;
                }

                int l3 = MathHelper.ceiling_float_int((float)(i6 + 1) / 10.0F) - 1;
                int i4 = i1 + i6 % 10 * 8;
                int j4 = k1 - l3 * i2;

                if (i <= 4)
                {
                    j4 += this.rand.nextInt(2);
                }

                if (i6 == l2)
                {
                    j4 -= 2;
                }

                int k4 = 0;

                if (entityplayer.worldObj.getWorldInfo().isHardcoreModeEnabled())
                {
                    k4 = 5;
                }

                this.drawTexturedModalRect(i4, j4, 16 + k3 * 9, 9 * k4, 9, 9);

                if(!Settings.OLD_HEALTH) {
                    if (flag) {
                        if (i6 * 2 + 1 < j) {
                            this.drawTexturedModalRect(i4, j4, j6 + 54, 9 * k4, 9, 9);
                        }

                        if (i6 * 2 + 1 == j) {
                            this.drawTexturedModalRect(i4, j4, j6 + 63, 9 * k4, 9, 9);
                        }
                    }
                }

                if (f2 > 0.0F)
                {
                    if (f2 == f1 && f1 % 2.0F == 1.0F)
                    {
                        this.drawTexturedModalRect(i4, j4, j6 + 153, 9 * k4, 9, 9);
                    }
                    else
                    {
                        this.drawTexturedModalRect(i4, j4, j6 + 144, 9 * k4, 9, 9);
                    }

                    f2 -= 2.0F;
                }
                else
                {
                    if (i6 * 2 + 1 < i)
                    {
                        this.drawTexturedModalRect(i4, j4, j6 + 36, 9 * k4, 9, 9);
                    }

                    if (i6 * 2 + 1 == i)
                    {
                        this.drawTexturedModalRect(i4, j4, j6 + 45, 9 * k4, 9, 9);
                    }
                }
            }

            Entity entity = entityplayer.ridingEntity;

            if (entity == null)
            {
                this.mc.mcProfiler.endStartSection("food");

                for (int k6 = 0; k6 < 10; ++k6)
                {
                    int i7 = k1;
                    int l7 = 16;
                    int j8 = 0;

                    if (entityplayer.isPotionActive(Potion.hunger))
                    {
                        l7 += 36;
                        j8 = 13;
                    }

                    if (entityplayer.getFoodStats().getSaturationLevel() <= 0.0F && this.updateCounter % (k * 3 + 1) == 0)
                    {
                        i7 = k1 + (this.rand.nextInt(3) - 1);
                    }

                    if (flag1)
                    {
                        j8 = 1;
                    }

                    int i9 = j1 - k6 * 8 - 9;
                    this.drawTexturedModalRect(i9, i7, 16 + j8 * 9, 27, 9, 9);

                    if (flag1)
                    {
                        if (k6 * 2 + 1 < l)
                        {
                            this.drawTexturedModalRect(i9, i7, l7 + 54, 27, 9, 9);
                        }

                        if (k6 * 2 + 1 == l)
                        {
                            this.drawTexturedModalRect(i9, i7, l7 + 63, 27, 9, 9);
                        }
                    }

                    if (k6 * 2 + 1 < k)
                    {
                        this.drawTexturedModalRect(i9, i7, l7 + 36, 27, 9, 9);
                    }

                    if (k6 * 2 + 1 == k)
                    {
                        this.drawTexturedModalRect(i9, i7, l7 + 45, 27, 9, 9);
                    }
                }
            }
            else if (entity instanceof EntityLivingBase)
            {
                this.mc.mcProfiler.endStartSection("mountHealth");
                EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
                int j7 = (int)Math.ceil((double)entitylivingbase.getHealth());
                float f3 = entitylivingbase.getMaxHealth();
                int k8 = (int)(f3 + 0.5F) / 2;

                if (k8 > 30)
                {
                    k8 = 30;
                }

                int j9 = k1;

                for (int k9 = 0; k8 > 0; k9 += 20)
                {
                    int l4 = Math.min(k8, 10);
                    k8 -= l4;

                    for (int i5 = 0; i5 < l4; ++i5)
                    {
                        int j5 = 52;
                        int k5 = 0;

                        if (flag1)
                        {
                            k5 = 1;
                        }

                        int l5 = j1 - i5 * 8 - 9;
                        this.drawTexturedModalRect(l5, j9, j5 + k5 * 9, 9, 9, 9);

                        if (i5 * 2 + 1 + k9 < j7)
                        {
                            this.drawTexturedModalRect(l5, j9, j5 + 36, 9, 9, 9);
                        }

                        if (i5 * 2 + 1 + k9 == j7)
                        {
                            this.drawTexturedModalRect(l5, j9, j5 + 45, 9, 9, 9);
                        }
                    }

                    j9 -= 10;
                }
            }

            this.mc.mcProfiler.endStartSection("air");

            if (entityplayer.isInsideOfMaterial(Material.water))
            {
                int l6 = this.mc.thePlayer.getAir();
                int k7 = MathHelper.ceiling_double_int((double)(l6 - 2) * 10.0D / 300.0D);
                int i8 = MathHelper.ceiling_double_int((double)l6 * 10.0D / 300.0D) - k7;

                for (int l8 = 0; l8 < k7 + i8; ++l8)
                {
                    if (l8 < k7)
                    {
                        this.drawTexturedModalRect(j1 - l8 * 8 - 9, j2, 16, 18, 9, 9);
                    }
                    else
                    {
                        this.drawTexturedModalRect(j1 - l8 * 8 - 9, j2, 25, 18, 9, 9);
                    }
                }
            }

            this.mc.mcProfiler.endSection();
        }
    }


    @Overwrite
    private void renderScoreboard(ScoreObjective objective, ScaledResolution resolution) {
        hyperiumGuiIngame.renderScoreboard(objective, resolution);
    }

    /**
     * Add toggle for boss bar texture
     *
     * @author boomboompower
     * @reason cool things
     */
    @Overwrite
    private void renderBossHealth() {
        hyperiumGuiIngame.renderBossHealth();
    }

    /**
     * Disables the normal crosshair if custom crosshair is active.
     */
    @Inject(method = "showCrosshair",at=@At("HEAD"),cancellable = true)
    protected void showCrosshair(CallbackInfoReturnable<Boolean> ci) {
        hyperiumGuiIngame.showCrosshair(ci);
    }
}
