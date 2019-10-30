package me.semx11.autotip.command.impl;

import cc.hyperium.commands.CommandException;
import me.semx11.autotip.Autotip;
import me.semx11.autotip.chat.MessageUtil;
import me.semx11.autotip.command.CommandAbstract;

public class CommandLimbo extends CommandAbstract {

    private boolean executed;

    public CommandLimbo(Autotip autotip) {
        super(autotip);
    }

    public boolean hasExecuted() {
        return executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }

    @Override
    public String getName() {
        return "limbo";
    }

    @Override
    public String getUsage() {
        return "/limbo";
    }

    @Override
    public void onExecute(String[] args) throws CommandException {
        MessageUtil messageUtil = autotip.getMessageUtil();

        if (autotip.getSessionManager().isOnHypixel()) {
            executed = true;
            messageUtil.sendCommand("/achat \u00a7c");
        } else {
            messageUtil.send("&cYou must be on Hypixel to use this command!");
        }
    }
}
