package cc.hyperium.commands.defaults;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import cc.hyperium.gui.ParticleGui;

public class CommandParticleAuras implements BaseCommand {
    @Override
    public String getName() {
        return "particleaura";
    }

    @Override
    public String getUsage() {
        return "/particleaura";
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        new ParticleGui().show();
    }
}
