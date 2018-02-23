/*
 * Hyperium Client, Free client with huds and popular mod
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

package me.semx11.autotip.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Hosts {

    private static Hosts instance;

    private List<Host> hosts;

    private Hosts() {
    }

    public static Hosts getInstance() {
        return instance;
    }

    public static void updateHosts() {
        Gson gson = new GsonBuilder().setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        try {
            String json = IOUtils.toString(
                    new URL("https://gist.githubusercontent.com/Semx11/35d6b58783ef8d0527f82782f6555834/raw/hosts.json"));
            instance = gson.fromJson(json, Hosts.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Host getHostById(String id) {
        return hosts.stream()
                .filter(h -> h.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

}
