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

package cc.hyperium.mods.accountswitcher;

import cc.hyperium.Hyperium;
import cc.hyperium.mods.AbstractMod;
import cc.hyperium.mods.accountswitcher.account.AccountManager;
import cc.hyperium.mods.accountswitcher.commands.SwitchCommand;
import cc.hyperium.mods.accountswitcher.gui.AccountGUI;
import cc.hyperium.utils.ChatColor;
import net.minecraft.client.gui.GuiScreen;

public class AccountSwitcher extends AbstractMod {

    private AccountManager accountManager;
    private final Metadata metadata;

    public AccountSwitcher() {
        metadata = new Metadata(this, "AccountSwitcher", "1.0", "littlemissantivirus");
        metadata.setDisplayName(ChatColor.AQUA + "AccountSwitcher");

        accountManager = new AccountManager();
    }

    @Override
    public AbstractMod init() {
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler()
                .registerCommand(new SwitchCommand());

        return this;
    }

    @Override
    public Metadata getModMetadata() {
        return metadata;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

}
