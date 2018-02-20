package com.hcc.commands;

/**
 * An exception caused by invalid command usage, will log the command
 * usage to the client (instead of anything else)
 *
 * @author boomboompower
 */
public class CommandUsageException extends CommandException {
    
    /**
     * Default constructor for this class
     */
    public CommandUsageException() {
    }
}

