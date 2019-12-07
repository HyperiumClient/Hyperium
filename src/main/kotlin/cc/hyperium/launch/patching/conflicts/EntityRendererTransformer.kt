package cc.hyperium.launch.patching.conflicts

import cc.hyperium.Hyperium
import cc.hyperium.event.EventBus
import cc.hyperium.event.render.DrawBlockHighlightEvent
import cc.hyperium.event.render.RenderEvent
import cc.hyperium.event.render.RenderWorldEvent
import cc.hyperium.handlers.HyperiumHandlers
import cc.hyperium.handlers.handlers.OtherConfigOptions
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
import net.minecraft.client.renderer.RenderGlobal
import net.minecraft.client.settings.GameSettings
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.profiler.Profiler
import net.minecraft.util.*
import org.lwjgl.opengl.Display
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.MethodInsnNode

// absolute pain ahead
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
                    getstatic(Hyperium::class, "INSTANCE", Hyperium::class)
                    invokevirtual(Hyperium::class, "getHandlers", HyperiumHandlers::class)
                    invokevirtual(
                        HyperiumHandlers::class,
                        "getPerspectiveHandler",
                        PerspectiveModifierHandler::class
                    )
                    astore_2
                    aload_0
                    getfield(EntityRenderer::class, "mc", Minecraft::class)
                    invokevirtual(
                        Minecraft::class,
                        "getRenderViewEntity",
                        Entity::class
                    )
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
                    ifeq(L[2])
                    aload_3
                    checkcast(EntityLivingBase::class)
                    invokevirtual(EntityLivingBase::class, "isPlayerSleeping", boolean)
                    ifeq(L[2])
                    fload(4)
                    fconst_1
                    fadd
                    fstore(4)
                    fconst_0
                    ldc(0.3F)
                    fconst_0
                    invokestatic(GlStateManager::class, "translate", void, float, float, float)
                    aload_0
                    getfield(EntityRenderer::class, "mc", Minecraft::class)
                    getfield(
                        Minecraft::class,
                        "gameSettings",
                        GameSettings::class
                    )
                    getfield(GameSettings::class, "debugCamEnable", boolean)
                    ifne(L[13])
                    new(BlockPos::class)
                    dup
                    aload_3
                    invokespecial(BlockPos::class, "<init>", void, Entity::class)
                    astore(11)
                    aload_0
                    getfield(EntityRenderer::class, "mc", Minecraft::class)
                    getfield(
                        Minecraft::class,
                        "theWorld",
                        WorldClient::class
                    )
                    aload(11)
                    invokevirtual(
                        WorldClient::class,
                        "getBlockState",
                        IBlockState::class,
                        BlockPos::class
                    )
                    astore(12)
                    aload(12)
                    invokeinterface(IBlockState::class, "getBlock", Block::class)
                    astore(13)
                    aload(13)
                    getstatic("net/minecraft/init/Blocks", "bed", Block::class)
                    if_acmpne(L[1])
                    aload(12)
                    getstatic(
                        BlockBed::class,
                        "FACING",
                        PropertyDirection::class
                    )
                    invokeinterface(
                        IBlockState::class,
                        "getValue",
                        java.lang.Comparable::class,
                        IProperty::class
                    )
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
                    invokestatic(
                        GlStateManager::class,
                        "rotate",
                        void,
                        float,
                        float,
                        float,
                        float
                    )

                    +L[1]
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
                    ldc(180.0F)
                    fadd
                    fconst_0
                    ldc(-1.0F)
                    fconst_0
                    invokestatic(
                        GlStateManager::class,
                        "rotate",
                        void,
                        float,
                        float,
                        float,
                        float
                    )
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
                    ldc(-1.0F)
                    fconst_0
                    fconst_0
                    invokestatic(
                        GlStateManager::class,
                        "rotate",
                        void,
                        float,
                        float,
                        float,
                        float
                    )
                    goto(L[13])

                    +L[2]
                    aload_0
                    getfield(EntityRenderer::class, "mc", Minecraft::class)
                    getfield(
                        Minecraft::class,
                        "gameSettings",
                        GameSettings::class
                    )
                    getfield(GameSettings::class, "thirdPersonView", int)
                    ifle(L[12])
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
                    getfield(
                        Minecraft::class,
                        "gameSettings",
                        GameSettings::class
                    )
                    getfield(GameSettings::class, "debugCamEnable", boolean)
                    ifeq(L[3])
                    fconst_0
                    fconst_0
                    dload(11)
                    dneg
                    d2f
                    invokestatic(GlStateManager::class, "translate", void, float, float, float)
                    goto(L[11])

                    +L[3]
                    aload_3
                    getfield(Entity::class, "rotationYaw", float)
                    fstore(13)
                    aload_3
                    getfield(Entity::class, "rotationPitch", float)
                    fstore(14)
                    aload_2
                    getfield(PerspectiveModifierHandler::class, "enabled", boolean)
                    ifeq(L[4])
                    aload_2
                    getfield(PerspectiveModifierHandler::class, "modifiedYaw", float)
                    fstore(13)
                    aload_2
                    getfield(PerspectiveModifierHandler::class, "modifiedPitch", float)
                    fstore(14)

                    +L[4]
                    aload_0
                    getfield(EntityRenderer::class, "mc", Minecraft::class)
                    getfield(
                        Minecraft::class,
                        "gameSettings",
                        GameSettings::class
                    )
                    getfield(GameSettings::class, "thirdPersonView", int)
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
                    invokestatic(MathHelper::class, "sin", float, float)
                    fneg
                    fload(14)
                    ldc(180.0F)
                    fdiv
                    ldc(3.1415927F)
                    fmul
                    invokestatic(MathHelper::class, "cos", float, float)
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
                    invokestatic(MathHelper::class, "cos", float, float)
                    fload(14)
                    ldc(180.0F)
                    fdiv
                    ldc(3.1415927F)
                    fmul
                    invokestatic(MathHelper::class, "cos", float, float)
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
                    invokestatic(MathHelper::class, "sin", float, float)
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
                    getfield(EntityRenderer::class, "mc", Minecraft::class)
                    getfield(
                        Minecraft::class,
                        "theWorld",
                        WorldClient::class
                    )
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
                    invokevirtual(
                        WorldClient::class,
                        "rayTraceBlocks",
                        MovingObjectPosition::class,
                        Vec3::class,
                        Vec3::class
                    )
                    astore(25)
                    aload(25)
                    ifnull(L[7])
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
                    ifge(L[7])
                    dload(26)
                    dstore(11)

                    +L[7]
                    iinc(21, 1)
                    goto(L[6])

                    +L[8]
                    aload_0
                    getfield(EntityRenderer::class, "mc", Minecraft::class)
                    getfield(
                        Minecraft::class,
                        "gameSettings",
                        GameSettings::class
                    )
                    getfield(GameSettings::class, "thirdPersonView", int)
                    iconst_2
                    if_icmpne(L[9])
                    ldc(180.0F)
                    fconst_0
                    fconst_1
                    fconst_0
                    invokestatic(
                        GlStateManager::class,
                        "rotate",
                        void,
                        float,
                        float,
                        float,
                        float
                    )

                    +L[9]
                    aload_2
                    getfield(PerspectiveModifierHandler::class, "enabled", boolean)
                    ifeq(L[10])
                    aload_2
                    getfield(PerspectiveModifierHandler::class, "modifiedPitch", float)
                    fload(14)
                    fsub
                    fconst_1
                    fconst_0
                    fconst_0
                    invokestatic(
                        GlStateManager::class,
                        "rotate",
                        void,
                        float,
                        float,
                        float,
                        float
                    )
                    aload_2
                    getfield(PerspectiveModifierHandler::class, "modifiedYaw", float)
                    fload(13)
                    fsub
                    fconst_0
                    fconst_1
                    fconst_0
                    invokestatic(
                        GlStateManager::class,
                        "rotate",
                        void,
                        float,
                        float,
                        float,
                        float
                    )
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
                    invokestatic(
                        GlStateManager::class,
                        "rotate",
                        void,
                        float,
                        float,
                        float,
                        float
                    )
                    fload(14)
                    aload_2
                    getfield(PerspectiveModifierHandler::class, "modifiedPitch", float)
                    fsub
                    fconst_1
                    fconst_0
                    fconst_0
                    invokestatic(
                        GlStateManager::class,
                        "rotate",
                        void,
                        float,
                        float,
                        float,
                        float
                    )
                    goto(L[11])

                    +L[10]
                    aload_3
                    getfield(Entity::class, "rotationPitch", float)
                    fload(14)
                    fsub
                    fconst_1
                    fconst_0
                    fconst_0
                    invokestatic(
                        GlStateManager::class,
                        "rotate",
                        void,
                        float,
                        float,
                        float,
                        float
                    )
                    aload_3
                    getfield(Entity::class, "rotationYaw", float)
                    fload(13)
                    fsub
                    fconst_0
                    fconst_1
                    fconst_0
                    invokestatic(
                        GlStateManager::class,
                        "rotate",
                        void,
                        float,
                        float,
                        float,
                        float
                    )
                    fconst_0
                    fconst_0
                    dload(11)
                    dneg
                    d2f
                    invokestatic(GlStateManager::class, "translate", void, float, float, float)
                    fload(13)
                    aload_3
                    getfield(Entity::class, "rotationYaw", float)
                    fsub
                    fconst_0
                    fconst_1
                    fconst_0
                    invokestatic(
                        GlStateManager::class,
                        "rotate",
                        void,
                        float,
                        float,
                        float,
                        float
                    )
                    fload(14)
                    aload_3
                    getfield(Entity::class, "rotationPitch", float)
                    fsub
                    fconst_1
                    fconst_0
                    fconst_0
                    invokestatic(
                        GlStateManager::class,
                        "rotate",
                        void,
                        float,
                        float,
                        float,
                        float
                    )

                    +L[11]
                    goto(L[13])

                    +L[12]
                    fconst_0
                    fconst_0
                    ldc(-0.1F)
                    invokestatic(GlStateManager::class, "translate", void, float, float, float)

                    +L[13]
                    aload_0
                    getfield(EntityRenderer::class, "mc", Minecraft::class)
                    getfield(
                        Minecraft::class,
                        "gameSettings",
                        GameSettings::class
                    )
                    getfield(GameSettings::class, "debugCamEnable", boolean)
                    ifne(L[16])
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
                    ldc(180.0F)
                    fadd
                    fstore(11)
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
                    fstore(12)
                    fconst_0
                    fstore(13)
                    aload_3
                    instanceof(EntityAnimal::class)
                    ifeq(L[14])
                    aload_3
                    checkcast(EntityAnimal::class)
                    astore(14)
                    aload(14)
                    getfield(EntityAnimal::class, "prevRotationYawHead", float)
                    aload(14)
                    getfield(EntityAnimal::class, "rotationYawHead", float)
                    aload(14)
                    getfield(EntityAnimal::class, "prevRotationYawHead", float)
                    fsub
                    fload_1
                    fmul
                    fadd
                    ldc(180.0F)
                    fadd
                    fstore(11)

                    +L[14]
                    aload_2
                    getfield(PerspectiveModifierHandler::class, "enabled", boolean)
                    ifeq(L[15])
                    fconst_0
                    fconst_0
                    fconst_0
                    fconst_1
                    invokestatic(
                        GlStateManager::class,
                        "rotate",
                        void,
                        float,
                        float,
                        float,
                        float
                    )
                    aload_2
                    getfield(PerspectiveModifierHandler::class, "modifiedPitch", float)
                    fconst_1
                    fconst_0
                    fconst_0
                    invokestatic(
                        GlStateManager::class,
                        "rotate",
                        void,
                        float,
                        float,
                        float,
                        float
                    )
                    aload_2
                    getfield(PerspectiveModifierHandler::class, "modifiedYaw", float)
                    ldc(180.0F)
                    fadd
                    fconst_0
                    fconst_1
                    fconst_0
                    invokestatic(
                        GlStateManager::class,
                        "rotate",
                        void,
                        float,
                        float,
                        float,
                        float
                    )
                    goto(L[16])

                    +L[15]
                    fconst_0
                    fconst_0
                    fconst_0
                    fconst_1
                    invokestatic(
                        GlStateManager::class,
                        "rotate",
                        void,
                        float,
                        float,
                        float,
                        float
                    )
                    fload(12)
                    fconst_1
                    fconst_0
                    fconst_0
                    invokestatic(
                        GlStateManager::class,
                        "rotate",
                        void,
                        float,
                        float,
                        float,
                        float
                    )
                    fload(11)
                    fconst_0
                    fconst_1
                    fconst_0
                    invokestatic(
                        GlStateManager::class,
                        "rotate",
                        void,
                        float,
                        float,
                        float,
                        float
                    )

                    +L[16]
                    fconst_0
                    fload(4)
                    fneg
                    fconst_0
                    invokestatic(GlStateManager::class, "translate", void, float, float, float)
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
                    aload_0
                    aload_0
                    getfield(EntityRenderer::class, "mc", Minecraft::class)
                    getfield(
                        Minecraft::class,
                        "renderGlobal",
                        RenderGlobal::class
                    )
                    dload(5)
                    dload(7)
                    dload(9)
                    fload_1
                    invokevirtual(
                        RenderGlobal::class,
                        "hasCloudFog",
                        boolean,
                        double,
                        double,
                        double,
                        float
                    )
                    putfield(EntityRenderer::class, "cloudFog", boolean)
                    _return
                }.first
            }

            if (method.name == "updateCameraAndRender") {
                val changePerspective = assembleBlock {
                    getstatic(Hyperium::class, "INSTANCE", Hyperium::class)
                    invokevirtual(Hyperium::class, "getHandlers", HyperiumHandlers::class)
                    ifnull(L["9"])
                    getstatic(Hyperium::class, "INSTANCE", Hyperium::class)
                    invokevirtual(Hyperium::class, "getHandlers", HyperiumHandlers::class)
                    invokevirtual(HyperiumHandlers::class, "getPerspectiveHandler", PerspectiveModifierHandler::class)
                    ifnonnull(L["10"])
                    +L["9"]
                    _return
                    +L["10"]
                    getstatic(Hyperium::class, "INSTANCE", Hyperium::class)
                    invokevirtual(Hyperium::class, "getHandlers", HyperiumHandlers::class)
                    invokevirtual(HyperiumHandlers::class, "getPerspectiveHandler", PerspectiveModifierHandler::class)
                    astore(5)
                    invokestatic(Display::class, "isActive", boolean)
                    istore(6)
                    invokestatic(Minecraft::class, "getMinecraft", Minecraft::class)
                    getfield(Minecraft::class, "inGameHasFocus", boolean)
                    ifeq(L["13"])
                    iload(6)
                    ifeq(L["13"])
                    aload(5)
                    getfield(PerspectiveModifierHandler::class, "enabled", boolean)
                    ifeq(L["15"])
                    invokestatic(Minecraft::class, "getMinecraft", Minecraft::class)
                    getfield(Minecraft::class, "gameSettings", GameSettings::class)
                    getfield(GameSettings::class, "thirdPersonView", int)
                    iconst_1
                    if_icmpeq(L["15"])
                    aload(5)
                    invokevirtual(PerspectiveModifierHandler::class, "onDisable", void)
                    +L["15"]
                    aload(5)
                    getfield(PerspectiveModifierHandler::class, "enabled", boolean)
                    ifeq(L["13"])
                    invokestatic(Minecraft::class, "getMinecraft", Minecraft::class)
                    getfield(Minecraft::class, "mouseHelper", MouseHelper::class)
                    invokevirtual(MouseHelper::class, "mouseXYChange", void)
                    invokestatic(Minecraft::class, "getMinecraft", Minecraft::class)
                    getfield(Minecraft::class, "gameSettings", GameSettings::class)
                    getfield(GameSettings::class, "mouseSensitivity", float)
                    ldc(0.6)
                    fmul
                    ldc(0.2)
                    fadd
                    fstore(7)
                    fload(7)
                    fload(7)
                    fmul
                    ldc(8.0)
                    fmul
                    fstore(8)
                    invokestatic(Minecraft::class, "getMinecraft", Minecraft::class)
                    getfield(Minecraft::class, "mouseHelper", MouseHelper::class)
                    getfield(MouseHelper::class, "deltaX", int)
                    i2f
                    fload(8)
                    fmul
                    fstore(9)
                    invokestatic(Minecraft::class, "getMinecraft", Minecraft::class)
                    getfield(Minecraft::class, "mouseHelper", MouseHelper::class)
                    getfield(MouseHelper::class, "deltaY", int)
                    i2f
                    fload(8)
                    fmul
                    fstore(10)
                    aload(5)
                    dup
                    getfield(PerspectiveModifierHandler::class, "modifiedYaw", float)
                    fload(9)
                    ldc(8.0)
                    fdiv
                    fadd
                    putfield(PerspectiveModifierHandler::class, "modifiedYaw", float)
                    aload(5)
                    dup
                    getfield(PerspectiveModifierHandler::class, "modifiedPitch", float)
                    fload(10)
                    ldc(8.0)
                    fdiv
                    fadd
                    putfield(PerspectiveModifierHandler::class, "modifiedPitch", float)
                    aload(5)
                    getfield(PerspectiveModifierHandler::class, "modifiedPitch", float)
                    invokestatic(Math::class, "abs", float, float)
                    ldc(90.0)
                    fcmpl
                    ifle(L["3"])
                    aload(5)
                    getfield(PerspectiveModifierHandler::class, "modifiedPitch", float)
                    fconst_0
                    fcmpl
                    ifle(L["26"])
                    aload(5)
                    ldc(90.0)
                    putfield(PerspectiveModifierHandler::class, "modifiedPitch", float)
                    goto(L["13"])
                    aload(5)
                    ldc(-90.0)
                    putfield(PerspectiveModifierHandler::class, "modifiedPitch", float)
                    +L["13"]
                }.first


                val postRenderEvent = assembleBlock {
                    getstatic(EventBus::class, "INSTANCE", EventBus::class)
                    new(RenderEvent::class)
                    dup
                    invokespecial(RenderEvent::class, "<init>", void)
                    invokevirtual(EventBus::class, "post", void, Object::class)
                }.first


                for (insn in method.instructions.iterator()) {
                    if (insn is LdcInsnNode) {
                        if (insn.cst == "mouse") {
                            method.instructions.insertBefore(insn.previous?.previous?.previous, changePerspective)
                        }

                        if (insn.cst == "gui") {
                            method.instructions.insertBefore(insn.next?.next, postRenderEvent)
                        }
                    }
                }
            }

            if (method.name == "renderWorldPass") {
                val postDrawBlockHighlightEvent = assembleBlock {
                    new(DrawBlockHighlightEvent::class)
                    dup
                    aload_0
                    getfield(EntityRenderer::class, "mc", Minecraft::class)
                    invokevirtual(Minecraft::class, "getRenderViewEntity", Entity::class)
                    checkcast(EntityPlayer::class)
                    aload_0
                    getfield(EntityRenderer::class, "mc", Minecraft::class)
                    getfield(Minecraft::class, "objectMouseOver", MovingObjectPosition::class)
                    fload_2
                    invokespecial(
                        DrawBlockHighlightEvent::class,
                        "<init>",
                        void,
                        EntityPlayer::class,
                        MovingObjectPosition::class,
                        float
                    )
                    astore(17)
                    getstatic(EventBus::class, "INSTANCE", EventBus::class)
                    aload(17)
                    invokevirtual(EventBus::class, "post", void, Object::class)
                    aload(17)
                    invokevirtual(DrawBlockHighlightEvent::class, "isCancelled", boolean)
                    ifeq(L["78"])
                    getstatic(Hyperium::class, "INSTANCE", Hyperium::class)
                    invokevirtual(Hyperium::class, "getHandlers", HyperiumHandlers::class)
                    invokevirtual(HyperiumHandlers::class, "getConfigOptions", OtherConfigOptions::class)
                    iconst_1
                    putfield(OtherConfigOptions::class, "isCancelBox", boolean)
                    +L["78"]
                }.first

                val postRenderWorldEvent = assembleBlock {
                    aload_0
                    getfield(EntityRenderer::class, "mc", Minecraft::class)
                    getfield(Minecraft::class, "mcProfiler", Profiler::class)
                    ldc("hyperium_render_last")
                    invokevirtual(Profiler::class, "startSection", void, String::class)
                    new(RenderWorldEvent::class)
                    dup
                    aload_0
                    getfield(EntityRenderer::class, "mc", Minecraft::class)
                    getfield(Minecraft::class, "renderGlobal", RenderGlobal::class)
                    fload_2
                    invokespecial(RenderWorldEvent::class, "<init>", void, RenderGlobal::class, float)
                    invokevirtual(RenderWorldEvent::class, "post", void)
                    aload_0
                    getfield(EntityRenderer::class, "mc", Minecraft::class)
                    getfield(Minecraft::class, "mcProfiler", Profiler::class)
                    invokevirtual(Profiler::class, "endSection", void)
                }.first

                for (insn in method.instructions.iterator()) {
                    if (insn is LdcInsnNode) {
                        if (insn.cst == "outline") {
                            method.instructions.insertBefore(
                                insn.previous?.previous?.previous,
                                postDrawBlockHighlightEvent
                            )
                        }

                        if (insn.cst == "hand") {
                            method.instructions.insertBefore(
                                insn.previous?.previous?.previous,
                                postRenderWorldEvent
                            )
                        }
                    }
                }
            }
        }

        original.methods.forEach {
            println(it.name)
        }

        return original
    }

    override fun getClassName() = "bfk"
}
