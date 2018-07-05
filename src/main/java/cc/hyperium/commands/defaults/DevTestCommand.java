package cc.hyperium.commands.defaults;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.gui.ParticleGui;

/**
 * Created by mitchellkatz on 6/25/18. Designed for production use on Sk1er.club
 */
public class DevTestCommand implements BaseCommand {
    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        new ParticleGui().show();
    }
}
