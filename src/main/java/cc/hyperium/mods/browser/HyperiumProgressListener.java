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

package cc.hyperium.mods.browser;

import cc.hyperium.SplashProgress;
import net.montoyo.mcef.utilities.IProgressListener;

public class HyperiumProgressListener implements IProgressListener {

    private int oldProgress = -1;

    @Override
    public void onProgressed(double d) {
        SplashProgress.PROGRESS = (int) d;

        SplashProgress.update();
    }

    @Override
    public void onTaskChanged(String name) {
        if (oldProgress == -1) {
            oldProgress = SplashProgress.PROGRESS;
        }

        SplashProgress.MAX = 100;
        SplashProgress.CURRENT = name;
        SplashProgress.update();
    }

    @Override
    public void onProgressEnd() {
        SplashProgress.MAX = SplashProgress.DEFAULT_MAX;
        SplashProgress.PROGRESS = oldProgress;
        SplashProgress.CANCEL_IF_MAX = true;
    }
}
