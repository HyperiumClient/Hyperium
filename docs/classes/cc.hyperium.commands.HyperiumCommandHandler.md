[< Back](../README.md)
# HyperiumCommandHandler #
>#### Class Overview ####
>This is our custom client-side command implementation, it handles most of the
 command logic and other firing methods. Commands should be register by doing
 <i>Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand({@link BaseCommand})</i>
## Constructors ##
### public HyperiumCommandHandler () ###
>#### Constructor Overview ####
>No description provided
>
## Methods ##
### public void onChat (SendChatMessageEvent) ###
>#### Method Overview ####
>No description provided
>
### public void registerCommand (BaseCommand) ###
>#### Method Overview ####
>Registers the command to this CommandHandler instance.
      also registers any aliases if applicable
>
>### Parameters ###
>**command**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The command to register
>
### public void removeCommand (BaseCommand) ###
>#### Method Overview ####
>Removes a register command & all aliases
>
>### Parameters ###
>**command**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the command to unregister
>
### public void autoComplete (String) ###
>#### Method Overview ####
>No description provided
>
### public String getLatestAutoComplete () ###
>#### Method Overview ####
>No description provided
>
### public void clear () ###
>#### Method Overview ####
>No description provided
>

---
Powered by [MDDocs](https://github.com/VRCube/MDDocs)