package cc.hyperium.launch.patching.conflicts;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Iterator;

public class AbstractClientPlayerTransformer implements ConflictTransformer {
    @Override
    public String getClassName() {
        return "net.minecraft.client.entity.AbstractClientPlayer";
    }

    @Override
    public ClassNode transform(ClassNode original) {
        // private HyperiumCapeHandler hook;
        original.visitField(Opcodes.ACC_PRIVATE, "hook", "Lcc/hyperium/handlers/handlers/animation/cape/HyperiumCapeHandler;", null, null).visitEnd();
        for (MethodNode node : original.methods) {
            switch (node.name) {
                case "<init>": {
                    InsnList l = new InsnList();
                    // hook = new HyperiumCapeHandler(playerProfile);
                    l.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    l.add(new TypeInsnNode(Opcodes.NEW, "cc/hyperium/handlers/handlers/animation/cape/HyperiumCapeHandler"));
                    l.add(new InsnNode(Opcodes.DUP));
                    l.add(new VarInsnNode(Opcodes.ALOAD, 2));
                    l.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "cc/hyperium/handlers/handlers/animation/cape/HyperiumCapeHandler", "<init>",
                            "(Lcom/mojang/authlib/GameProfile;)V", false));
                    l.add(new FieldInsnNode(Opcodes.PUTFIELD, "bet", "hook", "Lcc/hyperium/handlers/handlers/animation/cape/HyperiumCapeHandler;"));
                    Iterator<AbstractInsnNode> it = node.instructions.iterator();
                    while (it.hasNext()) {
                        AbstractInsnNode insn = it.next();
                        if (insn.getOpcode() == Opcodes.RETURN) {
                            node.instructions.insertBefore(insn, l);
                        }
                    }
                    break;
                }
                case "getPlayerInfo":  // there's only one method called b
                    node.access = Opcodes.ACC_PUBLIC;
                    break;
                case "getSkinType": { // there's only one method called c
                    InsnList l = new InsnList();
                    LabelNode l2 = new LabelNode();
                    LabelNode l5 = new LabelNode();
                    LabelNode l7 = new LabelNode();
                    LabelNode l8 = new LabelNode();
                    LabelNode l10 = new LabelNode();
                    LabelNode l11 = new LabelNode();
                    // NickHider instance = NickHider.instance;
                    l.add(new FieldInsnNode(Opcodes.GETSTATIC, "cc/hyperium/mods/nickhider/NickHider", "instance", "Lcc/hyperium/mods/nickhider/NickHider;"));
                    l.add(new VarInsnNode(Opcodes.ASTORE, 1));
                    // instance != null
                    l.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    l.add(new JumpInsnNode(Opcodes.IFNULL, l2));
                    // instance.getNickHiderConfig().isHideSkins()
                    l.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/NickHider", "getNickHiderConfig", "()Lcc/hyperium/mods/nickhider/config/NickHiderConfig;", false));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/config/NickHiderConfig", "isHideSkins", "()Z", false));
                    l.add(new JumpInsnNode(Opcodes.IFEQ, l2));
                    // instance.getNickHiderConfig().isMasterEnabled()
                    l.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/NickHider", "getNickHiderConfig", "()Lcc/hyperium/mods/nickhider/config/NickHiderConfig;", false));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/config/NickHiderConfig", "isMasterEnabled", "()Z", false));
                    l.add(new JumpInsnNode(Opcodes.IFEQ, l2));
                    // NickHiderConfig config = instance.getNickHiderConfig();
                    l.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/NickHider", "getNickHiderConfig", "()Lcc/hyperium/mods/nickhider/config/NickHiderConfig;", false));
                    l.add(new VarInsnNode(Opcodes.ASTORE, 2));
                    // getUniqueID().equals(Minecraft.getMinecraft().thePlayer.getUniqueID())
                    l.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/entity/AbstractClientPlayer", "getUniqueID", "()Ljava/util/UUID;", false));
                    l.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/client/Minecraft", "getMinecraft", "()Lnet/minecraft/client/Minecraft;", false));
                    l.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/Minecraft", "thePlayer", "Lnet/minecraft/client/entity/EntityPlayerSP;"));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "bew", "aK", "()Ljava/util/UUID;", false));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/util/UUID", "equals", "(Ljava/lang/Object;)Z", false));
                    l.add(new JumpInsnNode(Opcodes.IFEQ, l5));
                    // some return stuff
                    l.add(new VarInsnNode(Opcodes.ALOAD, 2));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/config/NickHiderConfig", "isUseRealSkinForSelf", "()Z", false));
                    l.add(new JumpInsnNode(Opcodes.IFEQ, l7));
                    l.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/NickHider", "getPlayerSkin", "()Ljy;", false));
                    l.add(new JumpInsnNode(Opcodes.IFNULL, l7));
                    l.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/NickHider", "getPlayerSkin", "()Ljy;", false));
                    l.add(new JumpInsnNode(Opcodes.GOTO, l8));
                    // some other stuff
                    l.add(l7);
                    l.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/entity/AbstractClientPlayer", "getUniqueID", "()Ljava/util/UUID;", false));
                    l.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/client/resources/DefaultPlayerSkin", "getDefaultSkin", "(Ljava/util/UUID;)Lnet/minecraft/util/ResourceLocation;", false));
                    // actually return
                    l.add(l8);
                    l.add(new InsnNode(Opcodes.ARETURN));
                    // else block
                    l.add(l5);
                    l.add(new VarInsnNode(Opcodes.ALOAD, 2));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/config/NickHiderConfig", "isHideOtherSkins", "()Z", false));
                    l.add(new JumpInsnNode(Opcodes.IFEQ, l2));
                    // return stuff
                    l.add(new VarInsnNode(Opcodes.ALOAD, 2));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/config/NickHiderConfig", "isUsePlayerSkinForAll", "()Z", false));
                    l.add(new JumpInsnNode(Opcodes.IFEQ, l10));
                    l.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/NickHider", "getPlayerSkin", "()Ljy;", false));
                    l.add(new JumpInsnNode(Opcodes.IFNULL, l10));
                    l.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/NickHider", "getPlayerSkin", "()Ljy;", false));
                    l.add(new JumpInsnNode(Opcodes.GOTO, l11));
                    // more return stuff
                    l.add(l10);
                    l.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/entity/AbstractClientPlayer", "getUniqueID", "()Ljava/util/UUID;", false));
                    l.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/client/resources/DefaultPlayerSkin", "getDefaultSkin", "(Ljava/util/UUID;)Lnet/minecraft/util/ResourceLocation;", false));
                    // actually return
                    l.add(l11);
                    l.add(new InsnNode(Opcodes.ARETURN));
                    // default stuff
                    l.add(l2);
                    l.add(node.instructions);
                    node.instructions = l;
                    break;
                }
                case "k": {
                    InsnList l = new InsnList();
                    LabelNode l1 = new LabelNode();
                    LabelNode l4 = new LabelNode();
                    // if (hook.getLocationCape() != null)
                    l.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    l.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/entity/AbstractClientPlayer", "hook", "Lcc/hyperium/handlers/handlers/animation/cape/HyperiumCapeHandler;"));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/handlers/handlers/animation/cape/HyperiumCapeHandler", "getLocationCape", "()Lnet/minecraft/util/ResourceLocation;", false));
                    l.add(new JumpInsnNode(Opcodes.IFNULL, l1));
                    // return hook.getLocationCape();
                    l.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    l.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/entity/AbstractClientPlayer", "hook", "Lcc/hyperium/handlers/handlers/animation/cape/HyperiumCapeHandler;"));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/handlers/handlers/animation/cape/HyperiumCapeHandler", "getLocationCape", "()Lnet/minecraft/util/ResourceLocation;", false));
                    l.add(new InsnNode(Opcodes.ARETURN));
                    // NickHider instance = NickHider.instance;
                    l.add(l1);
                    l.add(new FieldInsnNode(Opcodes.GETSTATIC, "cc/hyperium/mods/nickhider/NickHider", "instance", "Lcc/hyperium/mods/nickhider/NickHider;"));
                    l.add(new VarInsnNode(Opcodes.ASTORE, 1));
                    // instance != null
                    l.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    l.add(new JumpInsnNode(Opcodes.IFNULL, l4));
                    // instance.getNickHiderConfig().isHideSkins()
                    l.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/NickHider", "getNickHiderConfig", "()Lcc/hyperium/mods/nickhider/config/NickHiderConfig;", false));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/config/NickHiderConfig", "isHideSkins", "()Z", false));
                    l.add(new JumpInsnNode(Opcodes.IFEQ, l4));
                    // instance.getNickHiderConfig().isMasterEnabled()
                    l.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/NickHider", "getNickHiderConfig", "()Lcc/hyperium/mods/nickhider/config/NickHiderConfig;", false));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/config/NickHiderConfig", "isMasterEnabled", "()Z", false));
                    l.add(new JumpInsnNode(Opcodes.IFEQ, l4));
                    // NickHiderConfig config = instance.getNickHiderConfig();
                    l.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/NickHider", "getNickHiderConfig", "()Lcc/hyperium/mods/nickhider/config/NickHiderConfig;", false));
                    l.add(new VarInsnNode(Opcodes.ASTORE, 2));
                    // getUniqueID().equals(Minecraft.getMinecraft().thePlayer.getUniqueID()) && config.isUseRealSkinForSelf()
                    l.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/entity/AbstractClientPlayer", "getUniqueID", "()Ljava/util/UUID;", false));
                    l.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/client/Minecraft", "getMinecraft", "()Lnet/minecraft/client/Minecraft;", false));
                    l.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/Minecraft", "thePlayer", "Lnet/minecraft/client/entity/EntityPlayerSP;"));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/entity/EntityPlayerSP", "getUniqueID", "()Ljava/util/UUID;", false));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/util/UUID", "equals", "(Ljava/lang/Object;)Z", false));
                    l.add(new JumpInsnNode(Opcodes.IFEQ, l4));
                    l.add(new VarInsnNode(Opcodes.ALOAD, 2));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/config/NickHiderConfig", "isUseRealSkinForSelf", "()Z", false));
                    // return instance.getPlayerCape();
                    l.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/NickHider", "getPlayerCape", "()Ljy;", false));
                    l.add(new InsnNode(Opcodes.ARETURN));
                    // vanilla stuff
                    l.add(l4);
                    l.add(node.instructions);
                    node.instructions = l;
                    break;
                }
                case "l": {
                    InsnList l = new InsnList();

                    LabelNode l2 = new LabelNode();
                    LabelNode l5 = new LabelNode();
                    // NickHider instance = NickHider.instance;
                    l.add(new FieldInsnNode(Opcodes.GETSTATIC, "cc/hyperium/mods/nickhider/NickHider", "instance", "Lcc/hyperium/mods/nickhider/NickHider;"));
                    l.add(new VarInsnNode(Opcodes.ASTORE, 1));
                    // instance != null
                    l.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    l.add(new JumpInsnNode(Opcodes.IFNULL, l2));
                    // instance.getNickHiderConfig().isHideSkins()
                    l.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/NickHider", "getNickHiderConfig", "()Lcc/hyperium/mods/nickhider/config/NickHiderConfig;", false));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/config/NickHiderConfig", "isHideSkins", "()Z", false));
                    l.add(new JumpInsnNode(Opcodes.IFEQ, l2));
                    // instance.getNickHiderConfig().isMasterEnabled()
                    l.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/NickHider", "getNickHiderConfig", "()Lcc/hyperium/mods/nickhider/config/NickHiderConfig;", false));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/config/NickHiderConfig", "isMasterEnabled", "()Z", false));
                    l.add(new JumpInsnNode(Opcodes.IFEQ, l2));
                    // NickHiderConfig config = instance.getNickHiderConfig();
                    l.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/NickHider", "getNickHiderConfig", "()Lcc/hyperium/mods/nickhider/config/NickHiderConfig;", false));
                    l.add(new VarInsnNode(Opcodes.ASTORE, 2));
                    // if (getUniqueID().equals(Minecraft.getMinecraft().thePlayer.getUniqueID()))
                    l.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/entity/AbstractClientPlayer", "getUniqueID", "()Ljava/util/UUID;", false));
                    l.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/client/Minecraft", "getMinecraft", "()Lnet/minecraft/client/Minecraft;", false));
                    l.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/Minecraft", "thePlayer", "Lbew;"));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/entity/EntityPlayerSP", "getUniqueID", "()Ljava/util/UUID;", false));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/util/UUID", "equals", "(Ljava/lang/Object;)Z", false));
                    l.add(new JumpInsnNode(Opcodes.IFEQ, l5));
                    // if (config.isUseRealSkinForSelf() && instance.getPlayerSkin() != null)
                    l.add(new VarInsnNode(Opcodes.ALOAD, 2));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/config/NickHiderConfig", "isUseRealSkinForSelf", "()Z", false));
                    l.add(new JumpInsnNode(Opcodes.IFEQ, l2));
                    l.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/NickHider", "getPlayerSkin", "()Ljy;", false));
                    l.add(new JumpInsnNode(Opcodes.IFNULL, l2));
                    // return instance.getPlayerCape();
                    l.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/NickHider", "getPlayerRealSkinType", "()Ljava/lang/String;", false));
                    l.add(new InsnNode(Opcodes.ARETURN));
                    // else if (config.isHideOtherSkins() && config.isUsePlayerSkinForAll() && instance.getPlayerSkin() != null)
                    l.add(l5);
                    l.add(new VarInsnNode(Opcodes.ALOAD, 2));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/config/NickHiderConfig", "isHideOtherSkins", "()Z", false));
                    l.add(new JumpInsnNode(Opcodes.IFEQ, l2));
                    l.add(new VarInsnNode(Opcodes.ALOAD, 2));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/config/NickHiderConfig", "isUsePlayerSkinForAll", "()Z", false));
                    l.add(new JumpInsnNode(Opcodes.IFEQ, l2));
                    l.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/NickHider", "getPlayerSkin", "()Ljy;", false));
                    l.add(new JumpInsnNode(Opcodes.IFNULL, l2));
                    // return instance.getPlayerRealSkinType();
                    l.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/NickHider", "getPlayerRealSkinType", "()Ljava/lang/String;", false));
                    l.add(new InsnNode(Opcodes.ARETURN));

                    // vanilla stuff
                    l.add(l2);
                    l.add(node.instructions);
                    node.instructions = l;
                    break;
                }
                case "o":
                    Iterator<AbstractInsnNode> iterator = node.instructions.iterator();
                    while (iterator.hasNext()) {
                        AbstractInsnNode insn = iterator.next();
                        if (insn.getOpcode() == Opcodes.FRETURN) {
                            InsnList l = new InsnList();

                            // FovUpdateEvent event = new FovUpdateEvent(this, f);
                            l.add(new TypeInsnNode(Opcodes.NEW, "cc/hyperium/event/entity/FovUpdateEvent"));
                            l.add(new InsnNode(Opcodes.DUP));
                            l.add(new VarInsnNode(Opcodes.ALOAD, 0));
                            l.add(new VarInsnNode(Opcodes.FLOAD, 1));
                            l.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "cc/hyperium/event/entity/FovUpdateEvent", "<init>", "(Lwn;F)V", false));
                            l.add(new VarInsnNode(Opcodes.ASTORE, 3));
                            // EventBus.INSTANCE.post(event);
                            l.add(new FieldInsnNode(Opcodes.GETSTATIC, "cc/hyperium/event/EventBus", "INSTANCE", "Lcc/hyperium/event/EventBus;"));
                            l.add(new VarInsnNode(Opcodes.ALOAD, 3));
                            l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/event/EventBus", "post", "(Ljava/lang/Object;)V", false));
                            // load new fov
                            l.add(new VarInsnNode(Opcodes.ALOAD, 3));
                            l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/event/entity/FovUpdateEvent", "getNewFov", "()F", false));

                            node.instructions.insertBefore(insn, l);
                        }
                    }
                    break;
            }
        }
        return original;
    }
}
