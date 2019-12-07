package cc.hyperium.launch.patching.conflicts

import cc.hyperium.Hyperium
import cc.hyperium.handlers.HyperiumHandlers
import cc.hyperium.integrations.perspective.PerspectiveModifierHandler
import cc.hyperium.utils.renderer.shader.ShaderHelper
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import codes.som.anthony.koffee.koffee
import net.minecraft.block.Block
import net.minecraft.block.BlockBed
import net.minecraft.block.properties.IProperty
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.client.renderer.EntityRenderer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderGlobal
import net.minecraft.client.settings.GameSettings
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.init.Blocks
import net.minecraft.util.*
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode

class EntityRendererTransformer : ConflictTransformer {
    override fun transform(original: ClassNode): ClassNode {
        for (method in original.methods) {
            if (method.name == "<init>") {
                val createShaderHelper = assembleBlock {
                    new(ShaderHelper::class)
                    dup
                    aload_0
                    invokespecial(ShaderHelper::class, "<init>", void, EntityRenderer::class)
                    pop
                }.first

                method.instructions.iterator().forEach {
                    if (it.opcode == Opcodes.RETURN) {
                        method.instructions.insertBefore(it, createShaderHelper)
                    }
                }
            }

            if (method.name == "loadShader") {
                method.access = Opcodes.ACC_PUBLIC
            }

            if (method.name == "updateRenderer") {
                val setPositionEyes = assembleBlock {
                    aload_0
                    getfield(EntityRenderer::class, "mc", Minecraft::class)
                    invokevirtual(Minecraft::class, "getRenderViewEntity", Entity::class)
                    fconst_1
                    invokevirtual(Entity::class, "getPositionEyes", Vec3::class, float)
                    invokespecial(BlockPos::class, "<init>", void, Vec3::class)
                }.first

                for (insn in method.instructions.iterator()) {
                    if (insn.opcode == Opcodes.INVOKESPECIAL && insn is MethodInsnNode && insn.owner == "net/minecraft/util/BlockPos"
                        && insn.name == "<init>" && insn.desc == "(DDD)V"
                    ) {
                        method.instructions.insertBefore(insn.previous.previous.previous, setPositionEyes)
                        method.instructions.remove(insn.previous.previous.previous)
                        method.instructions.remove(insn.previous.previous)
                        method.instructions.remove(insn.previous)
                        method.instructions.remove(insn)
                        break
                    }
                }
            }

            if (method.name == "orientCamera") {
                method.instructions = assembleBlock {
                    getstatic("cc/hyperium/Hyperium", "INSTANCE", "cc/hyperium/Hyperium")
                    invokevirtual("cc/hyperium/Hyperium", "getHandlers", "cc/hyperium/handlers/HyperiumHandlers")
                    invokevirtual("cc/hyperium/handlers/HyperiumHandlers", "getPerspectiveHandler", "cc/hyperium/integrations/perspective/PerspectiveModifierHandler")
                    astore_2
                    aload_0
                    getfield("net/minecraft/client/renderer/EntityRenderer", "mc", "net/minecraft/client/Minecraft")
                    invokevirtual("net/minecraft/client/Minecraft", "getRenderViewEntity", "net/minecraft/entity/Entity")
                    astore_3
                    aload_3
                    invokevirtual("net/minecraft/entity/Entity", "getEyeHeight", float)
                    fstore(4)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "prevPosX", double)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "posX", double)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "prevPosX", double)
                    dsub
                    fload_1
                    f2d
                    dmul
                    dadd
                    dstore(5)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "prevPosY", double)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "posY", double)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "prevPosY", double)
                    dsub
                    fload_1
                    f2d
                    dmul
                    dadd
                    fload(4)
                    f2d
                    dadd
                    dstore(7)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "prevPosZ", double)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "posZ", double)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "prevPosZ", double)
                    dsub
                    fload_1
                    f2d
                    dmul
                    dadd
                    dstore(9)
                    aload_3
                    instanceof("net/minecraft/entity/EntityLivingBase")
                    ifeq(L[2])
                    aload_3
                    checkcast("net/minecraft/entity/EntityLivingBase")
                    invokevirtual("net/minecraft/entity/EntityLivingBase", "isPlayerSleeping", boolean)
                    ifeq(L[2])
                    fload(4)
                    fconst_1
                    fadd
                    fstore(4)
                    fconst_0
                    ldc(0.3F)
                    fconst_0
                    invokestatic("net/minecraft/client/renderer/GlStateManager", "translate", void, float, float, float)
                    aload_0
                    getfield("net/minecraft/client/renderer/EntityRenderer", "mc", "net/minecraft/client/Minecraft")
                    getfield("net/minecraft/client/Minecraft", "gameSettings", "net/minecraft/client/settings/GameSettings")
                    getfield("net/minecraft/client/settings/GameSettings", "debugCamEnable", boolean)
                    ifne(L[13])
                    new("net/minecraft/util/BlockPos")
                    dup
                    aload_3
                    invokespecial("net/minecraft/util/BlockPos", "<init>", void, "net/minecraft/entity/Entity")
                    astore(11)
                    aload_0
                    getfield("net/minecraft/client/renderer/EntityRenderer", "mc", "net/minecraft/client/Minecraft")
                    getfield("net/minecraft/client/Minecraft", "theWorld", "net/minecraft/client/multiplayer/WorldClient")
                    aload(11)
                    invokevirtual("net/minecraft/client/multiplayer/WorldClient", "getBlockState", "net/minecraft/block/state/IBlockState", "net/minecraft/util/BlockPos")
                    astore(12)
                    aload(12)
                    invokeinterface("net/minecraft/block/state/IBlockState", "getBlock", "net/minecraft/block/Block")
                    astore(13)
                    aload(13)
                    getstatic("net/minecraft/init/Blocks", "bed", "net/minecraft/block/Block")
                    if_acmpne(L[1])
                    aload(12)
                    getstatic("net/minecraft/block/BlockBed", "FACING", "net/minecraft/block/properties/PropertyDirection")
                    invokeinterface("net/minecraft/block/state/IBlockState", "getValue", "java/lang/Comparable", "net/minecraft/block/properties/IProperty")
                    checkcast("net/minecraft/util/EnumFacing")
                    invokevirtual("net/minecraft/util/EnumFacing", "getHorizontalIndex", int)
                    istore(14)
                    iload(14)
                    bipush(90)
                    imul
                    i2f
                    fconst_0
                    fconst_1
                    fconst_0
                    invokestatic("net/minecraft/client/renderer/GlStateManager", "rotate", void, float, float, float, float)

                    +L[1]
                    aload_3
                    getfield("net/minecraft/entity/Entity", "prevRotationYaw", float)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "rotationYaw", float)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "prevRotationYaw", float)
                    fsub
                    fload_1
                    fmul
                    fadd
                    ldc(180.0F)
                    fadd
                    fconst_0
                    ldc(-1.0F)
                    fconst_0
                    invokestatic("net/minecraft/client/renderer/GlStateManager", "rotate", void, float, float, float, float)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "prevRotationPitch", float)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "rotationPitch", float)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "prevRotationPitch", float)
                    fsub
                    fload_1
                    fmul
                    fadd
                    ldc(-1.0F)
                    fconst_0
                    fconst_0
                    invokestatic("net/minecraft/client/renderer/GlStateManager", "rotate", void, float, float, float, float)
                    goto(L[13])

                    +L[2]
                    aload_0
                    getfield("net/minecraft/client/renderer/EntityRenderer", "mc", "net/minecraft/client/Minecraft")
                    getfield("net/minecraft/client/Minecraft", "gameSettings", "net/minecraft/client/settings/GameSettings")
                    getfield("net/minecraft/client/settings/GameSettings", "thirdPersonView", int)
                    ifle(L[12])
                    aload_0
                    getfield("net/minecraft/client/renderer/EntityRenderer", "thirdPersonDistanceTemp", float)
                    aload_0
                    getfield("net/minecraft/client/renderer/EntityRenderer", "thirdPersonDistance", float)
                    aload_0
                    getfield("net/minecraft/client/renderer/EntityRenderer", "thirdPersonDistanceTemp", float)
                    fsub
                    fload_1
                    fmul
                    fadd
                    f2d
                    dstore(11)
                    aload_0
                    getfield("net/minecraft/client/renderer/EntityRenderer", "mc", "net/minecraft/client/Minecraft")
                    getfield("net/minecraft/client/Minecraft", "gameSettings", "net/minecraft/client/settings/GameSettings")
                    getfield("net/minecraft/client/settings/GameSettings", "debugCamEnable", boolean)
                    ifeq(L[3])
                    fconst_0
                    fconst_0
                    dload(11)
                    dneg
                    d2f
                    invokestatic("net/minecraft/client/renderer/GlStateManager", "translate", void, float, float, float)
                    goto(L[11])

                    +L[3]
                    aload_3
                    getfield("net/minecraft/entity/Entity", "rotationYaw", float)
                    fstore(13)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "rotationPitch", float)
                    fstore(14)
                    aload_2
                    getfield("cc/hyperium/integrations/perspective/PerspectiveModifierHandler", "enabled", boolean)
                    ifeq(L[4])
                    aload_2
                    getfield("cc/hyperium/integrations/perspective/PerspectiveModifierHandler", "modifiedYaw", float)
                    fstore(13)
                    aload_2
                    getfield("cc/hyperium/integrations/perspective/PerspectiveModifierHandler", "modifiedPitch", float)
                    fstore(14)

                    +L[4]
                    aload_0
                    getfield("net/minecraft/client/renderer/EntityRenderer", "mc", "net/minecraft/client/Minecraft")
                    getfield("net/minecraft/client/Minecraft", "gameSettings", "net/minecraft/client/settings/GameSettings")
                    getfield("net/minecraft/client/settings/GameSettings", "thirdPersonView", int)
                    iconst_2
                    if_icmpne(L[5])
                    fload(14)
                    ldc(180.0F)
                    fadd
                    fstore(14)

                    +L[5]
                    fload(13)
                    ldc(180.0F)
                    fdiv
                    ldc(3.1415927F)
                    fmul
                    invokestatic("net/minecraft/util/MathHelper", "sin", float, float)
                    fneg
                    fload(14)
                    ldc(180.0F)
                    fdiv
                    ldc(3.1415927F)
                    fmul
                    invokestatic("net/minecraft/util/MathHelper", "cos", float, float)
                    fmul
                    f2d
                    dload(11)
                    dmul
                    dstore(15)
                    fload(13)
                    ldc(180.0F)
                    fdiv
                    ldc(3.1415927F)
                    fmul
                    invokestatic("net/minecraft/util/MathHelper", "cos", float, float)
                    fload(14)
                    ldc(180.0F)
                    fdiv
                    ldc(3.1415927F)
                    fmul
                    invokestatic("net/minecraft/util/MathHelper", "cos", float, float)
                    fmul
                    f2d
                    dload(11)
                    dmul
                    dstore(17)
                    fload(14)
                    ldc(180.0F)
                    fdiv
                    ldc(3.1415927F)
                    fmul
                    invokestatic("net/minecraft/util/MathHelper", "sin", float, float)
                    fneg
                    f2d
                    dload(11)
                    dmul
                    dstore(19)
                    iconst_0
                    istore(21)

                    +L[6]
                    iload(21)
                    bipush(8)
                    if_icmpge(L[8])
                    iload(21)
                    iconst_1
                    iand
                    iconst_2
                    imul
                    iconst_1
                    isub
                    i2f
                    fstore(22)
                    iload(21)
                    iconst_1
                    ishr
                    iconst_1
                    iand
                    iconst_2
                    imul
                    iconst_1
                    isub
                    i2f
                    fstore(23)
                    iload(21)
                    iconst_2
                    ishr
                    iconst_1
                    iand
                    iconst_2
                    imul
                    iconst_1
                    isub
                    i2f
                    fstore(24)
                    fload(22)
                    ldc(0.1F)
                    fmul
                    fstore(22)
                    fload(23)
                    ldc(0.1F)
                    fmul
                    fstore(23)
                    fload(24)
                    ldc(0.1F)
                    fmul
                    fstore(24)
                    aload_0
                    getfield("net/minecraft/client/renderer/EntityRenderer", "mc", "net/minecraft/client/Minecraft")
                    getfield("net/minecraft/client/Minecraft", "theWorld", "net/minecraft/client/multiplayer/WorldClient")
                    new("net/minecraft/util/Vec3")
                    dup
                    dload(5)
                    fload(22)
                    f2d
                    dadd
                    dload(7)
                    fload(23)
                    f2d
                    dadd
                    dload(9)
                    fload(24)
                    f2d
                    dadd
                    invokespecial("net/minecraft/util/Vec3", "<init>", void, double, double, double)
                    new("net/minecraft/util/Vec3")
                    dup
                    dload(5)
                    dload(15)
                    dsub
                    fload(22)
                    f2d
                    dadd
                    fload(24)
                    f2d
                    dadd
                    dload(7)
                    dload(19)
                    dsub
                    fload(23)
                    f2d
                    dadd
                    dload(9)
                    dload(17)
                    dsub
                    fload(24)
                    f2d
                    dadd
                    invokespecial("net/minecraft/util/Vec3", "<init>", void, double, double, double)
                    invokevirtual("net/minecraft/client/multiplayer/WorldClient", "rayTraceBlocks", "net/minecraft/util/MovingObjectPosition", "net/minecraft/util/Vec3", "net/minecraft/util/Vec3")
                    astore(25)
                    aload(25)
                    ifnull(L[7])
                    aload(25)
                    getfield("net/minecraft/util/MovingObjectPosition", "hitVec", "net/minecraft/util/Vec3")
                    new("net/minecraft/util/Vec3")
                    dup
                    dload(5)
                    dload(7)
                    dload(9)
                    invokespecial("net/minecraft/util/Vec3", "<init>", void, double, double, double)
                    invokevirtual("net/minecraft/util/Vec3", "distanceTo", double, "net/minecraft/util/Vec3")
                    dstore(26)
                    dload(26)
                    dload(11)
                    dcmpg
                    ifge(L[7])
                    dload(26)
                    dstore(11)

                    +L[7]
                    iinc(21, 1)
                    goto(L[6])

                    +L[8]
                    aload_0
                    getfield("net/minecraft/client/renderer/EntityRenderer", "mc", "net/minecraft/client/Minecraft")
                    getfield("net/minecraft/client/Minecraft", "gameSettings", "net/minecraft/client/settings/GameSettings")
                    getfield("net/minecraft/client/settings/GameSettings", "thirdPersonView", int)
                    iconst_2
                    if_icmpne(L[9])
                    ldc(180.0F)
                    fconst_0
                    fconst_1
                    fconst_0
                    invokestatic("net/minecraft/client/renderer/GlStateManager", "rotate", void, float, float, float, float)

                    +L[9]
                    aload_2
                    getfield("cc/hyperium/integrations/perspective/PerspectiveModifierHandler", "enabled", boolean)
                    ifeq(L[10])
                    aload_2
                    getfield("cc/hyperium/integrations/perspective/PerspectiveModifierHandler", "modifiedPitch", float)
                    fload(14)
                    fsub
                    fconst_1
                    fconst_0
                    fconst_0
                    invokestatic("net/minecraft/client/renderer/GlStateManager", "rotate", void, float, float, float, float)
                    aload_2
                    getfield("cc/hyperium/integrations/perspective/PerspectiveModifierHandler", "modifiedYaw", float)
                    fload(13)
                    fsub
                    fconst_0
                    fconst_1
                    fconst_0
                    invokestatic("net/minecraft/client/renderer/GlStateManager", "rotate", void, float, float, float, float)
                    fconst_0
                    fconst_0
                    dload(11)
                    dneg
                    d2f
                    invokestatic("net/minecraft/client/renderer/GlStateManager", "translate", void, float, float, float)
                    fload(13)
                    aload_2
                    getfield("cc/hyperium/integrations/perspective/PerspectiveModifierHandler", "modifiedYaw", float)
                    fsub
                    fconst_0
                    fconst_1
                    fconst_0
                    invokestatic("net/minecraft/client/renderer/GlStateManager", "rotate", void, float, float, float, float)
                    fload(14)
                    aload_2
                    getfield("cc/hyperium/integrations/perspective/PerspectiveModifierHandler", "modifiedPitch", float)
                    fsub
                    fconst_1
                    fconst_0
                    fconst_0
                    invokestatic("net/minecraft/client/renderer/GlStateManager", "rotate", void, float, float, float, float)
                    goto(L[11])

                    +L[10]
                    aload_3
                    getfield("net/minecraft/entity/Entity", "rotationPitch", float)
                    fload(14)
                    fsub
                    fconst_1
                    fconst_0
                    fconst_0
                    invokestatic("net/minecraft/client/renderer/GlStateManager", "rotate", void, float, float, float, float)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "rotationYaw", float)
                    fload(13)
                    fsub
                    fconst_0
                    fconst_1
                    fconst_0
                    invokestatic("net/minecraft/client/renderer/GlStateManager", "rotate", void, float, float, float, float)
                    fconst_0
                    fconst_0
                    dload(11)
                    dneg
                    d2f
                    invokestatic("net/minecraft/client/renderer/GlStateManager", "translate", void, float, float, float)
                    fload(13)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "rotationYaw", float)
                    fsub
                    fconst_0
                    fconst_1
                    fconst_0
                    invokestatic("net/minecraft/client/renderer/GlStateManager", "rotate", void, float, float, float, float)
                    fload(14)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "rotationPitch", float)
                    fsub
                    fconst_1
                    fconst_0
                    fconst_0
                    invokestatic("net/minecraft/client/renderer/GlStateManager", "rotate", void, float, float, float, float)

                    +L[11]
                    goto(L[13])

                    +L[12]
                    fconst_0
                    fconst_0
                    ldc(-0.1F)
                    invokestatic("net/minecraft/client/renderer/GlStateManager", "translate", void, float, float, float)

                    +L[13]
                    aload_0
                    getfield("net/minecraft/client/renderer/EntityRenderer", "mc", "net/minecraft/client/Minecraft")
                    getfield("net/minecraft/client/Minecraft", "gameSettings", "net/minecraft/client/settings/GameSettings")
                    getfield("net/minecraft/client/settings/GameSettings", "debugCamEnable", boolean)
                    ifne(L[16])
                    aload_3
                    getfield("net/minecraft/entity/Entity", "prevRotationYaw", float)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "rotationYaw", float)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "prevRotationYaw", float)
                    fsub
                    fload_1
                    fmul
                    fadd
                    ldc(180.0F)
                    fadd
                    fstore(11)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "prevRotationPitch", float)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "rotationPitch", float)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "prevRotationPitch", float)
                    fsub
                    fload_1
                    fmul
                    fadd
                    fstore(12)
                    fconst_0
                    fstore(13)
                    aload_3
                    instanceof("net/minecraft/entity/passive/EntityAnimal")
                    ifeq(L[14])
                    aload_3
                    checkcast("net/minecraft/entity/passive/EntityAnimal")
                    astore(14)
                    aload(14)
                    getfield("net/minecraft/entity/passive/EntityAnimal", "prevRotationYawHead", float)
                    aload(14)
                    getfield("net/minecraft/entity/passive/EntityAnimal", "rotationYawHead", float)
                    aload(14)
                    getfield("net/minecraft/entity/passive/EntityAnimal", "prevRotationYawHead", float)
                    fsub
                    fload_1
                    fmul
                    fadd
                    ldc(180.0F)
                    fadd
                    fstore(11)

                    +L[14]
                    aload_2
                    getfield("cc/hyperium/integrations/perspective/PerspectiveModifierHandler", "enabled", boolean)
                    ifeq(L[15])
                    fconst_0
                    fconst_0
                    fconst_0
                    fconst_1
                    invokestatic("net/minecraft/client/renderer/GlStateManager", "rotate", void, float, float, float, float)
                    aload_2
                    getfield("cc/hyperium/integrations/perspective/PerspectiveModifierHandler", "modifiedPitch", float)
                    fconst_1
                    fconst_0
                    fconst_0
                    invokestatic("net/minecraft/client/renderer/GlStateManager", "rotate", void, float, float, float, float)
                    aload_2
                    getfield("cc/hyperium/integrations/perspective/PerspectiveModifierHandler", "modifiedYaw", float)
                    ldc(180.0F)
                    fadd
                    fconst_0
                    fconst_1
                    fconst_0
                    invokestatic("net/minecraft/client/renderer/GlStateManager", "rotate", void, float, float, float, float)
                    goto(L[16])

                    +L[15]
                    fconst_0
                    fconst_0
                    fconst_0
                    fconst_1
                    invokestatic("net/minecraft/client/renderer/GlStateManager", "rotate", void, float, float, float, float)
                    fload(12)
                    fconst_1
                    fconst_0
                    fconst_0
                    invokestatic("net/minecraft/client/renderer/GlStateManager", "rotate", void, float, float, float, float)
                    fload(11)
                    fconst_0
                    fconst_1
                    fconst_0
                    invokestatic("net/minecraft/client/renderer/GlStateManager", "rotate", void, float, float, float, float)

                    +L[16]
                    fconst_0
                    fload(4)
                    fneg
                    fconst_0
                    invokestatic("net/minecraft/client/renderer/GlStateManager", "translate", void, float, float, float)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "prevPosX", double)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "posX", double)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "prevPosX", double)
                    dsub
                    fload_1
                    f2d
                    dmul
                    dadd
                    dstore(5)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "prevPosY", double)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "posY", double)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "prevPosY", double)
                    dsub
                    fload_1
                    f2d
                    dmul
                    dadd
                    fload(4)
                    f2d
                    dadd
                    dstore(7)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "prevPosZ", double)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "posZ", double)
                    aload_3
                    getfield("net/minecraft/entity/Entity", "prevPosZ", double)
                    dsub
                    fload_1
                    f2d
                    dmul
                    dadd
                    dstore(9)
                    aload_0
                    aload_0
                    getfield("net/minecraft/client/renderer/EntityRenderer", "mc", "net/minecraft/client/Minecraft")
                    getfield("net/minecraft/client/Minecraft", "renderGlobal", "net/minecraft/client/renderer/RenderGlobal")
                    dload(5)
                    dload(7)
                    dload(9)
                    fload_1
                    invokevirtual("net/minecraft/client/renderer/RenderGlobal", "hasCloudFog", boolean, double, double, double, float)
                    putfield("net/minecraft/client/renderer/EntityRenderer", "cloudFog", boolean)
                    _return
                }.first
            }
        }

        original.methods.forEach {
            println(it.name)
        }

        return original
    }

    override fun getClassName() = "bfk"
}
