package cc.hyperium.utils.protocol

import cc.hyperium.mods.sk1ercommon.Multithreading
import net.minecraft.client.multiplayer.ServerAddress
import net.minecraft.network.EnumConnectionState
import net.minecraft.network.NetworkManager
import net.minecraft.network.handshake.client.C00Handshake
import net.minecraft.network.status.client.C00PacketServerQuery
import java.net.InetAddress
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap

object ProtocolDetector {
    private val futures: MutableMap<Pair<String, Int>, CompletableFuture<Boolean>> =
        ConcurrentHashMap<Pair<String, Int>, CompletableFuture<Boolean>>()

    fun isCompatibleWithVersion(ip: String, version: Int): CompletableFuture<Boolean> {
        val cached = futures[Pair(ip, version)]
        if (cached != null) return cached
        val future = CompletableFuture<Boolean>()
        val pair = Pair(ip, version)
        futures[pair] = future
        Multithreading.runAsync {
            try {
                val address = ServerAddress.fromString(ip)
                val nm = NetworkManager.createNetworkManagerAndConnect(
                    InetAddress.getByName(address.ip),
                    address.port,
                    false
                )
                nm.setNetHandler { VersionCompatibilityStatusState(future, version, nm) { futures.remove(pair) } }
                nm.sendPacket(C00Handshake(version, address.ip, address.port, EnumConnectionState.STATUS))
                nm.sendPacket(C00PacketServerQuery())
            } catch (exception: Exception) {
                future.completeExceptionally(exception)
                futures.remove(pair)
            }
        }

        return future
    }
}