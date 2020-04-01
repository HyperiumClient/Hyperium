package cc.hyperium.utils.protocol

import net.minecraft.network.NetworkManager
import net.minecraft.network.status.INetHandlerStatusClient
import net.minecraft.network.status.server.S00PacketServerInfo
import net.minecraft.network.status.server.S01PacketPong
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent
import java.util.concurrent.CompletableFuture

class VersionCompatibilityStatusState(
    val future: CompletableFuture<Boolean>,
    val version: Int,
    val manager: NetworkManager,
    val onComplete: () -> Unit
) : INetHandlerStatusClient {
    var received = false

    override fun onDisconnect(reason: IChatComponent) {
        if (future.isDone) return
        future.complete(false)
        onComplete()
    }

    override fun handleServerInfo(packetIn: S00PacketServerInfo) {
        if (received) {
            manager.closeChannel(ChatComponentText("Done checking protocol versions."))
            return
        }

        received = true
        future.complete(packetIn.response.protocolVersionInfo.protocol >= version)
        onComplete()
    }

    override fun handlePong(packetIn: S01PacketPong) {}
}