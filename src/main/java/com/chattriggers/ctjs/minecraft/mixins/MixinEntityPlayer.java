package com.chattriggers.ctjs.minecraft.mixins;

import com.chattriggers.ctjs.minecraft.imixins.IMixinEntityPlayer;
import net.minecraft.entity.Entity;
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

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends Entity implements IMixinEntityPlayer {
    @Shadow public abstract Team getTeam();
    @Shadow public abstract String getName();

    private String displayName = getName();

    public MixinEntityPlayer(World worldIn) {
        super(worldIn);
    }

    public void setDisplayName(String name) {
         this.displayName = name;
    }

    @Overwrite
    public IChatComponent getDisplayName() {
        IChatComponent ichatcomponent = new ChatComponentText(ScorePlayerTeam.formatPlayerName(getTeam(), displayName));
        ichatcomponent.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + this.getName() + " "));
        ichatcomponent.getChatStyle().setChatHoverEvent(getHoverEvent());
        ichatcomponent.getChatStyle().setInsertion(this.getName());
        return ichatcomponent;
    }
}
