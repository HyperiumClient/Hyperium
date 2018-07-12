package cc.hyperium.mixinsimp.gui;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.handlers.handlers.ApiDataHandler;
import cc.hyperium.mixins.gui.IMixinGui;
import cc.hyperium.mixins.gui.IMixinGuiPlayerTabOverlay;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.StaffUtils;
import com.google.common.collect.Ordering;
import com.mojang.authlib.GameProfile;
import java.awt.Color;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
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

public class HyperiumGuiPlayerTabOverlay {
    private GuiPlayerTabOverlay parent;

    public HyperiumGuiPlayerTabOverlay(GuiPlayerTabOverlay parent) {
        this.parent = parent;
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

    public void drawPing(int p_175245_1_, int p_175245_2_, int yIn, NetworkPlayerInfo networkPlayerInfoIn, float zLevel, Minecraft mc) {
        final int ping = networkPlayerInfoIn.getResponseTime();
        final int x = p_175245_2_ + p_175245_1_ - (mc.fontRendererObj.getStringWidth(ping + "") >> 1) - 2;
        final int y = yIn + (mc.fontRendererObj.FONT_HEIGHT >> 2);
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
                mc.fontRendererObj.drawString("   " + ping + "", (2 * x), (2 * y), colour);
                GlStateManager.scale(2.0f, 2.0f, 2.0f);
                GlStateManager.popMatrix();

            }
            return;
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(parent.icons);
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

        ((IMixinGui) parent).setZLevel(zLevel + 100.0F);
        parent.drawTexturedModalRect(p_175245_2_ + p_175245_1_ - 5, yIn, i * 10, 176 + j * 8, 10, 8);
        ((IMixinGui) parent).setZLevel(zLevel - 100.0F);
    }

