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

package cc.hyperium.mods.levelhead.display;

import cc.hyperium.mods.levelhead.Levelhead;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.Team;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class AboveHeadDisplay extends LevelheadDisplay {

    private boolean bottomValue = true;
    private int index;

    public AboveHeadDisplay(DisplayConfig config) {
        super(DisplayPosition.ABOVE_HEAD, config);
    }

    @Override
    public void tick() {
        Minecraft.getMinecraft().theWorld.playerEntities.forEach(player -> {
            if (!existedMoreThan5Seconds.contains(player.getUniqueID())) {
                if (!timeCheck.containsKey(player.getUniqueID())) timeCheck.put(player.getUniqueID(), 0);

                int old = timeCheck.get(player.getUniqueID());
                if (old > 100) {
                    if (!existedMoreThan5Seconds.contains(player.getUniqueID())) {
                        existedMoreThan5Seconds.add(player.getUniqueID());
                    }
                } else if (!player.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer)) {
                    timeCheck.put(player.getUniqueID(), old + 1);
                }
            }

            if (loadOrRender(player)) {
                UUID uuid = player.getUniqueID();
                if (!cache.containsKey(uuid)) Levelhead.getInstance().fetch(uuid, this, bottomValue);
            }
        });
    }

    @Override
    public void checkCacheSize() {
        int max = Math.max(150, Levelhead.getInstance().getDisplayManager().getMasterConfig().getPurgeSize());
        if (cache.size() > max) {
            ArrayList<UUID> safePlayers = Minecraft.getMinecraft().theWorld.playerEntities.stream().filter(player ->
                existedMoreThan5Seconds.contains(player.getUniqueID())).
                map(Entity::getUniqueID).collect(Collectors.toCollection(ArrayList::new));

            existedMoreThan5Seconds.clear();
            existedMoreThan5Seconds.addAll(safePlayers);

            cache.keySet().stream().filter(uuid -> !safePlayers.contains(uuid)).forEach(uuid -> {
                cache.remove(uuid);
                trueValueCache.remove(uuid);
            });
        }
    }

    @Override
    public void onDelete() {
        cache.clear();
        trueValueCache.clear();
        existedMoreThan5Seconds.clear();
    }

    @Override
    public boolean loadOrRender(EntityPlayer player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getPotionID() == 14) return false;
        }

        if (!renderFromTeam(player) || player.riddenByEntity != null) {
            return false;
        }

        int renderDistance = Levelhead.getInstance().getDisplayManager().getMasterConfig().getRenderDistance();
        int min = Math.min(64 * 64, renderDistance * renderDistance);
        return !(player.getDistanceSqToEntity(Minecraft.getMinecraft().thePlayer) > min) &&
            (!player.hasCustomName() || !player.getCustomNameTag().isEmpty()) &&
            !player.getDisplayName().toString().isEmpty() &&
            existedMoreThan5Seconds.contains(player.getUniqueID()) &&
            !player.getDisplayName().getFormattedText().contains(ChatColor.COLOR_CHAR + "k") &&
            !player.isInvisible() &&
            !player.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer) &&
            !player.isSneaking();
    }

    private boolean renderFromTeam(EntityPlayer player) {
        Team team = player.getTeam();
        Team team1 = Minecraft.getMinecraft().thePlayer.getTeam();

        if (team != null) {
            Team.EnumVisible visibility = team.getNameTagVisibility();
            switch (visibility) {
                case NEVER:
                    return false;

                case HIDE_FOR_OTHER_TEAMS:
                    return team1 == null || team.isSameTeam(team1);

                case HIDE_FOR_OWN_TEAM:
                    return team1 == null || !team.isSameTeam(team1);

                case ALWAYS:
                default:
                    return true;
            }
        }

        return true;
    }

    void setBottomValue(boolean bottomValue) {
        this.bottomValue = bottomValue;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
