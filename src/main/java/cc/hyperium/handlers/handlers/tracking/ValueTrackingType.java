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

package cc.hyperium.handlers.handlers.tracking;

public enum ValueTrackingType {

    COINS("Coins", StatisticViewingGui.CompressionType.SUM, StatisticViewingGui.MissingDataHandling.ZERO),
    EXPERIENCE("Experience", StatisticViewingGui.CompressionType.SUM, StatisticViewingGui.MissingDataHandling.ZERO),
    ERROR("Error", StatisticViewingGui.CompressionType.SUM, StatisticViewingGui.MissingDataHandling.ZERO);

    private String name;
    private StatisticViewingGui.CompressionType compressionType;
    private StatisticViewingGui.MissingDataHandling missingDataHandling;

    ValueTrackingType(String name, StatisticViewingGui.CompressionType compressionType, StatisticViewingGui.MissingDataHandling missingDataHandling) {
        this.name = name;
        this.compressionType = compressionType;
        this.missingDataHandling = missingDataHandling;
    }

    public static ValueTrackingType parse(String in) {
        try {
            valueOf(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ERROR;
    }

    public StatisticViewingGui.MissingDataHandling getMissingDataHandling() {
        return missingDataHandling;
    }

    public String getDisplay() {
        return name;
    }

    public StatisticViewingGui.CompressionType getCompressionType() {
        return compressionType;
    }
}
