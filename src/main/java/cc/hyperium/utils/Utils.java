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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

public class Utils {

    public static final Utils INSTANCE = new Utils();

    public ByteBuffer readImageToBuffer(InputStream inputStream) throws IOException {
        BufferedImage bufferedimage = ImageIO.read(inputStream);
        int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), null, 0, bufferedimage.getWidth());
        ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);
        Arrays.stream(aint).map(i -> i << 8 | (i >> 24 & 255)).forEach(bytebuffer::putInt);
        bytebuffer.flip();
        return bytebuffer;
    }

    public void setCursor(ResourceLocation cursor) {
        try {
            BufferedImage image = ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(cursor).getInputStream());
            int w = image.getWidth();
            int h = image.getHeight();
            int[] pixels = new int[(w * h)];
            image.getRGB(0, 0, w, h, pixels, 0, w);
            ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4);

            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int pixel = pixels[(h - 1 - y) * w + x];
                    buffer.put((byte) (pixel & 0xFF));
                    buffer.put((byte) (pixel >> 8 & 0xFF));
                    buffer.put((byte) (pixel >> 16 & 0xFF));
                    buffer.put((byte) (pixel >> 24 & 0xFF));
                }
            }

            buffer.flip();
            Mouse.setNativeCursor(new Cursor(w, h, 0, h - 1, 1, buffer.asIntBuffer(), null));
        } catch (Exception ignored) {
        }
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
