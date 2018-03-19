/*
 *     Copyright (C) 2018  Hyperium <https://hyperium.cc/>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.semx11.autotip.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Versions {

    private static Versions instance;

    private Version latest;
    private Version latestBeta;
    private List<VersionInfo> versions = new ArrayList<>();

    private Versions(Version latest) {
        this.latest = latest;
    }

    public static Versions getInstance() {
        return instance;
    }

    public static void updateVersions() {
        Gson gson = new GsonBuilder().setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        try {
            String json = IOUtils.toString(
                    new URL("https://gist.githubusercontent.com/Semx11/35d6b58783ef8d0527f82782f6555834/raw/versions.json"));
            instance = gson.fromJson(json, Versions.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        instance.versions.sort((v1, v2) -> v2.getVersion().compareTo(v1.getVersion()));
    }

    public void addVersion(Version version, VersionInfo.Severity severity, boolean isBetaVersion,
                           String... changelog) {
        versions.add(new VersionInfo(version, severity, isBetaVersion, changelog));
    }

    public VersionInfo getInfoByVersion(Version version) {
        return versions.stream()
                .filter(v -> v.getVersion().equals(version))
                .findFirst()
                .orElse(null);
    }

    public List<VersionInfo> getHigherVersionInfo(Version version) {
        return getHigherVersionInfo(version, this.latest);
    }

    public List<VersionInfo> getHigherVersionInfo(Version version, Version highest) {
        return versions.stream()
                .filter(vi -> vi.getVersion().compareTo(version) == 1
                        && vi.getVersion().compareTo(highest) < 1)
                .collect(Collectors.toList());
    }

    public Version getLatest() {
        return latest;
    }

    public Version getLatestBeta() {
        return latestBeta;
    }

}
