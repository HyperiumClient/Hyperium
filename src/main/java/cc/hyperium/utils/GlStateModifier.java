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

package cc.hyperium.utils;

import net.minecraft.client.renderer.GlStateManager;

public class GlStateModifier implements IGlStateModifier {

  // Create an instance to be used in other classes
  public static final IGlStateModifier INSTANCE = new GlStateModifier();

  /**
   * Get textureName through reflection to allow for accessibility
   *
   * @param id Texture ic
   */
  public void setTexture(int id) {
//    GlStateManager.TextureState o = GlStateManager.textureState[GlStateManager.activeTextureUnit];
//    o.textureName = id;
  }

  /**
   * Reset the color
   */
  @Override
  public void resetColor() {
//    GlStateManager.colorState.red = -1;
  }

  /**
   * Reset the texture
   */
  public void reset() {
//    setTexture(-1);
  }

}
