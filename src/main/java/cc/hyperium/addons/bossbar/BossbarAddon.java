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

package cc.hyperium.addons.bossbar;

import cc.hyperium.Hyperium;
import cc.hyperium.addons.AbstractAddon;
import cc.hyperium.addons.bossbar.commands.CommandBossbar;
import cc.hyperium.addons.bossbar.config.BossbarConfig;
import cc.hyperium.addons.bossbar.gui.GuiBossbarSetting;

public class BossbarAddon extends AbstractAddon {
    private BossbarConfig bossbarConfig;
    private GuiBossbarSetting guiBossBarSetting;

    public GuiBossbarSetting getGuiBossBarSetting() {
        return guiBossBarSetting;
    }

    @Override
    public AbstractAddon init() {
        bossbarConfig = new BossbarConfig();
        guiBossBarSetting = new GuiBossbarSetting();
        CommandBossbar commandBossbar = new CommandBossbar(this);
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand(commandBossbar);
        return this;
    }

    public BossbarConfig getConfig() {
        return bossbarConfig;
    }

    @Override
    public Metadata getAddonMetadata() {
        Metadata metadata = new Metadata(this, "BossbarAddon", "1.0", "SiroQ");
        metadata.setDescription("Allows for full bossbar customization");
        return metadata;
    }
}
