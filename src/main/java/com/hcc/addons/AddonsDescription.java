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

import com.hcc.exceptions.HCCException;
import com.hcc.utils.Utils;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class AddonsDescription {
    private JSONObject json;

    /**
     * @param file Addon jar
     * @throws HCCException if it fails to read description
     */
    AddonsDescription(File file) throws HCCException {
        JarFile jar;
        try {
            jar = new JarFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new HCCException("Failed to load jarfile");
        }
        try {
            ZipEntry entry = jar.getEntry("addon.json");
            JSONObject json = new JSONObject(new Utils().fromList(IOUtils.readLines(jar.getInputStream(entry), Charset.defaultCharset())));
            this.json = json;
            if (!json.has("version") && !json.has("name") && !json.has("main")) {
                throw new HCCException("Invalid addon jar (addon.json does not exist or invalid)");
            }
        } catch (Exception e) {
            throw new HCCException("Invalid addon jar (addon.json does not exist or invalid)");
        }
    }

    public String getName() {
        return json.getString("name");
    }
    public String getVersion(){
        return json.getString("version");
    }
    public String getMain(){
        return json.getString("main");
    }
}
