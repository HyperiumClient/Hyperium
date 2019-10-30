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

package cc.hyperium.mods.togglechat.toggles.defaults;


import cc.hyperium.mods.togglechat.toggles.ToggleBase;

import java.util.LinkedList;
import java.util.regex.Pattern;

public class TypeAds extends ToggleBase {

    private final Pattern networkBoosterPattern = Pattern.compile("\nBuying a (?<game>.*) Network Booster activates (?<coinboost>.*) for (?<count>.*) players & supports the server!\nClick to browse Network Boosters! (?<thing>.) (?<site>.*)\n");
    private final Pattern mysteryPattern = Pattern.compile("\nMystery Boxes contain tons of awesome collectibles! Unlock Housing items, find legendary Pets and more!\nClick to browse Mystery Boxes! (?<symbol>.) (?<site>.*)\n");
    private final Pattern mediaPattern1 = Pattern.compile("\nSee all the posts shared by Hypixel on (?<name>.*)!\nLike the Hypixel page! (?<special>.) (?<link>.*)\n");
    private final Pattern mediaPattern2 = Pattern.compile("\nKeep up with the latest from Hypixel on (?<name>.*)!\nFollow @HypixelNetwork! (?<special>.) (?<link>.+)\n");
    private final Pattern mediaPattern3 = Pattern.compile("\nBe the first to watch Hypixel (?<media>.+) videos!\nSubscribe to Hypixel! (?<special>.) (?<link>.+)\n");
    private final Pattern mediaPattern4 = Pattern.compile("\nGet deals and news sent to your email!\nSignup for the Newsletter! (?<special>.) (?<link>.+)\n");

    private boolean enabled = true;

    @Override
    public String getName() {
        return "Ads";
    }

    @Override
    public String getDisplayName() {
        return "Ads: %s";
    }

    @Override
    public boolean shouldToggle(String message) {
        return networkBoosterPattern.matcher(message).find()
            || mysteryPattern.matcher(message).find()
            || mediaPattern1.matcher(message).matches()
            || mediaPattern2.matcher(message).matches()
            || mediaPattern3.matcher(message).matches()
            || mediaPattern4.matcher(message).matches();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public LinkedList<String> getDescription() {
        return asLinked(
            "Toggles all server chat",
            "advertisements such as",
            "things prompting the",
            "store page",
            "",
            "This cleans up the chat",
            "whilst you are afk",
            "so you don\'t miss",
            "important messages");
    }
}
