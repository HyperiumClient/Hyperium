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

package cc.hyperium.commands.defaults;


import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.handlers.handlers.tracking.StatisticViewingGui;
import cc.hyperium.handlers.handlers.tracking.ValueTrackingType;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class CommandTest implements BaseCommand {

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getUsage() {
        return "/test";
    }

    @Override
    public void onExecute(String[] args) {
        long l = System.currentTimeMillis();
        long l1 = l - TimeUnit.DAYS.toMillis(1);
        System.out.println(l);
        System.out.println(l1);
        for (int i = 0; i < (args.length != 0 ? 500 : 0); i++) {
            long time = ThreadLocalRandom.current().nextLong(l1, l);
            Hyperium.INSTANCE.getHandlers().getHypixelValueTracking().post(ValueTrackingType.COINS, ThreadLocalRandom.current().nextInt(1000),
                    time);
        }
        new StatisticViewingGui().show();
    }
}
