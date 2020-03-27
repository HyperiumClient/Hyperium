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

package cc.hyperium.mods.chromahud.displayitems.chromahud;

import cc.hyperium.mods.chromahud.ElementRenderer;
import cc.hyperium.mods.chromahud.api.DisplayItem;
import cc.hyperium.utils.ChatColor;
import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

import java.text.DecimalFormat;

/**
 * I can try to save you from the speeding ticket with this meter, but I don't feel like it's gonna
 * help you IRL
 *
 * @author TIVJ-dev
 */
public class SpeedMeter extends DisplayItem {
  public static DecimalFormat defaultDecimalFormat = new DecimalFormat("#.##");

  private final Minecraft mc;
  private SpeedUnit speedUnit;
  private DecimalFormat format;
  private Mode mode;

  public SpeedMeter(JsonHolder object, int ordinal) {
    super(object, ordinal);
    this.mode = Mode.valueOf(object.optString("mode", Mode.XYZ.name()));
    this.speedUnit = SpeedUnit.valueOf(object.optString("unit", SpeedUnit.BPS.name()));
    this.format = new DecimalFormat(object.optString("format", "#.##"));
    this.mc = Minecraft.getMinecraft();
    height = 10;
  }

  @Override
  public void save() {
    data.put("mode", this.mode.name());
    data.put("unit", this.speedUnit.name());
    data.put("format", this.getFormat());
  }

  public String getFormat() {
    return this.format.toPattern();
  }

  public void setFormat(String format) {
    try {
      this.format = new DecimalFormat(format);
    } catch (Exception e) {
      this.format = defaultDecimalFormat;
    }
  }

  public void setMode(Mode mode) {
    this.mode = mode == null ? Mode.XYZ : mode;
  }

  public void setSpeedUnit(SpeedUnit speedUnit) {
    this.speedUnit =
            speedUnit == null ? SpeedUnit.BPS : speedUnit; // if unit is null, use first unit in list
  }

  public void cycleMode() {
    this.mode = Mode.get(this.mode.ordinal()+1);
    if (this.mode == null) this.mode = Mode.XYZ;
  }

  public void cycleSpeedUnit() {
    this.speedUnit = SpeedUnit.get(this.speedUnit.ordinal()+1);
    if (this.speedUnit == null) this.speedUnit = SpeedUnit.BPS;
  }

  public void draw(int x, double y, boolean isConfig) {
    if (this.mc.thePlayer == null) return;
    double speed =
            convertSpeedToUnits(
                    Math.abs(getSpeedInBlocksPerTick()),
                    speedUnit); // Math#abs is to make sure the speed isn't negative

    String text =
            "Speed"
                    + (mode.equals(Mode.XYZ) ? "" : " " + mode.name())
                    + ": "
                    + format.format(speed)
                    + " "
                    + speedUnit.unit;

    ElementRenderer.draw(x, y, ChatColor.translateAlternateColorCodes('%', text));
    width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
  }

  private double getSpeedInBlocksPerTick() {
    double x, y, z;
    EntityPlayerSP player = this.mc.thePlayer;
    switch (mode) {
      case X:
        return player.posX - player.prevPosX;

      case Y:
        return player.posY - player.prevPosY;

      case Z:
        return player.posZ - player.prevPosZ;

      case XY:
        x = player.posX - player.prevPosX;
        y = player.posY - player.prevPosY;
        return Math.sqrt(x * x + y * y);

      case XYZ:
        x = player.posX - player.prevPosX;
        y = player.posY - player.prevPosY;
        z = player.posZ - player.prevPosZ;
        return Math.sqrt(x * x + y * y + z * z);
    }
    return 0D;
  }

  public static double convertSpeedToUnits(double originalSpeed, SpeedUnit unit) {
    if (unit.equals(SpeedUnit.BPT)) return originalSpeed;
    else return originalSpeed * 20 * unit.multiplier;
  }

  // Blame Wikipedia if multipliers (except BPT, BPS, MPS and KMPH) are wrong.
  public enum SpeedUnit {
    BPS(1D, "blocks/s"),
    MPS(1D, "m/s"),
    BPT(0D, "blocks/tick"),
    KMPH(3.6D, "km/h"),
    MPH(2.236936D, "mph"),
    KNOT(1.943844D, "knots"),
    FTPS(3.280840D, "ft/s");

    public double multiplier;
    public String unit;

    SpeedUnit(double multiplier, String unit) {
      this.multiplier = multiplier;
      this.unit = unit;
    }

    public static SpeedUnit get(int index) {
      for (SpeedUnit unit : SpeedUnit.values()) {
        if (unit.ordinal() == index) return unit;
      }
      return null;
    }
  }

  public enum Mode {
    XYZ,
    XY,
    X,
    Y,
    Z;

    public static Mode get(int index) {
      for (Mode mode : Mode.values()) {
        if (mode.ordinal() == index) return mode;
      }
      return null;
    }
  }
}