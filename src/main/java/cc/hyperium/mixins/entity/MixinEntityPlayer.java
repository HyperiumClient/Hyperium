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

package cc.hyperium.mixins.entity;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.PlayerAttackEntityEvent;
import cc.hyperium.event.PlayerSwingEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends EntityLivingBase {

    private final float sneakingHeight = 1.54F;
    private final float standingHeight = 1.62F;
    @Shadow
    public float cameraYaw;
    private boolean last = false;
    private float currentHeight = 1.62F;
    private long lastChangeTime = System.currentTimeMillis();
    private int timeDelay = 1000 / 60;
    private IChatComponent cachedName;
    private long lastNameUpdate = 0L;

    public MixinEntityPlayer(World worldIn) {
        super(worldIn);
    }

    @Shadow
    public abstract boolean isPlayerSleeping();

    @Shadow
    public abstract Team getTeam();

    @Shadow
    public abstract String getName();

    @Inject(method = "updateEntityActionState", at = @At("RETURN"))
    private void onUpdate(CallbackInfo ci) {
        if (last != this.isSwingInProgress) {
            last = this.isSwingInProgress;
            if (this.isSwingInProgress) {
                EventBus.INSTANCE.post(
                        new PlayerSwingEvent(this.entityUniqueID, this.getPositionVector(), this.getLookVec(),
                                this.getPosition()));
            }
        }
    }

    @Inject(method = "attackTargetEntityWithCurrentItem", at = @At("HEAD"))
    public void attackTargetEntityWithCurrentItem(Entity targetEntity, CallbackInfo ci) {
        EventBus.INSTANCE.post(new PlayerAttackEntityEvent(this.entityUniqueID, targetEntity));
    }

    /**
     * @author CoalOres
     */
    @Overwrite
    public float getEyeHeight() {
        if (Settings.OLD_SNEAKING) {
            if (this.isSneaking()) {
                if (currentHeight > sneakingHeight) {
                    long time = System.currentTimeMillis();
                    long timeSinceLastChange = time - lastChangeTime;
                    if (timeSinceLastChange > timeDelay) {
                        currentHeight -= 0.012F;
                        lastChangeTime = time;
                    }
                }
            } else {
                if (currentHeight < standingHeight && currentHeight > 0.2F) {
                    long time = System.currentTimeMillis();
                    long timeSinceLastChange = time - lastChangeTime;
                    if (timeSinceLastChange > timeDelay) {
                        currentHeight += 0.012F;
                        lastChangeTime = time;
                    }
                } else {
                    currentHeight = 1.62F;
                }
            }

            if (this.isPlayerSleeping()) {
                currentHeight = 0.2F;
            }

            return currentHeight;
        } else {
            float f = 1.62F;

            if (this.isPlayerSleeping()) {
                f = 0.2F;
            }

            if (this.isSneaking()) {
                f -= 0.08F;
            }

            return f;
        }
    }

    @Overwrite
    public IChatComponent getDisplayName() {
        if (cachedName == null || System.currentTimeMillis() - lastChangeTime > 50L) {
            IChatComponent ichatcomponent = new ChatComponentText(ScorePlayerTeam.formatPlayerName(this.getTeam(), this.getName()));
            ichatcomponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + this.getName() + " "));
            //Unneeded for client
            if (Minecraft.getMinecraft().isIntegratedServerRunning())
                ichatcomponent.getChatStyle().setChatHoverEvent(this.getHoverEvent());
            ichatcomponent.getChatStyle().setInsertion(this.getName());
            this.cachedName = ichatcomponent;
            lastNameUpdate = System.currentTimeMillis();
        }
        return cachedName;
    }
}
