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

package cc.hyperium.mods.motionblur.resource;


import cc.hyperium.config.Settings;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Locale;

public class MotionBlurResource implements IResource {

  private static final String JSON = "{\"targets\":[\"swap\",\"previous\"],\"passes\":[{\"name\":\"phosphor\",\"intarget\":\"minecraft:main\",\"outtarget\":\"swap\",\"auxtargets\":[{\"name\":\"PrevSampler\",\"id\":\"previous\"}],\"uniforms\":[{\"name\":\"Phosphor\",\"values\":[%.2f, %.2f, %.2f]}]},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"previous\"},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"minecraft:main\"}]}";

  @Override
  public ResourceLocation getResourceLocation() {
    return null;
  }

  @Override
  public InputStream getInputStream() {
    double amount = 0.7 + Settings.MOTION_BLUR_AMOUNT / 100.0 * 3.0 - 0.01;
    return IOUtils.toInputStream(String.format(Locale.ENGLISH, JSON, amount, amount, amount),
        Charset.defaultCharset());
  }

  @Override
  public boolean hasMetadata() {
    return false;
  }

  @Override
  public <T extends IMetadataSection> T getMetadata(String p_110526_1_) {
    return null;
  }

  @Override
  public String getResourcePackName() {
    return null;
  }
}
