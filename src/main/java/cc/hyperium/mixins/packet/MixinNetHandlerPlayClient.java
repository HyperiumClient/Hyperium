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

package cc.hyperium.mixins.packet;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.ServerChatEvent;
import cc.hyperium.mods.timechanger.TimeChanger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.util.EnumParticleTypes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Provides code that may be used in mods that require it
 *
 */
@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient {
    
    @Shadow private WorldClient clientWorldController;
    @Shadow
    private Minecraft gameController;
    
    private TimeChanger timeChanger = (TimeChanger) Hyperium.INSTANCE.getModIntegration().getTimeChanger();
    
    /**
     * For TimeChanger, changes the way time packets are handled
     *
     * @author boomboompower
     */
    @Overwrite
    public void handleTimeUpdate(S03PacketTimeUpdate packet) {
        switch (this.timeChanger.getTimeType()) {
            case DAY:
                handleActualPacket(new S03PacketTimeUpdate(packet.getWorldTime(), -6000L, true));
                break;
            case SUNSET:
                handleActualPacket(new S03PacketTimeUpdate(packet.getWorldTime(), -22880L, true));
                break;
            case NIGHT:
                handleActualPacket(new S03PacketTimeUpdate(packet.getWorldTime(), -18000L, true));
                break;
            case VANILLA:
                handleActualPacket(packet);
                break;
        }
    }
    
    /**
     * The actual logic of the packet, may be spoofed.
     *
     * @param packetIn the packet
     */
    private void handleActualPacket(S03PacketTimeUpdate packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn,
            (INetHandlerPlayClient) getNetworkManager().getNetHandler(), this.gameController);
        this.gameController.theWorld.setTotalWorldTime(packetIn.getTotalWorldTime());
        this.gameController.theWorld.setWorldTime(packetIn.getWorldTime());
    }
    
    /**
     * Renders a specified animation: Waking up a player, a living entity swinging its currently held item, being hurt
     * or receiving a critical hit by normal or magical means
     *
     * @author boomboompower
     * @reason Fixes internal NPE
     */
    @Overwrite
    public void handleAnimation(S0BPacketAnimation packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, (INetHandlerPlayClient) getNetworkManager().getNetHandler(), this.gameController);
        
        // Stops the code if the world is null, usually due to a weird packet from the server
        if (this.clientWorldController == null) {
            return;
        }
        
        Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityID());
        
        if (entity != null) {
            if (packetIn.getAnimationType() == 0) {
                EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
                entitylivingbase.swingItem();
            } else if (packetIn.getAnimationType() == 1) {
                entity.performHurtAnimation();
            } else if (packetIn.getAnimationType() == 2) {
                EntityPlayer entityplayer = (EntityPlayer)entity;
                entityplayer.wakeUpPlayer(false, false, false);
            } else if (packetIn.getAnimationType() == 4) {
                this.gameController.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT);
            } else if (packetIn.getAnimationType() == 5) {
                this.gameController.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT_MAGIC);
            }
        }
    }
    
    /**
     * Allows detection of incoming chat packets from the server (includes actionbars)
     *
     * Byte values for the event
     * 0 : Standard Text Message, displayed in chat
     * 1 : 'System' message, displayed as standard text in the chat.
     * 2 : 'Status' message, displayed as an action bar above the hotbar
     *
     * @author boomboompower
     * @reason Detect incoming chat packets being sent from the server
     */
    @Overwrite
    public void handleChat(S02PacketChat packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, (INetHandlerPlayClient) getNetworkManager().getNetHandler(), this.gameController);
        
        ServerChatEvent event = new ServerChatEvent(packetIn.getType(), packetIn.getChatComponent());
        
        EventBus.INSTANCE.post(event);
        
        // If the event is cancelled or the message is empty, we'll ignore the packet.
        if (event.isCancelled() || event.getChat().getFormattedText().isEmpty()) {
            return;
        }
        
        if (packetIn.getType() == 2) {
            this.gameController.ingameGUI.setRecordPlaying(event.getChat(), false);
        } else {
            // This will then trigger the other chat event
            this.gameController.ingameGUI.getChatGUI().printChatMessage(event.getChat());
        }
    }
    
    @Shadow
    public abstract NetworkManager getNetworkManager();
}
