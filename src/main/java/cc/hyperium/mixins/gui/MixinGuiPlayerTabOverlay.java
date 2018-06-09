/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.hyperium.mixins.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.handlers.handlers.ApiDataHandler;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.StaffUtils;
import com.google.common.collect.Ordering;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.awt.*;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

@Mixin(GuiPlayerTabOverlay.class)
public abstract class MixinGuiPlayerTabOverlay extends Gui {

    @Shadow
    @Final
    private static Ordering<NetworkPlayerInfo> field_175252_a;
    @Shadow
    @Final
    private Minecraft mc;
    @Shadow
    private IChatComponent header;
    @Shadow
    private IChatComponent footer;

    @Shadow
    public abstract String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn);

    @Shadow
    protected abstract void drawScoreboardValues(ScoreObjective p_175247_1_, int p_175247_2_, String p_175247_3_, int p_175247_4_, int p_175247_5_, NetworkPlayerInfo p_175247_6_);

    /**
     * Allows the user to use numbered ping if they wish
     *
     * @author boomboompower
     */
    @Overwrite
    protected void drawPing(int p_175245_1_, int p_175245_2_, int yIn, NetworkPlayerInfo networkPlayerInfoIn) {
        final int ping = networkPlayerInfoIn.getResponseTime();
        final int x = p_175245_2_ + p_175245_1_ - (this.mc.fontRendererObj.getStringWidth(ping + "") >> 1) - 2;
        final int y = yIn + (this.mc.fontRendererObj.FONT_HEIGHT >> 2);
        if (Settings.NUMBER_PING) {
            int colour;

            if (ping > 500) {
                colour = 11141120;
            } else if (ping > 300) {
                colour = 11184640;
            } else if (ping > 200) {
                colour = 11193344;
            } else if (ping > 135) {
                colour = 2128640;
            } else if (ping > 70) {
                colour = 39168;
            } else if (ping >= 0) {
                colour = 47872;
            } else {
                colour = 11141120;
            }


            if (ping >= 0 && ping < 10000) {
                GlStateManager.pushMatrix();
                GlStateManager.scale(0.5f, 0.5f, 0.5f);
                this.mc.fontRendererObj.drawString(ping + "", (2 * x), (2 * y), colour);
                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                GlStateManager.popMatrix();

            }
            return;
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(icons);
        int i = 0;
        int j = 0;

        if (ping < 0) {
            j = 5;
        } else if (ping < 150) {
            j = 0;
        } else if (ping < 300) {
            j = 1;
        } else if (ping < 600) {
            j = 2;
        } else if (ping < 1000) {
            j = 3;
        } else {
            j = 4;
        }

        this.zLevel += 100.0F;
        drawTexturedModalRect(p_175245_2_ + p_175245_1_ - 11, yIn, i * 10, 176 + j * 8, 10, 8);

        this.zLevel -= 100.0F;
    }

    /**
     * @author Sk1er
     */
    @Overwrite
    public void renderPlayerlist(int width, Scoreboard scoreboardIn, ScoreObjective scoreObjectiveIn) {
        NetHandlerPlayClient nethandlerplayclient = this.mc.thePlayer.sendQueue;
        List<NetworkPlayerInfo> list = field_175252_a.<NetworkPlayerInfo>sortedCopy(nethandlerplayclient.getPlayerInfoMap());
        int i = 0;
        int j = 0;

        for (NetworkPlayerInfo networkplayerinfo : list) {
            int k = this.mc.fontRendererObj.getStringWidth(this.getPlayerName(networkplayerinfo));
            i = Math.max(i, k);

            if (scoreObjectiveIn != null && scoreObjectiveIn.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
                k = this.mc.fontRendererObj.getStringWidth(" " + scoreboardIn.getValueFromObjective(networkplayerinfo.getGameProfile().getName(), scoreObjectiveIn).getScorePoints());
                j = Math.max(j, k);
            }
        }

        list = list.subList(0, Math.min(list.size(), 80));

        if (Hyperium.INSTANCE.getHandlers().getConfigOptions().friendsFirstIntag) {
            ConcurrentLinkedDeque<NetworkPlayerInfo> friends = new ConcurrentLinkedDeque<>();
            ApiDataHandler dataHandler = Hyperium.INSTANCE.getHandlers().getDataHandler();
            List<UUID> friendUUIDList = dataHandler.getFriendUUIDList();
            for (NetworkPlayerInfo networkPlayerInfo : list) {
                UUID id = networkPlayerInfo.getGameProfile().getId();
                if (friendUUIDList.contains(id)) {
                    friends.add(networkPlayerInfo);
                }
            }
            list.removeAll(friends);
            friends.addAll(list);
            list.clear();
            list.addAll(friends);
        }

        int l3 = list.size();
        int i4 = l3;
        int j4;

        for (j4 = 1; i4 > 20; i4 = (l3 + j4 - 1) / j4) {
            ++j4;
        }

        boolean flag = this.mc.isIntegratedServerRunning() || this.mc.getNetHandler().getNetworkManager().getIsencrypted();
        int l;

        if (scoreObjectiveIn != null) {
            if (scoreObjectiveIn.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
                l = 90;
            } else {
                l = j;
            }
        } else {
            l = 0;
        }

        int i1 = Math.min(j4 * ((flag ? 9 : 0) + i + l + 13), width - 50) / j4;
        int j1 = width / 2 - (i1 * j4 + (j4 - 1) * 5) / 2;
        int k1 = 10;
        int l1 = i1 * j4 + (j4 - 1) * 5;
        List<String> list1 = null;
        List<String> list2 = null;

        if (this.header != null) {
            list1 = this.mc.fontRendererObj.listFormattedStringToWidth(this.header.getFormattedText(), width - 50);

            for (String s : list1) {
                l1 = Math.max(l1, this.mc.fontRendererObj.getStringWidth(s));
            }
        }

        if (this.footer != null) {
            list2 = this.mc.fontRendererObj.listFormattedStringToWidth(this.footer.getFormattedText(), width - 50);

            for (String s2 : list2) {
                l1 = Math.max(l1, this.mc.fontRendererObj.getStringWidth(s2));
            }
        }

        if (list1 != null) {
            drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + list1.size() * this.mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

            for (String s3 : list1) {
                int i2 = this.mc.fontRendererObj.getStringWidth(s3);
                this.mc.fontRendererObj.drawStringWithShadow(s3, (float) (width / 2 - i2 / 2), (float) k1, -1);
                k1 += this.mc.fontRendererObj.FONT_HEIGHT;
            }

            ++k1;
        }

        drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + i4 * 9, Integer.MIN_VALUE);

        for (int k4 = 0; k4 < l3; ++k4) {
            int l4 = k4 / i4;
            int i5 = k4 % i4;
            int j2 = j1 + l4 * i1 + l4 * 5;
            int k2 = k1 + i5 * 9;
            drawRect(j2, k2, j2 + i1, k2 + 8, 553648127);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

            if (k4 < list.size()) {
                NetworkPlayerInfo networkplayerinfo1 = (NetworkPlayerInfo) list.get(k4);
                String s1 = this.getPlayerName(networkplayerinfo1);
                GameProfile gameprofile = networkplayerinfo1.getGameProfile();

                if (flag) {
                    EntityPlayer entityplayer = this.mc.theWorld.getPlayerEntityByUUID(gameprofile.getId());
                    boolean flag1 = entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.CAPE) && (gameprofile.getName().equals("Dinnerbone") || gameprofile.getName().equals("Grumm"));
                    this.mc.getTextureManager().bindTexture(networkplayerinfo1.getLocationSkin());
                    int l2 = 8 + (flag1 ? 8 : 0);
                    int i3 = 8 * (flag1 ? -1 : 1);
                    Gui.drawScaledCustomSizeModalRect(j2, k2, 8.0F, (float) l2, 8, i3, 8, 8, 64.0F, 64.0F);

                    if (entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.HAT)) {
                        int j3 = 8 + (flag1 ? 8 : 0);
                        int k3 = 8 * (flag1 ? -1 : 1);
                        Gui.drawScaledCustomSizeModalRect(j2, k2, 40.0F, (float) j3, 8, k3, 8, 8, 64.0F, 64.0F);
                    }

                    j2 += 9;
                }

                int renderX = j2 + this.mc.fontRendererObj.getStringWidth(s1) + 2;

                if (Hyperium.INSTANCE.getHandlers().getConfigOptions().showOnlinePlayers) {
                    String s = "⚫";
                    boolean online = Hyperium.INSTANCE.getHandlers().getStatusHandler().isOnline(gameprofile.getId());
                    if (StaffUtils.isStaff(gameprofile.getId())) {
                        StaffUtils.DotColour colour = StaffUtils.getColor(gameprofile.getId());
                        if (colour.isChroma) {
                            drawChromaWaveString(s, renderX, (k2 - 2));
                        } else {
                            String format = StaffUtils.getColor(gameprofile.getId()) + s;
                            this.mc.fontRendererObj.drawString(format, renderX, (k2-2), Color.WHITE.getRGB());
                        }
                    } else {
                        String format = online ? ChatColor.GREEN + s : ChatColor.RED + s;
                        this.mc.fontRendererObj.drawString(format, renderX, (k2-2), Color.WHITE.getRGB());
                    }
                }

                if (networkplayerinfo1.getGameType() == WorldSettings.GameType.SPECTATOR) {
                    s1 = EnumChatFormatting.ITALIC + s1;
                    this.mc.fontRendererObj.drawStringWithShadow(s1, (float) j2, (float) k2, -1862270977);
                } else {
                    this.mc.fontRendererObj.drawStringWithShadow(s1, (float) j2, (float) k2, -1);
                }

                if (scoreObjectiveIn != null && networkplayerinfo1.getGameType() != WorldSettings.GameType.SPECTATOR) {
                    int k5 = j2 + i + 1;
                    int l5 = k5 + l;

                    if (l5 - k5 > 5) {
                        this.drawScoreboardValues(scoreObjectiveIn, k2, gameprofile.getName(), k5, l5, networkplayerinfo1);
                    }
                }

                this.drawPing(i1, j2 - (flag ? 9 : 0), k2, networkplayerinfo1);
            }
        }

        if (list2 != null) {
            k1 = k1 + i4 * 9 + 1;
            drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + list2.size() * this.mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

            for (String s4 : list2) {
                int j5 = this.mc.fontRendererObj.getStringWidth(s4);
                this.mc.fontRendererObj.drawStringWithShadow(s4, (float) (width / 2 - j5 / 2), (float) k1, -1);
                k1 += this.mc.fontRendererObj.FONT_HEIGHT;
            }
        }
    }
    private static void drawChromaWaveString(String text, int xIn, int y) {
        FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
        int x = xIn;
        for (char c : text.toCharArray()) {
            long dif = (x * 10) - (y * 10);
            long l = System.currentTimeMillis() - dif;
            float ff = 2000.0F;
            int i = Color.HSBtoRGB((float) (l % (int) ff) / ff, 0.8F, 0.8F);
            String tmp = String.valueOf(c);
            renderer.drawString(tmp, (float) ((double) x), (float) ((double) y), i, false);
            x += (double) renderer.getCharWidth(c);
        }
    }
}
