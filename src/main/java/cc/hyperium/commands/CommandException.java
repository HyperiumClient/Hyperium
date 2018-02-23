package cc.hyperium.commands;

/**
 * Generic exception for command handling which triggers the message
 * to be sent to the players chat
 */
public class CommandException extends Exception {
    
    /**
     * Basic constructor for no arguments
     */
    public CommandException() {
        super();
    }
    
    public CommandException(String message) {
        super(message);
    }
}

