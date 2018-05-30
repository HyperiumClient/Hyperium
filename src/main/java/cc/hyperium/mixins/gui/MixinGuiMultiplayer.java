package cc.hyperium.mixins.gui;

import cc.hyperium.mixinsimp.renderer.gui.IMixinGuiMultiplayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.play.server.S40PacketDisconnect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMultiplayer.class)
public abstract class MixinGuiMultiplayer implements IMixinGuiMultiplayer {
    @Shadow
    private boolean directConnect;
    @Shadow
    private ServerData selectedServer;

    @Shadow private GuiScreen parentScreen;

    @Shadow protected abstract void connectToServer(ServerData server);

    @Shadow private ServerSelectionList serverListSelector;
    @Override
    public void makeDirectConnect() {
        directConnect = true;
    }

    @Override
    public void setIp(ServerData ip) {
        this.selectedServer = ip;
    }

}
