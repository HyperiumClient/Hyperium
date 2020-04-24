package cc.hyperium.launch.patching.conflicts

import cc.hyperium.handlers.handlers.cape.HyperiumCapeHandler
import cc.hyperium.hooks.AbstractClientPlayerHook
import cc.hyperium.mods.nickhider.NickHider
import cc.hyperium.mods.nickhider.config.NickHiderConfig
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import codes.som.anthony.koffee.koffee
import com.mojang.authlib.GameProfile
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.AbstractClientPlayer
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.resources.DefaultPlayerSkin
import net.minecraft.util.ResourceLocation
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import java.util.*

class AbstractClientPlayerTransformer : ConflictTransformer {
    override fun getClassName() = "bet"

    override fun transform(original: ClassNode): ClassNode {
        original.visitField(
            Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL,
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

                    it.instructions.insertBefore(it.instructions.last.previous, createHyperiumCapeHandler)
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
                            invokestatic(
                                DefaultPlayerSkin::class,
                                "getDefaultSkin",
                                ResourceLocation::class,
                                UUID::class
                            )
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
                            invokestatic(
                                DefaultPlayerSkin::class,
                                "getDefaultSkin",
                                ResourceLocation::class,
                                UUID::class
                            )
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
                    it.instructions.clear()
                    it.localVariables.clear()
                    it.koffee {
                        aload_0
                        invokestatic(AbstractClientPlayerHook::class, "getFovModifierHook", float, AbstractClientPlayer::class)
                        freturn
                    }
                }
            }
        }

        return original
    }
}
