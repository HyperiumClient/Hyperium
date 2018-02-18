package com.hcc.commands;

/**
 * Generic exception for command handling
 */
public class CommandException extends Exception {

    public CommandException(String message) {
        super(message);
    }
}

