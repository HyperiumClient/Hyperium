[< Back](../README.md)
# BaseCommand #
>#### Class Overview ####
>The basic command implementation
## Methods ##
### public String getName () ###
>#### Method Overview ####
>Gets the name of the command
>
### public String getUsage () ###
>#### Method Overview ####
>Gets the usage string for the command.
>
### public List getCommandAliases () ###
>#### Method Overview ####
>A list of aliases to the main command
      this will not be used if null/empty
>
### public void onExecute (String[]) ###
>#### Method Overview ####
>Callback when the command is invoked
>
>### Throws ###
>**CommandException**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;for errors inside the command, these errors
 will log directly to the players chat (without a prefix)
>
### public List onTabComplete (String[]) ###
>#### Method Overview ####
>No description provided
>

---
Powered by [MDDocs](https://github.com/VRCube/MDDocs)