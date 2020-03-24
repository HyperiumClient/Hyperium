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

package cc.hyperium.purchases;

import cc.hyperium.config.Settings;
import cc.hyperium.utils.JsonHolder;

public class PurchaseSettings {

  private final boolean buttDisabled;
  private final boolean wingsDisabled;
  private final String wingsType;
  private final double wingsScale;
  private final boolean wingsColor;
  private final int wingsRed;
  private final int wingsGreen;
  private final int wingsBlue;
  private final boolean dragonHeadDisabled;
  private final EnumPurchaseType currentHatType;
  private final EnumPurchaseType companion;

  public PurchaseSettings(JsonHolder source) {
    buttDisabled = source.optJSONObject("butt").optBoolean("disabled");
    wingsDisabled = source.optJSONObject("wings").optBoolean("disabled");
    wingsType = source.optJSONObject("wings").optString("type");
    wingsScale = source.optJSONObject("wings").optDouble("scale", Settings.WINGS_SCALE);
    wingsColor = source.optJSONObject("wings").optBoolean("wings_color", true);
    wingsRed = source.optJSONObject("wings").optInt("color_red", Settings.WINGS_RED);
    wingsGreen = source.optJSONObject("wings").optInt("color_green", Settings.WINGS_GREEN);
    wingsBlue = source.optJSONObject("wings").optInt("color_blue", Settings.WINGS_BLUE);
    dragonHeadDisabled = source.optJSONObject("dragon").optBoolean("disabled");
    currentHatType = EnumPurchaseType.parse(source.optJSONObject("hat").optString("current_type"));
    companion = EnumPurchaseType.parse(source.optJSONObject("companion").optString("type"));
  }

  public boolean isDragonHeadDisabled() {
    return dragonHeadDisabled;
  }

  public double getWingsScale() {
    return wingsScale;
  }

  public String getWingsType() {
    return wingsType;
  }

  public boolean isWingsDisabled() {
    return wingsDisabled;
  }

  public boolean isWingsColor() {
    return wingsColor;
  }

  public int getWingsRed() {
    return wingsRed;
  }

  public int getWingsGreen() {
    return wingsGreen;
  }

  public int getWingsBlue() {
    return wingsBlue;
  }

  public boolean isButtDisabled() {
    return buttDisabled;
  }

  public EnumPurchaseType getCurrentHatType() {
    return currentHatType;
  }

  public EnumPurchaseType getCurrentCompanion() {
    return companion;
  }
}
