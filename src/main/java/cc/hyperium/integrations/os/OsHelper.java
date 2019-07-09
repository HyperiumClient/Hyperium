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

package cc.hyperium.integrations.os;

import org.apache.commons.lang3.SystemUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OsHelper {
    public static boolean isProcessRunning(String proc) throws IOException {
        if (SystemUtils.IS_OS_WINDOWS)
            return isProcessRunningWindows(proc);
        else if (SystemUtils.IS_OS_MAC)
            return isProcessRunningMac(proc);
        return false;
    }

    private static boolean isProcessRunningWindows(String proc) throws IOException {
        String line;
        StringBuilder pidInfo = new StringBuilder();
        Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((line = input.readLine()) != null) {
            pidInfo.append(line);
        }
        input.close();
        return pidInfo.toString().contains(proc);
    }

    private static boolean isProcessRunningMac(String proc) throws IOException {
        String line;
        StringBuilder pidInfo = new StringBuilder();
        Process p = Runtime.getRuntime().exec("ps -A");
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        while ((line = input.readLine()) != null) {
            pidInfo.append(line);
        }
        input.close();
        return pidInfo.toString().contains(proc);
    }
}
