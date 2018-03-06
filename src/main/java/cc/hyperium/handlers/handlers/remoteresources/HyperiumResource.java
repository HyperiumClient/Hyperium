/*
 *  Hypixel Community Client, Client optimized for Hypixel Network
 *     Copyright (C) 2018  Hyperium Dev Team
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

package cc.hyperium.handlers.handlers.remoteresources;

import cc.hyperium.utils.JsonHolder;

import java.awt.image.BufferedImage;

public class HyperiumResource {

    private String data;
    private boolean successfullyLoaded;
    private JsonHolder asJson = null;
    private BufferedImage image;

    public HyperiumResource(String data, boolean successfullyLoaded) {
        this.data = data;
        this.successfullyLoaded = successfullyLoaded;
    }
    //Order switched so null, false won't call just string
    public HyperiumResource(boolean successfullyLoaded, BufferedImage image) {
        this.successfullyLoaded = successfullyLoaded;
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getData() {
        return data;
    }

    public boolean isSuccessfullyLoaded() {
        return successfullyLoaded;
    }

    public JsonHolder getasJson() {
        if (asJson == null) {
            asJson = new JsonHolder(data);
        }
        return asJson;
    }
}
