package cc.hyperium.mods.chunkanimator;

/*
 * Ported by Cubxity on 12/1/2018
 */

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import org.jetbrains.annotations.NotNull;
import java.util.WeakHashMap;

public class AnimationHandler {
    private WeakHashMap<RenderChunk, AnimationData> timeStamps = new WeakHashMap<>();

    public void setPosition(RenderChunk rc, BlockPos bp) {
        if (!ChunkAnimatorConfig.enabled)
            return;
        if (Minecraft.getMinecraft().thePlayer != null) {
            boolean flag = true;
            BlockPos zeroedPlayerPosition = Minecraft.getMinecraft().thePlayer.getPosition();
            zeroedPlayerPosition = zeroedPlayerPosition.add(0, -zeroedPlayerPosition.getY(), 0);
            BlockPos zeroedCenteredChunkPos = bp.add(8, -bp.getY(), 8);

            if (ChunkAnimatorConfig.disableAroundPlayer) {
                flag = zeroedPlayerPosition.distanceSq(zeroedCenteredChunkPos) > (64 * 64);
            }

            if (flag) {
                EnumFacing chunkFacing = null;

                if (ChunkAnimatorConfig.mode.equals("From sides")) {
                    Vec3i dif = zeroedPlayerPosition.subtract(zeroedCenteredChunkPos);

                    int difX = Math.abs(dif.getX());
                    int difZ = Math.abs(dif.getZ());

                    chunkFacing = getFacing(dif, difX, difZ);
                }

                AnimationData animationData = new AnimationData(-1L, chunkFacing);
                timeStamps.put(rc, animationData);
            }
        }
    }

    public void preRenderChunk(RenderChunk renderChunk) {
        if (!ChunkAnimatorConfig.enabled)
            return;
        if (this.timeStamps.containsKey(renderChunk)) {
            final AnimationData animationData = this.timeStamps.get(renderChunk);
            long time = animationData.timeStamp;
            String mode = ChunkAnimatorConfig.mode;
            if (time == -1L) {
                time = System.currentTimeMillis();
                animationData.timeStamp = time;
                if (mode.equals("From direction you're facing")) {
                    BlockPos zeroedPlayerPosition = Minecraft.getMinecraft().thePlayer.getPosition();
                    zeroedPlayerPosition = zeroedPlayerPosition.add(0, -zeroedPlayerPosition.getY(), 0);
                    final BlockPos zeroedCenteredChunkPos = renderChunk.getPosition().add(8, -renderChunk.getPosition().getY(), 8);
                    final Vec3i dif = zeroedPlayerPosition.subtract(zeroedCenteredChunkPos);
                    final int difX = Math.abs(dif.getX());
                    final int difZ = Math.abs(dif.getZ());
                    animationData.chunkFacing = getFacing(dif, difX, difZ);
                }
            }
            final long timeDif = System.currentTimeMillis() - time;
            final int animationDuration = ChunkAnimatorConfig.animDuration;
            if (timeDif < animationDuration) {
                final double chunkY = renderChunk.getPosition().getY();
                if (mode.equals("Down from sky 2")) {
                    if (chunkY < Minecraft.getMinecraft().theWorld.getHorizon()) {
                        mode = "Up from ground";
                    } else {
                        mode = "Down from sky 1";
                    }
                }
                if (mode.equals("From direction you're facing")) {
                    mode = "From sides";
                }
                switch (mode) {
                    case "Up from ground": {
                        final double modY = chunkY / animationDuration * timeDif;
                        GlStateManager.translate(0.0, -chunkY + modY, 0.0);
                        break;
                    }
                    case "Down from sky 1": {
                        final double modY = (256.0 - chunkY) / animationDuration * timeDif;
                        GlStateManager.translate(0.0, 256.0 - chunkY - modY, 0.0);
                        break;
                    }
                    case "From sides": {
                        final EnumFacing chunkFacing2 = animationData.chunkFacing;
                        if (chunkFacing2 != null) {
                            final Vec3i vec = chunkFacing2.getDirectionVec();
                            final double mod = -(200.0 - 200.0 / animationDuration * timeDif);
                            GlStateManager.translate(vec.getX() * mod, 0.0, vec.getZ() * mod);
                            break;
                        }
                        break;
                    }
                }
            } else {
                this.timeStamps.remove(renderChunk);
            }
        }
    }

    @NotNull
    private EnumFacing getFacing(Vec3i dif, int difX, int difZ) {
        EnumFacing chunkFacing;
        if (difX > difZ) {
            if (dif.getX() > 0) {
                chunkFacing = EnumFacing.EAST;
            } else {
                chunkFacing = EnumFacing.WEST;
            }
        } else {
            if (dif.getZ() > 0) {
                chunkFacing = EnumFacing.SOUTH;
            } else {
                chunkFacing = EnumFacing.NORTH;
            }
        }
        return chunkFacing;
    }

    private class AnimationData {
        long timeStamp;
        EnumFacing chunkFacing;

        AnimationData(final long timeStamp, final EnumFacing chunkFacing) {
            this.timeStamp = timeStamp;
            this.chunkFacing = chunkFacing;
        }
    }
}
