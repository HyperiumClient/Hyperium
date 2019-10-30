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

package cc.hyperium.handlers.handlers.stats;

import cc.hyperium.C;
import cc.hyperium.gui.HyperiumGui;
import cc.hyperium.mods.sk1ercommon.ResolutionUtil;
import club.sk1er.website.api.requests.HypixelApiGuild;
import club.sk1er.website.api.requests.HypixelApiPlayer;
import club.sk1er.website.utils.WebsiteUtils;
import net.hypixel.api.GameType;
import net.minecraft.client.gui.ScaledResolution;

import java.util.Date;
import java.util.List;

public class GuildStatsGui extends HyperiumGui {
    private HypixelApiGuild guild;

    GuildStatsGui(HypixelApiGuild guild) {
        this.guild = guild;
    }

    @Override
    protected void pack() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution current = ResolutionUtil.current();
        drawScaledText(guild.getName() + " " + guild.getFormatedTag(), current.getScaledWidth() / 2, 30, 3,
            -1, true, true);

        int columnX = current.getScaledWidth() / 50;
        int offsetLeftColumn = offset + 50;
        fontRendererObj.drawString(boldText("Founded: ", HypixelApiPlayer.DMY.format(new Date(guild.getCreateDate()))), columnX,
            100 - offsetLeftColumn, -1, true);
        fontRendererObj.drawString(boldText("Members: ", WebsiteUtils.comma(guild.getMembers().size())), columnX,
            110 - offsetLeftColumn, -1, true);
        fontRendererObj.drawString(boldText("Guild Coins: ", WebsiteUtils.comma(guild.getTotalGuildCoins())), columnX,
            120 - offsetLeftColumn, -1, true);
        fontRendererObj.drawString(boldText("Legacy Ranking: ", WebsiteUtils.comma(guild.getLegacyRanking())), columnX,
            130 - offsetLeftColumn, -1, true);
        List<String> strings = fontRendererObj.listFormattedStringToWidth(boldText("Description: ", guild.getDescription()), current.getScaledWidth() / 4);
        int i = 0;

        for (String string : strings) {
            fontRendererObj.drawString(string, columnX, 140 + i - offsetLeftColumn, -1, true);
            i += 10;
        }

        fontRendererObj.drawString(boldText("Guild Level: ", Double.toString(guild.getLevel())), columnX,
            150 + i - offsetLeftColumn, -1, true);
        fontRendererObj.drawString(boldText("Total Wins: ", Integer.toString(guild.getWins("total"))), columnX,
            170 + i - offsetLeftColumn, -1, true);

        for (GameType gameType : GameType.values()) {
            if (gameType == GameType.UNKNOWN || gameType == GameType.LEGACY) continue;
            fontRendererObj.drawString(boldText(gameType.getName() + " Wins: ", Integer.toString(guild.getWins(gameType.name()))), columnX,
                180 + i - offsetLeftColumn, -1, true);
            i += 10;
        }

        List<HypixelApiGuild.GuildPlayer> inOrder = guild.getInOrder();
        int memberX = 2 * current.getScaledWidth() / 6;
        int memberStart = 50 - offset;

        int memberWidth = current.getScaledWidth() - memberX - 40;
        fontRendererObj.drawString(C.BOLD + "Name", memberX, memberStart, -1, true);
        fontRendererObj.drawString(C.BOLD + "Rank", memberX + memberWidth / 3F, memberStart, -1, true);
        fontRendererObj.drawString(C.BOLD + "Join date", memberX + memberWidth / 3F * 2, memberStart, -1, true);
        memberStart += 10;

        for (HypixelApiGuild.GuildPlayer guildPlayer : inOrder) {
            String display = guildPlayer.getDisplay();
            fontRendererObj.drawString(display, memberX, memberStart, -1, true);
            String rank = guildPlayer.getRank();
            if (rank.equals("GUILDMASTER")) rank = "Guild Master";
            if (rank.equals("OFFICER")) rank = "Officer";
            if (rank.equals("MEMBER")) rank = "Member";
            fontRendererObj.drawString(rank, memberX + memberWidth / 3F, memberStart, -1, true);
            fontRendererObj.drawString(HypixelApiPlayer.DMYHHMMSS.format(guildPlayer.getJoinLong()), memberX + memberWidth / 3F * 2, memberStart, -1, true);
            memberStart += 10;
        }
    }
}
