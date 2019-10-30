/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
        if (!ChunkAnimatorConfig.enabled) return;
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
        if (!ChunkAnimatorConfig.enabled) return;
        if (timeStamps.containsKey(renderChunk)) {
            AnimationData animationData = timeStamps.get(renderChunk);
            long time = animationData.timeStamp;
            String mode = ChunkAnimatorConfig.mode;
            if (time == -1L) {
                time = System.currentTimeMillis();
                animationData.timeStamp = time;

                if (mode.equals("From direction you're facing")) {
                    BlockPos zeroedPlayerPosition = Minecraft.getMinecraft().thePlayer.getPosition();
                    zeroedPlayerPosition = zeroedPlayerPosition.add(0, -zeroedPlayerPosition.getY(), 0);
                    BlockPos zeroedCenteredChunkPos = renderChunk.getPosition().add(8, -renderChunk.getPosition().getY(), 8);
                    Vec3i dif = zeroedPlayerPosition.subtract(zeroedCenteredChunkPos);
                    int difX = Math.abs(dif.getX());
                    int difZ = Math.abs(dif.getZ());
                    animationData.chunkFacing = getFacing(dif, difX, difZ);
                }
            }
            long timeDif = System.currentTimeMillis() - time;
            int animationDuration = ChunkAnimatorConfig.animDuration;
            if (timeDif < animationDuration) {
                double chunkY = renderChunk.getPosition().getY();

                if (mode.equals("Down from sky 2")) {
                    mode = chunkY < Minecraft.getMinecraft().theWorld.getHorizon() ? "Up from ground" : "Down from sky 1";
                }

                if (mode.equals("From direction you're facing")) mode = "From sides";

                switch (mode) {
                    case "Up from ground": {
                        double modY = chunkY / animationDuration * timeDif;
                        GlStateManager.translate(0.0, -chunkY + modY, 0.0);
                        break;
                    }
                    case "Down from sky 1": {
                        double modY = (256.0 - chunkY) / animationDuration * timeDif;
                        GlStateManager.translate(0.0, 256.0 - chunkY - modY, 0.0);
                        break;
                    }
                    case "From sides": {
                        EnumFacing chunkFacing2 = animationData.chunkFacing;
                        if (chunkFacing2 != null) {
                            Vec3i vec = chunkFacing2.getDirectionVec();
                            double mod = -(200.0 - 200.0 / animationDuration * timeDif);
                            GlStateManager.translate(vec.getX() * mod, 0.0, vec.getZ() * mod);
                            break;
                        }
                        break;
                    }
                }
            } else {
                timeStamps.remove(renderChunk);
            }
        }
    }

    @NotNull
    private EnumFacing getFacing(Vec3i dif, int difX, int difZ) {
        return difX > difZ ? dif.getX() > 0 ? EnumFacing.EAST : EnumFacing.WEST : dif.getZ() > 0 ? EnumFacing.SOUTH : EnumFacing.NORTH;
    }

    private static class AnimationData {
        long timeStamp;
        EnumFacing chunkFacing;

        AnimationData(long timeStamp, EnumFacing chunkFacing) {
            this.timeStamp = timeStamp;
            this.chunkFacing = chunkFacing;
        }
    }
}
