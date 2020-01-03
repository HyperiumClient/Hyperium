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

import cc.hyperium.Hyperium;
import cc.hyperium.config.Settings;
import cc.hyperium.purchases.AbstractHyperiumPurchase;
import cc.hyperium.purchases.EnumPurchaseType;
import cc.hyperium.purchases.HyperiumPurchase;
import cc.hyperium.purchases.PurchaseApi;
import cc.hyperium.purchases.packages.EarsCosmetic;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Utils {

    public static final Utils INSTANCE = new Utils();

    public ByteBuffer readImageToBuffer(InputStream inputStream) throws IOException {
        BufferedImage bufferedimage = ImageIO.read(inputStream);
        int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), null, 0, bufferedimage.getWidth());
        ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);
        for (int i : aint) {
            int i1 = i << 8 | (i >> 24 & 255);
            bytebuffer.putInt(i1);
        }

        bytebuffer.flip();
        return bytebuffer;
    }

    /*
     * @return the entity name offset
     * */
    public <T extends Entity> float calculateDeadmauEarsOffset(T entity) {
        try {
            if (entity instanceof EntityPlayer && Hyperium.INSTANCE.getCosmetics().getDeadmau5Cosmetic().isPurchasedBy(entity.getUniqueID())) {
                HyperiumPurchase packageIfReady = PurchaseApi.getInstance().getPackageIfReady(entity.getUniqueID());

                if (packageIfReady != null) {
                    AbstractHyperiumPurchase purchase = packageIfReady.getPurchase(EnumPurchaseType.DEADMAU5_COSMETIC);
                    if (purchase != null) {
                        if (entity.getUniqueID() != Minecraft.getMinecraft().thePlayer.getUniqueID()) {
                            if (purchase instanceof EarsCosmetic && ((EarsCosmetic) purchase).isEnabled()) {
                                return 0.24F;
                            }
                        } else if (Settings.EARS_STATE.equalsIgnoreCase("on")) {
                            return 0.24F;
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return 0F;
    }
}
