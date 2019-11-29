package cc.hyperium.launch.patching.conflicts;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Iterator;

public class AbstractClientPlayerTransformer implements ConflictTransformer {
    @Override
    public String getClassName() {
        return "bet";
    }

    @Override
    public ClassNode transform(ClassNode original) {
        // private HyperiumCapeHandler hook;
        original.visitField(Opcodes.ACC_PRIVATE, "hook", "Lcc/hyperium/handlers/handlers/animation/cape/HyperiumCapeHandler;", null, null).visitEnd();
        for (MethodNode node : original.methods) {
            if (node.name.equals("<init>")) {
                InsnList l = new InsnList();
                // hook = new HyperiumCapeHandler(playerProfile);
                l.add(new VarInsnNode(Opcodes.ALOAD, 0));
                l.add(new TypeInsnNode(Opcodes.NEW, "cc/hyperium/handlers/handlers/animation/cape/HyperiumCapeHandler"));
                l.add(new InsnNode(Opcodes.DUP));
                l.add(new VarInsnNode(Opcodes.ALOAD, 2));
                l.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "cc/hyperium/handlers/handlers/animation/cape/HyperiumCapeHandler", "<init>", "(Lcom/mojang/authlib/GameProfile;)V", false));
                l.add(new FieldInsnNode(Opcodes.PUTFIELD, "bet", "hook", "Lcc/hyperium/handlers/handlers/animation/cape/HyperiumCapeHandler;"));
                l.add(new InsnNode(Opcodes.RETURN));
                System.out.println(l.size());
                System.out.println(node.instructions.size());
                node.instructions.remove(new InsnNode(Opcodes.RETURN));
                System.out.println(node.instructions.size());
                node.instructions.add(l);
                System.out.println(node.instructions.size());
            } else if (node.name.equals("b")) { // there's only one method called b
                node.access = Opcodes.ACC_PUBLIC;
            } else if (node.name.equals("c")) { // there's only one method called c
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
                l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "bet", "aK", "()Ljava/util/UUID;", false));
                l.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ave", "A", "()Lave;", false));
                l.add(new FieldInsnNode(Opcodes.GETFIELD, "ave", "h", "Lbew;"));
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
                l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "bet", "aK", "()Ljava/util/UUID;", false));
                l.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "bmz", "a", "(Ljava/util/UUID;)Ljy;", false));
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
                l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "bet", "aK", "()Ljava/util/UUID;", false));
                l.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "bmz", "a", "(Ljava/util/UUID;)Ljy;", false));
                // actually return
                l.add(l11);
                l.add(new InsnNode(Opcodes.ARETURN));
                // default stuff
                l.add(l2);
                l.add(node.instructions);
                node.instructions = l;
            } else if (node.name.equals("k")) {
                InsnList l = new InsnList();
                LabelNode l1 = new LabelNode();
                LabelNode l4 = new LabelNode();
                // if (hook.getLocationCape() != null)
                l.add(new VarInsnNode(Opcodes.ALOAD, 0));
                l.add(new FieldInsnNode(Opcodes.GETFIELD, "bet", "hook", "Lcc/hyperium/handlers/handlers/animation/cape/HyperiumCapeHandler;"));
                l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/handlers/handlers/animation/cape/HyperiumCapeHandler", "getLocationCape", "()Ljy;", false));
                l.add(new JumpInsnNode(Opcodes.IFNULL, l1));
                // return hook.getLocationCape();
                l.add(new VarInsnNode(Opcodes.ALOAD, 0));
                l.add(new FieldInsnNode(Opcodes.GETFIELD, "bet", "hook", "Lcc/hyperium/handlers/handlers/animation/cape/HyperiumCapeHandler;"));
                l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/handlers/handlers/animation/cape/HyperiumCapeHandler", "getLocationCape", "()Ljy;", false));
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
                l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "bet", "aK", "()Ljava/util/UUID;", false));
                l.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ave", "A", "()Lave;", false));
                l.add(new FieldInsnNode(Opcodes.GETFIELD, "ave", "h", "Lbew;"));
                l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "bew", "aK", "()Ljava/util/UUID;", false));
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
            } else if (node.name.equals("l")) {
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
                l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "bet", "aK", "()Ljava/util/UUID;", false));
                l.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "ave", "A", "()Lave;", false));
                l.add(new FieldInsnNode(Opcodes.GETFIELD, "ave", "h", "Lbew;"));
                l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "bew", "aK", "()Ljava/util/UUID;", false));
                l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/util/UUID", "equals", "(Ljava/lang/Object;)Z", false));
                l.add(new JumpInsnNode(Opcodes.IFEQ, l5));
                // if (config.isUseRealSkinForSelf() && instance.getPlayerSkin() != null)
                l.add(new VarInsnNode(Opcodes.ALOAD, 2));
                l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/config/NickHiderConfig", "isUseRealSkinForSelf", "()Z", false));
                l.add(new JumpInsnNode(Opcodes.IFEQ, l2));
                l.add(new VarInsnNode(Opcodes.ALOAD, 1));
                l.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cc/hyperium/mods/nickhider/NickHider", "getPlayerSkin", "()Ljy;", false));
                l.add(new JumpInsnNode(Opcodes.IFEQ, l2));
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
            } else if (node.name.equals("o")) {
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

                        System.out.println("Adding INSNs");
                        node.instructions.insertBefore(insn, l);
                    }
                }
            }
        }
        return original;
    }
}