    public void renderPlayerlist(int width, Scoreboard scoreboardIn, ScoreObjective scoreObjectiveIn, Ordering<NetworkPlayerInfo> field_175252_a, IChatComponent header, IChatComponent footer, Minecraft mc) {
        NetHandlerPlayClient nethandlerplayclient = mc.thePlayer.sendQueue;
        List<NetworkPlayerInfo> list = field_175252_a.<NetworkPlayerInfo>sortedCopy(nethandlerplayclient.getPlayerInfoMap());
        int i = 0;
        int j = 0;

        for (NetworkPlayerInfo networkplayerinfo : list) {
            int k = mc.fontRendererObj.getStringWidth(parent.getPlayerName(networkplayerinfo));
            i = Math.max(i, k);

            if (scoreObjectiveIn != null && scoreObjectiveIn.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
                k = mc.fontRendererObj.getStringWidth(" " + scoreboardIn.getValueFromObjective(networkplayerinfo.getGameProfile().getName(), scoreObjectiveIn).getScorePoints());
                j = Math.max(j, k);
            }
        }

        list = list.subList(0, Math.min(list.size(), 80));

        if (Settings.FRIENDS_FIRST_IN_TAB) {
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

        boolean flag = mc.isIntegratedServerRunning() || mc.getNetHandler().getNetworkManager().getIsencrypted();
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

        if (header != null) {
            list1 = mc.fontRendererObj.listFormattedStringToWidth(header.getFormattedText(), width - 50);

            for (String s : list1) {
                l1 = Math.max(l1, mc.fontRendererObj.getStringWidth(s));
            }
        }

        if (footer != null) {
            list2 = mc.fontRendererObj.listFormattedStringToWidth(footer.getFormattedText(), width - 50);

            for (String s2 : list2) {
                l1 = Math.max(l1, mc.fontRendererObj.getStringWidth(s2));
            }
        }

        if (list1 != null) {
            parent.drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + list1.size() * mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

            for (String s3 : list1) {
                int i2 = mc.fontRendererObj.getStringWidth(s3);
                mc.fontRendererObj.drawStringWithShadow(s3, (float) (width / 2 - i2 / 2), (float) k1, -1);
                k1 += mc.fontRendererObj.FONT_HEIGHT;
            }

            ++k1;
        }

        parent.drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + i4 * 9, Integer.MIN_VALUE);

        for (int k4 = 0; k4 < l3; ++k4) {
            int l4 = k4 / i4;
            int i5 = k4 % i4;
            int j2 = j1 + l4 * i1 + l4 * 5;
            int k2 = k1 + i5 * 9;
            parent.drawRect(j2, k2, j2 + i1 + 6, k2 + 8, 553648127);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

            if (k4 < list.size()) {
                NetworkPlayerInfo networkplayerinfo1 = (NetworkPlayerInfo) list.get(k4);
                String s1 = parent.getPlayerName(networkplayerinfo1);
                GameProfile gameprofile = networkplayerinfo1.getGameProfile();

                if (flag) {
                    EntityPlayer entityplayer = mc.theWorld.getPlayerEntityByUUID(gameprofile.getId());
                    boolean flag1 = entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.CAPE) && (gameprofile.getName().equals("Dinnerbone") || gameprofile.getName().equals("Grumm"));
                    mc.getTextureManager().bindTexture(networkplayerinfo1.getLocationSkin());
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

                int renderX = j2 + mc.fontRendererObj.getStringWidth(s1) + 2;

                if (Settings.SHOW_ONLINE_PLAYERS) {
                    String s = "⚫";
                    boolean online = Hyperium.INSTANCE.getHandlers().getStatusHandler().isOnline(gameprofile.getId());
                    if (StaffUtils.isStaff(gameprofile.getId())) {
                        StaffUtils.DotColour colour = StaffUtils.getColor(gameprofile.getId());
                        if (colour.isChroma) {
                            drawChromaWaveString(s, renderX, (k2 - 2));
                        } else {
                            String format = StaffUtils.getColor(gameprofile.getId()).baseColour + s;
                            mc.fontRendererObj.drawString(format, renderX, (k2 - 2), Color.WHITE.getRGB());
                        }
                    } else {
                        String format = online ? ChatColor.GREEN + s : ChatColor.RED + s;
                        mc.fontRendererObj.drawString(format, renderX, (k2 - 2), Color.WHITE.getRGB());
                    }
                }

                if (networkplayerinfo1.getGameType() == WorldSettings.GameType.SPECTATOR) {
                    s1 = EnumChatFormatting.ITALIC + s1;
                    mc.fontRendererObj.drawStringWithShadow(s1, (float) j2, (float) k2, -1862270977);
                } else {
                    mc.fontRendererObj.drawStringWithShadow(s1, (float) j2, (float) k2, -1);
                }

                if (scoreObjectiveIn != null && networkplayerinfo1.getGameType() != WorldSettings.GameType.SPECTATOR) {
                    int k5 = j2 + i + 1;
                    int l5 = k5 + l;

                    if (l5 - k5 > 5) {
                        ((IMixinGuiPlayerTabOverlay)parent).callDrawScoreboardValues(scoreObjectiveIn, k2, gameprofile.getName(), k5, l5, networkplayerinfo1);
                    }
                }

                ((IMixinGuiPlayerTabOverlay)parent).callDrawPing(i1, j2 - (flag ? 9 : 0), k2, networkplayerinfo1);
            }
        }

        if (list2 != null) {
            k1 = k1 + i4 * 9 + 1;
            parent.drawRect(width / 2 - l1 / 2 - 1, k1 - 1, width / 2 + l1 / 2 + 1, k1 + list2.size() * mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

            for (String s4 : list2) {
                int j5 = mc.fontRendererObj.getStringWidth(s4);
                mc.fontRendererObj.drawStringWithShadow(s4, (float) (width / 2 - j5 / 2), (float) k1, -1);
                k1 += mc.fontRendererObj.FONT_HEIGHT;
            }
        }
    }
}
