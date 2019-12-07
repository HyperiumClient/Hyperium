package cc.hyperium.launch.patching.conflicts

import cc.hyperium.Hyperium
import cc.hyperium.handlers.HyperiumHandlers
import cc.hyperium.integrations.perspective.PerspectiveModifierHandler
import cc.hyperium.utils.renderer.shader.ShaderHelper
import codes.som.anthony.koffee.assembleBlock
import codes.som.anthony.koffee.insns.jvm.*
import net.minecraft.block.Block
import net.minecraft.block.BlockBed
import net.minecraft.block.properties.IProperty
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.client.renderer.EntityRenderer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.settings.GameSettings
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
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

                method.instructions.insert(method.instructions.last, createShaderHelper)
            }

            if (method.name == "loadShader") {
                method.access = Opcodes.ACC_PUBLIC
            }

            if (method.name == "updateRenderer") {
                val setPositionEyes = assembleBlock {
                    fconst_1
                    invokevirtual(Entity::class, "getPositionEyes", Vec3::class, float)
                }.first

                for (insn in method.instructions.iterator()) {
                    if (insn.opcode == Opcodes.INVOKESPECIAL && insn is MethodInsnNode && insn.owner == "net/minecraft/util/BlockPos"
                        && insn.name == "<init>" && insn.desc == "(Lnet/minecraft/util/Vec3;)V"
                    ) {
                        method.instructions.insertBefore(insn, setPositionEyes)
                    }
                }
            }

            if (method.name == "orientCamera") {
                method.instructions = assembleBlock {
                    getstatic(Hyperium::class, "INSTANCE", Hyperium::class)
                    invokevirtual(Hyperium::class, "getHandlers", HyperiumHandlers::class)
                    invokevirtual(HyperiumHandlers::class, "getPerspectiveHandler", PerspectiveModifierHandler::class)
                    astore_2

                    aload_0
                    getfield(EntityRenderer::class, "mc", Minecraft::class)
                    invokevirtual(Minecraft::class, "getRenderViewEntity", Entity::class)
                    astore_3

                    aload_3
                    invokevirtual(Entity::class, "getEyeHeight", float)
                    fstore(4)

                    aload_3
                    getfield(Entity::class, "prevPosX", double)
                    aload_3
                    getfield(Entity::class, "posX", double)
                    aload_3
                    getfield(Entity::class, "prevPosX", double)
                    dsub
                    fload_1
                    f2d
                    dmul
                    dadd
                    dstore(5)

                    aload_3
                    getfield(Entity::class, "prevPosY", double)
                    aload_3
                    getfield(Entity::class, "posY", double)
                    aload_3
                    getfield(Entity::class, "prevPosY", double)
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
                    getfield(Entity::class, "prevPosZ", double)
                    aload_3
                    getfield(Entity::class, "posZ", double)
                    aload_3
                    getfield(Entity::class, "prevPosZ", double)
                    dsub
                    fload_1
                    f2d
                    dmul
                    dadd
                    dstore(9)

                    aload_3
                    instanceof(EntityLivingBase::class)
                    ifeq(L["7"])
                    aload_3
                    checkcast(EntityLivingBase::class)
                    invokevirtual(EntityLivingBase::class, "isPlayerSleeping", boolean)
                    ifeq(L["7"])

                    fload(4)
                    fconst_1
                    fadd
                    fstore(4)

                    fconst_0
                    ldc(0.3)
                    fconst_0
                    invokestatic(GlStateManager::class, "translate", void, float, float, float)

                    aload_0
                    getfield(EntityRenderer::class, "mc", Minecraft::class)
                    getfield(Minecraft::class, "gameSettings", GameSettings::class)
                    getfield(GameSettings::class, "debugCamEnable", boolean)
                    ifne(L["11"])

                    new(BlockPos::class)
                    dup
                    aload_3
                    invokespecial(BlockPos::class, "<init>", void, Entity::class)
                    astore(11)

                    aload_0
                    getfield(EntityRenderer::class, "mc", Minecraft::class)
                    getfield(Minecraft::class, "theWorld", WorldClient::class)
                    aload(11)
                    invokevirtual(WorldClient::class, "getBlockState", IBlockState::class, BlockPos::class)
                    astore(12)

                    aload(12)
                    invokeinterface(IBlockState::class, "getBlock", Block::class)
                    astore(13)

                    aload(13)
                    getstatic(Blocks::class, "bed", Block::class)
                    if_acmpne(L["16"])

                    aload(12)
                    getstatic(BlockBed::class, "FACING", PropertyDirection::class)
                    invokeinterface(IBlockState::class, "getValue", java.lang.Comparable::class, IProperty::class)
                    checkcast(EnumFacing::class)
                    invokevirtual(EnumFacing::class, "getHorizontalIndex", int)
                    istore(14)

                    iload(14)
                    bipush(90)
                    imul
                    i2f
                    fconst_0
                    fconst_1
                    fconst_0
                    invokestatic(GlStateManager::class, "rotate", void, float, float, float, float)

                    +L["16"]

                    aload_3
                    getfield(Entity::class, "prevRotationYaw", float)
                    aload_3
                    getfield(Entity::class, "rotationYaw", float)
                    aload_3
                    getfield(Entity::class, "prevRotationYaw", float)
                    fsub
                    fload_1
                    fmul
                    fadd
                    ldc(180.0)
                    fadd
                    fconst_0
                    ldc(-1.0)
                    fconst_0
                    invokestatic(GlStateManager::class, "rotate", void, float, float, float, float)

                    aload_3
                    getfield(Entity::class, "prevRotationPitch", float)
                    aload_3
                    getfield(Entity::class, "rotationPitch", float)
                    aload_3
                    getfield(Entity::class, "prevRotationPitch", float)
                    fsub
                    fload_1
                    fmul
                    fadd
                    ldc(-1.0)
                    fconst_0
                    fconst_0
                    invokestatic(GlStateManager::class, "rotate", void, float, float, float, float)

                    goto(L["11"])

                    +L["7"]

                    aload_0
                    getfield(EntityRenderer::class, "mc", Minecraft::class)
                    getfield(Minecraft::class, "gameSettings", GameSettings::class)
                    getfield(GameSettings::class, "thirdPersonView", int)
                    ifne(L["21"])

                    aload_0
                    getfield(EntityRenderer::class, "thirdPersonDistanceTemp", float)
                    aload_0
                    getfield(EntityRenderer::class, "thirdPersonDistance", float)
                    aload_0
                    getfield(EntityRenderer::class, "thirdPersonDistanceTemp", float)
                    fsub
                    fload_1
                    fmul
                    fadd
                    f2d
                    dstore(11)

                    aload_0
                    getfield(EntityRenderer::class, "mc", Minecraft::class)
                    getfield(Minecraft::class, "gameSettings", GameSettings::class)
                    getfield(GameSettings::class, "debugCamEnable", boolean)
                    ifeq(L["24"])

                    fconst_0
                    fconst_0
                    dload(11)
                    dneg
                    d2f
                    invokestatic(GlStateManager::class, "translate", void, float, float, float)
                    goto(L["26"])

                    +L["24"]

                    aload_3
                    getfield(Entity::class, "rotationYaw", float)
                    fstore(13)

                    aload_3
                    getfield(Entity::class, "rotationPitch", float)
                    fstore(14)

                    aload_2
                    getfield(PerspectiveModifierHandler::class, "enabled", boolean)
                    ifeq(L["29"])

                    aload_2
                    getfield(PerspectiveModifierHandler::class, "modifiedYaw", float)
                    fstore(13)

                    aload_2
                    getfield(PerspectiveModifierHandler::class, "modifiedPitch", float)
                    fstore(14)

                    +L["29"]

                    aload_0
                    getfield(EntityRenderer::class, "mc", Minecraft::class)
                    getfield(Minecraft::class, "gameSettings", GameSettings::class)
                    getfield(GameSettings::class, "thirdPersonView", int)
                    iconst_2
                    if_icmpne(L["32"])

                    fload(14)
                    ldc(180.0)
                    fadd
                    fstore(14)

                    +L["32"]

                    fload(13)
                    ldc(180.0)
                    fdiv
                    ldc(3.1415927)
                    fmul
                    invokestatic(MathHelper::class, "sin", float, float)
                    fneg
                    fload(14)
                    ldc(180.0)
                    fdiv
                    ldc(3.1415927)
                    fmul
                    invokestatic(MathHelper::class, "cos", float, float)
                    fmul
                    f2d
                    dload(11)
                    dmul
                    dstore(15)

                    fload(13)
                    ldc(180.0)
                    fdiv
                    ldc(3.1415927)
                    fmul
                    invokestatic(MathHelper::class, "cos", float, float)
                    fload(14)
                    ldc(180.0)
                    fdiv
                    ldc(3.1415927)
                    fmul
                    invokestatic(MathHelper::class, "cos", float, float)
                    fmul
                    f2d
                    dload(11)
                    dmul
                    dstore(17)

                    fload(14)
                    ldc(180.0)
                    fdiv
                    ldc(3.1415927)
                    fmul
                    invokestatic(MathHelper::class, "sin", float, float)
                    fneg
                    f2d
                    dload(11)
                    dmul
                    dstore(19)

                    iconst_0
                    istore(21)

                    iload(21)
                    bipush(8)
                    if_icmpge(L["38"])

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
                    ldc(0.1)
                    fmul
                    fstore(22)

                    fload(23)
                    ldc(0.1)
                    fmul
                    fstore(23)

                    fload(24)
                    ldc(0.1)
                    fmul
                    fstore(24)

                    aload_0
                    getfield(EntityRenderer::class, "mc", Minecraft::class)
                    getfield(Minecraft::class, "theWorld", WorldClient::class)
                    new(Vec3::class)
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
                    invokespecial(Vec3::class, "<init>", void, double, double, double)
                    new(Vec3::class)
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
                    invokespecial(Vec3::class, "<init>", void, double, double, double)
                    invokevirtual(WorldClient::class, "rayTraceBlocks", MovingObjectPosition::class, Vec3::class, Vec3::class)
                    astore(25)

                    aload(25)
                    ifnull(L["47"])

                    aload(25)
                    getfield(MovingObjectPosition::class, "hitVec", Vec3::class)
                    new(Vec3::class)
                    dup
                    dload(5)
                    dload(7)
                    dload(9)
                    invokespecial(Vec3::class, "<init>", void, double, double, double)
                    invokevirtual(Vec3::class, "distanceTo", double, Vec3::class)
                    dstore(26)

                    dload(26)
                    dload(11)
                    dcmpg
                    ifge(L["47"])

                    dload(26)
                    dstore(11)

                    iinc(21, 1)
                    goto(L["37"])

                    aload_0
                    getfield(EntityRenderer::class, "mc", Minecraft::class)
                    getfield(Minecraft::class, "gameSettings", GameSettings::class)
                    getfield(GameSettings::class, "thirdPersonView", int)
                    iconst_2
                    if_icmpne(L["51"])

                    ldc(180.0)
                    fconst_0
                    fconst_1
                    fconst_0
                    invokestatic(GlStateManager::class, "rotate", void, float, float, float, float)

                    +L["51"]

                    aload_2
                    getfield(PerspectiveModifierHandler::class, "enabled", boolean)
                    ifeq(L["53"])

                    aload_2
                    getfield(PerspectiveModifierHandler::class, "modifiedPitch", float)
                    fload(14)
                    fsub
                    fconst_1
                    fconst_0
                    fconst_0
                    invokestatic(GlStateManager::class, "rotate", void, float, float, float, float)

                    aload_2
                    getfield(PerspectiveModifierHandler::class, "modifiedYaw", float)
                    fload(13)
                    fsub
                    fconst_0
                    fconst_1
                    fconst_0
                    invokestatic(GlStateManager::class, "rotate", void, float, float, float, float)

                    fconst_0
                    fconst_0
                    dload(11)
                    dneg
                    d2f
                    invokestatic(GlStateManager::class, "translate", void, float, float, float)

                    fload(13)
                    aload_2
                    getfield(PerspectiveModifierHandler::class, "modifiedYaw", float)
                    fsub
                    fconst_0
                    fconst_1
                    fconst_0
                    invokestatic(GlStateManager::class, "rotate", void, float, float, float, float)

                    fload(14)
                    aload_2
                    getfield(PerspectiveModifierHandler::class, "modifiedPitch", float)
                    fsub
                    fconst_1
                    fconst_0
                    fconst_0
                    invokestatic(GlStateManager::class, "rotate", void, float, float, float, float)
                    goto(L["26"])
                }.first
            }
        }

        return original
    }

    override fun getClassName() = "bfk"
}
