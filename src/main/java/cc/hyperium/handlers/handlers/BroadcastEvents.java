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

package cc.hyperium.handlers.handlers;

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.event.AchievementGetEvent;
import cc.hyperium.event.EventBus;
import cc.hyperium.event.InvokeEvent;
import cc.hyperium.event.LevelupEvent;
import cc.hyperium.event.ServerChatEvent;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BroadcastEvents {


    private final Pattern LEVEL_UP_REGEX = Pattern.compile("You are now Hypixel Level (?<level>\\d*)!");
    private final Pattern ACHIEVEMENT_PATTERN = Pattern.compile("a>> {3}Achievement Unlocked: (?<achievement>.+) {3}<<a");

    @InvokeEvent
    public void levelUp(LevelupEvent event) {
        if (Settings.BROADCAST_LEVELUPS) {
            Hyperium.INSTANCE.getHandlers().getCommandQueue().queue("/gchat Levelup! I am now Hypixel Level: " + event.getLevel());
        }
    }

    @InvokeEvent
    public void acheivementGet(AchievementGetEvent event) {
        if (Settings.BROADCAST_ACHIEVEMENTS) {
            Hyperium.INSTANCE.getHandlers().getCommandQueue().queue("/gchat Achievement unlocked! I unlocked the " + event.getAchievement() + " achievement");
        }
    }

    //Raw: "a>>   Achievement Unlocked: Rambo   <<a"
    @InvokeEvent
    public void checkForEvents(ServerChatEvent event) {
        String raw = EnumChatFormatting.getTextWithoutFormattingCodes(event.getChat().getUnformattedText());
        Matcher levelMatcher = LEVEL_UP_REGEX.matcher(raw);
        if (levelMatcher.matches()) {
            String level = levelMatcher.group("level");
            if (StringUtils.isNumeric(level)) {
                EventBus.INSTANCE.post(new LevelupEvent(Integer.parseInt(level)));
            }
        }

        Matcher achMatcher = ACHIEVEMENT_PATTERN.matcher(raw);
        if (achMatcher.matches()) {
            String ach = achMatcher.group("achievement");
            //Check to stop spamming of gchat if achievement is broken and you get it many times.
            if (!achievementsGotten.contains(ach)) {
                EventBus.INSTANCE.post(new AchievementGetEvent(ach));
                achievementsGotten.add(ach);
            }
        }

    }

    private List<String> achievementsGotten = new ArrayList<>();
}
