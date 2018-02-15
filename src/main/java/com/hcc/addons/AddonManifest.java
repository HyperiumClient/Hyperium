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

package com.hcc.addons;

import com.google.common.io.Files;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hcc.exceptions.HCCException;
import com.hcc.utils.Utils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class AddonManifest {

    /**
     * Manifest json
     */
    private JsonObject json;

    /**
     * @throws HCCException if it fails to read description
     * @param jar Addon jar
     */
    public AddonManifest(JarFile jar) throws HCCException {

        try {
            ZipEntry entry = jar.getEntry("addon.json");
            JsonParser parser = new JsonParser();

            File jsonFile = File.createTempFile("json","tmp");
            jsonFile.deleteOnExit();

            InputStream jarInputStream = jar.getInputStream(entry);
            OutputStream os = new FileOutputStream(jsonFile);
            IOUtils.copy(jarInputStream,os);

            JsonObject json = parser.parse(Files.toString(jsonFile,Charset.defaultCharset())).getAsJsonObject();
            this.json = json;
            if(!json.has("version") && !json.has("name") && !json.has("main")){
                System.out.println("Json doesn't have correct attributes!");
                throw new HCCException("Invalid addon jar (addon.json does not exist or invalid)");
            }
        } catch (Exception e) {
            System.out.println("Json is fine, just another error.");
            e.printStackTrace();
            throw new HCCException("Invalid addon jar (addon.json does not exist or invalid)");
        }
    }

    /**
     * @return addon name
     */
    public String getName() {
        return json.get("name").getAsString();
    }

    /**
      @return addon version
     */
    public String getVersion() {
        return json.get("version").getAsString();
    }

    /**
     * @return main-class
     */
    public String getMain() {
        return json.get("main").getAsString();
    }

    /**
     * @return addon manifest json in string
     */
    @Override
    public String toString() {
        return json.toString();
    }
}
