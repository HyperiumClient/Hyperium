package com.hcc.mods.chromahud.displayitems.hcc;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.hcc.mods.chromahud.ElementRenderer;
import com.hcc.mods.chromahud.api.Dimension;
import com.hcc.mods.chromahud.api.DisplayItem;
import com.hcc.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreboardDisplay extends DisplayItem {

    public static ScoreObjective p_180475_1_;
    public static ScaledResolution p_180475_2_;

    public ScoreboardDisplay(JsonHolder raw, int ordinal) {
        super(raw, ordinal);
    }


    @Override
    public Dimension draw(int starX, double startY, boolean config) {
        if(p_180475_1_ != null) {
            Scoreboard scoreboard = p_180475_1_.getScoreboard();
            Collection<Score> collection = scoreboard.getSortedScores(p_180475_1_);
            List<Score> list = Lists.newArrayList(collection.stream().filter(p_apply_1_ -> p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#")).collect(Collectors.toList()));

            if (list.size() > 15) {
                collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
            } else {
                collection = list;
            }

            int i = Minecraft.getMinecraft().fontRendererObj.getStringWidth(p_180475_1_.getDisplayName());

            for (Score score : collection) {
                ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
                String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
                i = Math.max(i, Minecraft.getMinecraft().fontRendererObj.getStringWidth(s));
            }

            int i1 = collection.size() * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
            int k1 = 3;
            int l1 = starX;
            if (ElementRenderer.getCurrent().isRightSided())
                l1 -= i;
            int j = 0;

            int k = 0;
            for (Score score1 : collection) {
                ++j;
                ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
                String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
                String s2 = EnumChatFormatting.RED + "" + score1.getScorePoints();
                k = ((int) startY) + (collection.size() - j + 1) * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
                int l = p_180475_2_.getScaledWidth() - k1 + 2;
                if (ElementRenderer.getCurrent().isHighlighted())
                    Gui.drawRect(l1 - 2, k, l1 + i, k + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT, 1342177280);
                Minecraft.getMinecraft().fontRendererObj.drawString(s1, l1, k, 553648127);
                // todo: make toggle number
                if (data.optBoolean("numbers")) {
                    Minecraft.getMinecraft().fontRendererObj.drawString(s2, l1 + i - Minecraft.getMinecraft().fontRendererObj.getStringWidth(s2), k, 553648127);
                }

                if (j == collection.size()) {
                    String s3 = p_180475_1_.getDisplayName();
                    if (ElementRenderer.getCurrent().isHighlighted()) {
                        Gui.drawRect(l1 - 2, k - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT - 1, l1 + i, k - 1, 1610612736);
                        Gui.drawRect(l1 - 2, k - 1, l1 + i, k, 1342177280);
                    }
                    Minecraft.getMinecraft().fontRendererObj.drawString(s3, l1 + i / 2 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(s3) / 2, k - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT, 553648127);
                }
            }

            return new Dimension(i - (data.optBoolean("numbers") ? 0 : 10), i1 + 10);
        }
        return new Dimension(10, 10);
    }


}
