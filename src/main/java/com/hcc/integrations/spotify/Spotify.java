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

package com.hcc.integrations.spotify;

import com.hcc.Metadata;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.SystemUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class Spotify {
    private OkHttpClient client = new OkHttpClient();
    public Spotify() throws Exception{
        File webHelper = getWebHelper();
        if(webHelper == null)
            throw new UnsupportedOperationException("Could not find WebHelper // OS not supported!");
    }

    /**
     * @param url url to get
     * @return JSONObject content
     * @throws IOException if exception occurs
     */
    private JSONObject get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
                .build();
        Response response = client.newCall(request).execute();
        assert response.body() != null;
        return new JSONObject(response.body().string());
    }

    /**
     * @return web helper executable path
     */
    private File getWebHelper(){
        if(SystemUtils.IS_OS_WINDOWS){
            return new File(System.getProperty("user.home"), "\\AppData\\Roaming\\Spotify\\SpotifyWebHelper.exe");
        }else if(SystemUtils.IS_OS_MAC){
            return new File(System.getProperty("user.home"),"/Library/Application Support/Spotify/SpotifyWebHelper");
        }else{
            return null;
        }
    }
}
