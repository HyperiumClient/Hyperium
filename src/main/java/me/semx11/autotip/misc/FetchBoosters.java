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

package me.semx11.autotip.misc;

import me.semx11.autotip.Autotip;
import me.semx11.autotip.event.Tipper;
import me.semx11.autotip.util.Host;
import me.semx11.autotip.util.Hosts;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Collections;

public class FetchBoosters implements Runnable {

    @Override
    public void run() {
        Host tipHost = Hosts.getInstance().getHostById("totip");
        if (tipHost.isEnabled()) {
            try {
                HttpClient httpClient = HttpClientBuilder.create().build();
                HttpPost request = new HttpPost(tipHost.getUrl());
                request.addHeader("Content-Type", "application/x-www-form-urlencoded");
                request.setEntity(new StringEntity(Minecraft.getMinecraft().getSession().getUsername() + ":"
                        + String.join(":", Autotip.alreadyTipped)));
                HttpResponse response = httpClient.execute(request);
                Collections.addAll(Tipper.tipQueue,
                        EntityUtils.toString(response.getEntity()).split(":"));
                System.out.println("Fetched Boosters: " + StringUtils.join(Tipper.tipQueue, ", "));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Collections.addAll(Tipper.tipQueue, "all");
        }
    }
}
