package me.semx11.autotip.command;

import cc.hyperium.commands.BaseCommand;
import me.semx11.autotip.Autotip;
import net.minecraft.command.ICommandSender;

public abstract class CommandAbstract implements BaseCommand {

    public final Autotip autotip;

    public CommandAbstract(Autotip autotip) {
        this.autotip = autotip;
    }




}