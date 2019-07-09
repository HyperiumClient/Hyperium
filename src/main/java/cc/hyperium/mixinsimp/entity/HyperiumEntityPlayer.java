package cc.hyperium.mixinsimp.entity;

import cc.hyperium.config.Settings;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.LivingDeathEvent;
import cc.hyperium.event.PlayerAttackEntityEvent;
import cc.hyperium.event.PlayerSwingEvent;
import cc.hyperium.mixins.entity.IMixinEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;

public class HyperiumEntityPlayer {
    private EntityPlayer parent;

    private final float sneakingHeight = 1.54F;
    private final float standingHeight = 1.62F;
    private boolean last = false;
    private float currentHeight = 1.62F;
    private long lastChangeTime = System.currentTimeMillis();
    private int timeDelay = 1000 / 60;
    private IChatComponent cachedName;
    private long lastNameUpdate = 0L;
    private String displayName;

    public HyperiumEntityPlayer(EntityPlayer parent) {
        this.parent = parent;
    }

    public void onUpdate() {
        if (last != parent.isSwingInProgress) {
            last = parent.isSwingInProgress;
            if (parent.isSwingInProgress) {
                EventBus.INSTANCE.post(
                    new PlayerSwingEvent(parent.getUniqueID(), parent.getPositionVector(), parent.getLookVec(),
                        parent.getPosition()));
            }
        }
    }

    public float getEyeHeight() {
        if (Settings.OLD_SNEAKING) {
            if (parent.isSneaking()) {
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

            if (parent.isPlayerSleeping()) {
                currentHeight = 0.2F;
            }

            return currentHeight;
        } else {
            float f = 1.62F;

            if (parent.isPlayerSleeping()) {
                f = 0.2F;
            }

            if (parent.isSneaking()) {
                f -= 0.08F;
            }

            return f;
        }
    }

    public IChatComponent getDisplayName() {
        if (cachedName == null || System.currentTimeMillis() - lastChangeTime > 50L) {
            IChatComponent ichatcomponent = new ChatComponentText(ScorePlayerTeam
                .formatPlayerName(parent.getTeam(), displayName));
            ichatcomponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + parent.getName() + " "));
            //Unneeded for client
            if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
                ichatcomponent.getChatStyle()
                    .setChatHoverEvent(((IMixinEntity) parent).callGetHoverEvent());
            }
            ichatcomponent.getChatStyle().setInsertion(parent.getName());
            this.cachedName = ichatcomponent;
            lastNameUpdate = System.currentTimeMillis();
        }
        return cachedName;
    }

    public void attackTargetEntityWithCurrentItem(Entity targetEntity) {
        EventBus.INSTANCE.post(new PlayerAttackEntityEvent(parent.getUniqueID(), targetEntity));
    }

    public void onDeath(DamageSource source) {
        EventBus.INSTANCE.post(new LivingDeathEvent(parent, source));
    }

    public void setName(String name) {
        this.displayName = name;
    }
}
