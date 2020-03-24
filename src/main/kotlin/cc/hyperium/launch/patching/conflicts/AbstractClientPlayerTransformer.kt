package cc.hyperium.launch.patching.conflicts

import cc.hyperium.event.Event
import cc.hyperium.event.EventBus
import cc.hyperium.event.entity.FovUpdateEvent
import cc.hyperium.handlers.handlers.animation.cape.HyperiumCapeHandler
import cc.hyperium.mods.nickhider.NickHider
import cc.hyperium.mods.nickhider.config.NickHiderConfig
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import com.mojang.authlib.GameProfile
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.resources.DefaultPlayerSkin
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import java.util.*

class AbstractClientPlayerTransformer : ConflictTransformer {
    override fun getClassName() = "bet"

    override fun transform(original: ClassNode): ClassNode {
        original.visitField(
                Opcodes.ACC_PRIVATE,
                "hook",
                "Lcc/hyperium/handlers/handlers/animation/cape/HyperiumCapeHandler;",
                null,
                null
        ).visitEnd()

        original.methods.forEach {
            when (it.name) {
                "<init>" -> {
                    val (createHyperiumCapeHandler) = assembleBlock {
                        aload_0
                        new(HyperiumCapeHandler::class)
                        dup
                        aload_2
                        invokespecial(HyperiumCapeHandler::class, "<init>", void, GameProfile::class)
                        putfield(AbstractClientPlayer::class, "hook", HyperiumCapeHandler::class)
                    }

                    it.instructions.insertBefore(it.instructions.first, createHyperiumCapeHandler)
                }

                "getPlayerInfo" -> it.access = Opcodes.ACC_PUBLIC

                "getLocationSkin" -> {
                    if (it.desc == "()Lnet/minecraft/util/ResourceLocation;") {
                        val (createNickHider) = assembleBlock {
                            getstatic(NickHider::class, "instance", NickHider::class)
                            astore_1
                            aload_1
                            ifnull(L["2"])
                            aload_1
                            invokevirtual(NickHider::class, "getNickHiderConfig", NickHiderConfig::class)
                            invokevirtual(NickHiderConfig::class, "isHideSkins", boolean)
                            ifeq(L["2"])
                            aload_1
                            invokevirtual(NickHider::class, "getNickHiderConfig", NickHiderConfig::class)
                            invokevirtual(NickHiderConfig::class, "isMasterEnabled", boolean)
                            ifeq(L["2"])
                            aload_1
                            invokevirtual(NickHider::class, "getNickHiderConfig", NickHiderConfig::class)
                            astore_2
                            aload_0
                            invokevirtual(AbstractClientPlayer::class, "getUniqueID", UUID::class)
                            invokestatic(Minecraft::class, "getMinecraft", Minecraft::class)
                            getfield(Minecraft::class, "thePlayer", EntityPlayerSP::class)
                            invokevirtual(EntityPlayerSP::class, "getUniqueID", UUID::class)
                            invokevirtual(UUID::class, "equals", boolean, Object::class)
                            ifeq(L["5"])
                            aload_2
                            invokevirtual(NickHiderConfig::class, "isUseRealSkinForSelf", boolean)
                            ifeq(L["7"])
                            aload_1
                            invokevirtual(NickHider::class, "getPlayerSkin", ResourceLocation::class)
                            ifnull(L["7"])
                            aload_1
                            invokevirtual(NickHider::class, "getPlayerSkin", ResourceLocation::class)
                            goto(L["8"])
                            +L["7"]
                            aload_0
                            invokevirtual(AbstractClientPlayer::class, "getUniqueID", UUID::class)
                            invokestatic(DefaultPlayerSkin::class, "getDefaultSkin", ResourceLocation::class, UUID::class)
                            +L["8"]
                            areturn
                            +L["5"]
                            aload_2
                            invokevirtual(NickHiderConfig::class, "isHideOtherSkins", boolean)
                            ifeq(L["2"])
                            aload_2
                            invokevirtual(NickHiderConfig::class, "isUsePlayerSkinForAll", boolean)
                            ifeq(L["10"])
                            aload_1
                            invokevirtual(NickHider::class, "getPlayerSkin", ResourceLocation::class)
                            ifnull(L["10"])
                            aload_1
                            invokevirtual(NickHider::class, "getPlayerSkin", ResourceLocation::class)
                            goto(L["11"])
                            +L["10"]
                            aload_0
                            invokevirtual(AbstractClientPlayer::class, "getUniqueID", UUID::class)
                            invokestatic(DefaultPlayerSkin::class, "getDefaultSkin", ResourceLocation::class, UUID::class)
                            +L["11"]
                            areturn
                            +L["2"]
                        }

                        it.instructions.insertBefore(it.instructions.first, createNickHider)
                    }
                }

                "getLocationCape" -> {
                    val (createCapeAndNickHider) = assembleBlock {
                        aload_0
                        getfield(AbstractClientPlayer::class, "hook", HyperiumCapeHandler::class)
                        invokevirtual(HyperiumCapeHandler::class, "getLocationCape", ResourceLocation::class)
                        ifnull(L["1"])
                        aload_0
                        getfield(AbstractClientPlayer::class, "hook", HyperiumCapeHandler::class)
                        invokevirtual(HyperiumCapeHandler::class, "getLocationCape", ResourceLocation::class)
                        areturn
                        +L["1"]
                        getstatic(NickHider::class, "instance", NickHider::class)
                        astore_1
                        aload_1
                        ifnull(L["4"])
                        aload_1
                        invokevirtual(NickHider::class, "getNickHiderConfig", NickHiderConfig::class)
                        invokevirtual(NickHiderConfig::class, "isHideSkins", boolean)
                        ifeq(L["4"])
                        aload_1
                        invokevirtual(NickHider::class, "getNickHiderConfig", NickHiderConfig::class)
                        invokevirtual(NickHiderConfig::class, "isMasterEnabled", boolean)
                        ifeq(L["4"])
                        aload_1
                        invokevirtual(NickHider::class, "gettNickHiderConfig", NickHiderConfig::class)
                        astore_2
                        aload_0
                        invokevirtual(AbstractClientPlayer::class, "getUniqueID", UUID::class)
                        invokestatic(Minecraft::class, "getMinecraft", Minecraft::class)
                        getfield(Minecraft::class, "thePlayer", EntityPlayerSP::class)
                        invokevirtual(EntityPlayerSP::class, "getUniqueID", UUID::class)
                        invokevirtual(UUID::class, "equals", boolean, Object::class)
                        ifeq(L["4"])
                        aload_2
                        invokevirtual(NickHiderConfig::class, "isUseRealSkinForSelf", boolean)
                        ifeq(L["4"])
                        aload_1
                        invokevirtual(NickHider::class, "getPlayerCape", ResourceLocation::class)
                        areturn
                        +L["4"]
                    }

                    it.instructions.insertBefore(it.instructions.first, createCapeAndNickHider)
                }

                "getSkinType" -> {
                    val (createNickHider) = assembleBlock {
                        getstatic(NickHider::class, "instance", NickHider::class)
                        astore_1
                        aload_1
                        ifnull(L["2"])
                        aload_1
                        invokevirtual(NickHider::class, "getNickHiderConfig", NickHiderConfig::class)
                        invokevirtual(NickHiderConfig::class, "isHideSkins", boolean)
                        ifeq(L["2"])
                        aload_1
                        invokevirtual(NickHider::class, "getNickHiderConfig", NickHiderConfig::class)
                        invokevirtual(NickHiderConfig::class, "isMasterEnabled", boolean)
                        ifeq(L["2"])
                        aload_1
                        invokevirtual(NickHider::class, "getNickHiderConfig", NickHiderConfig::class)
                        astore_2
                        aload_0
                        invokevirtual(AbstractClientPlayer::class, "getUniqueID", UUID::class)
                        invokestatic(Minecraft::class, "getMinecraft", Minecraft::class)
                        getfield(Minecraft::class, "thePlayer", EntityPlayerSP::class)
                        invokevirtual(EntityPlayerSP::class, "getUniqueID", UUID::class)
                        invokevirtual(UUID::class, "equals", boolean, Object::class)
                        ifeq(L["5"])
                        aload_2
                        invokevirtual(NickHiderConfig::class, "isUseRealSkinForSelf", boolean)
                        ifeq(L["2"])
                        aload_1
                        invokevirtual(NickHider::class, "getPlayerSkin", ResourceLocation::class)
                        ifnull(L["2"])
                        aload_1
                        invokevirtual(NickHider::class, "getPlayerRealSkinType", String::class)
                        areturn
                        +L["5"]
                        aload_2
                        invokevirtual(NickHiderConfig::class, "isHideOtherSkins", boolean)
                        ifeq(L["2"])
                        aload_2
                        invokevirtual(NickHiderConfig::class, "isUsePlayerSkinForAll", boolean)
                        ifeq(L["2"])
                        aload_1
                        invokevirtual(NickHider::class, "getPlayerSkin", ResourceLocation::class)
                        ifnull(L["2"])
                        aload_1
                        invokevirtual(NickHider::class, "getPlayerRealSkinType", String::class)
                        areturn
                        +L["2"]
                    }

                    it.instructions.insertBefore(it.instructions.first, createNickHider)
                }

                "getFovModifier" -> {
                    val (setNewReturn) = assembleBlock {
                        new(FovUpdateEvent::class)
                        dup
                        aload_0
                        fload_1
                        invokespecial(FovUpdateEvent::class, "<init>", void, EntityPlayer::class, float)
                        astore_3
                        getstatic(EventBus::class, "INSTANCE", EventBus::class)
                        aload_3
                        invokevirtual(EventBus::class, "post", void, Event::class)
                        aload_3
                        invokevirtual(FovUpdateEvent::class, "getNewFov", float)
                        freturn
                    }

                    for (insn in it.instructions.iterator()) {
                        if (insn.opcode == Opcodes.FRETURN) {
                            it.instructions.insertBefore(insn, setNewReturn)
                        }
                    }
                }
            }
        }

        return original
    }
}
