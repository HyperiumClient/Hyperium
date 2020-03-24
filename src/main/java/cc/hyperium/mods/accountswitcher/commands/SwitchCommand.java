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

package cc.hyperium.mods.accountswitcher.commands;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.commands.CommandUsageException;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;

public class SwitchCommand implements BaseCommand {

  @Override
  public String getName() {
    return "accountswitcher";
  }

  @Override
  public String getUsage() {
    return "Usage: /accountswitcher <username/email> [password]";
  }

  @Override
  public void onExecute(String[] args) throws CommandException {
    if (args.length == 0 || args.length > 3) {
      throw new CommandUsageException();
    }

    if (args.length == 1) {
      // log in using an offline account.
      Hyperium.INSTANCE.getModIntegration().getAccountSwitcher().getAccountManager()
          .setOffline(args[0]);
    }

    if (args.length == 2) {
      // log in using a username/email and password.
      boolean b = Hyperium.INSTANCE.getModIntegration().getAccountSwitcher().getAccountManager()
          .setUser(args[0], args[1]);
      // Failed to login :(
      if (!b) {
        sendPlayerMessage(
            "&cNot able to login with that account! Are authentication servers down?");
        return;
      }
    }

    sendPlayerMessage(
        "&aYou should now be using a different account! Please rejoin the world/server.");
  }

}
