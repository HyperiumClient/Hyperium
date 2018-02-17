/*
 *     Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  HCC Dev Team
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.hcc.ac;

import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

import java.util.UUID;

public class User {
    private UUID player;
    private int aps = 0;
    private Vec3 lookVec = null;
    private Vec3 posVec = null;
    private BlockPos pos = null;

    User(UUID player) {
        this.player = player;
    }

    /**
     * when the player swing to count APS
     */
    public void onSwing() {
        aps++;
    }

    /**
     * resets aps every second because its per sec
     */
    public void resetAPS() {
        aps = 0;
    }

    /**
     * @return get current APS
     */
    public int getAps() {
        return aps;
    }

    /**
     * @return the UUID
     */
    public UUID getPlayer() {
        return player;
    }

    /**
     * @return current look vector
     */
    public Vec3 getLookVec() {
        return lookVec;
    }

    /**
     * set current look vector
     *
     * @param lookVec vector
     */
    public void setLookVec(Vec3 lookVec) {
        this.lookVec = lookVec;
    }

    /**
     * @return current position vector
     */
    public Vec3 getPosVec() {
        return posVec;
    }

    /**
     * set current position vector
     *
     * @param posVec vector
     */
    public void setPosVec(Vec3 posVec) {
        this.posVec = posVec;
    }

    /**
     * @return current block position of player
     */
    public BlockPos getPos() {
        return pos;
    }

    /**
     * set current block position
     *
     * @param pos position
     */
    public void setPos(BlockPos pos) {
        this.pos = pos;
    }
}
