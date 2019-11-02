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

package cc.hyperium.mods.autogg.commands;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.commands.CommandUsageException;
import cc.hyperium.handlers.handlers.chat.GeneralChatHandler;
import cc.hyperium.mods.autogg.AutoGG;
import cc.hyperium.utils.ChatColor;

public class GGCommand implements BaseCommand {

    private final AutoGG mod;

    public GGCommand(AutoGG mod) {
        this.mod = mod;
    }

    @Override
    public String getName() {
        return "autogg";
    }

    @Override
    public String getUsage() {
        return "Usage: /autogg <toggle, delay [seconds]>";
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        if (args.length == 0 || args.length > 2) {
            throw new CommandUsageException();
        }

        final String s = args[0];

        switch (s) {
            case "toggle":
            case "t": {
                mod.getConfig().flipToggle();

                showMessage(
                    ChatColor.GRAY + "AutoGG: " + (mod.getConfig().isToggled() ? (
                        ChatColor.GREEN + "On") : (ChatColor.RED + "Off")));

                saveConfig();
                break;
            }

            case "delay":
            case "d":
            case "time": {
                if (args.length == 2) {
                    try {
                        final int delay = Integer.parseInt(args[1]);
                        assert delay >= 0 && delay <= 5 : "Invalid integer";
                        mod.getConfig().setDelay(delay);

                        saveConfig();

                        showMessage(ChatColor.GRAY + "AutoGG delay set to "
                            + ChatColor.GREEN + mod.getConfig().getDelay() + "s");
                    } catch (NumberFormatException e) {
                        showMessage(ChatColor.RED + "Error: Please use an integer between 1 and 5 seconds.");
                    }
                    break;
                }
                showMessage(
                    ChatColor.GRAY + "AutoGG Delay: " + ChatColor.GREEN + mod.getConfig().getDelay() + "s");
                break;
            }

            default: {
                throw new CommandUsageException();
            }
        }
    }

    /**
     * Sends a message to the client
     *
     * @param message the message to send
     */
    private void showMessage(final String message) {
        GeneralChatHandler.instance().sendMessage(message, false);
    }

    /**
     * Saves the config
     */
    private void saveConfig() {
        Hyperium.CONFIG.save();
    }
}
