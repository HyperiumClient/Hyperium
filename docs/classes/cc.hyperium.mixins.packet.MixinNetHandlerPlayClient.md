[< Back](../README.md)
# MixinNetHandlerPlayClient #
>#### Class Overview ####
>Provides code that may be used in mods that require it
## Constructors ##
### public MixinNetHandlerPlayClient () ###
>#### Constructor Overview ####
>No description provided
>
## Methods ##
### public void handleTimeUpdate (S03PacketTimeUpdate) ###
>#### Method Overview ####
>For TimeChanger, changes the way time packets are handled
>
### public void handleAnimation (S0BPacketAnimation) ###
>#### Method Overview ####
>Renders a specified animation: Waking up a player, a living entity swinging its currently held item, being hurt
 or receiving a critical hit by normal or magical means
>
### public void handleChat (S02PacketChat) ###
>#### Method Overview ####
>Allows detection of incoming chat packets from the server (includes actionbars)

 Byte values for the event
 0 : Standard Text Message, displayed in chat
 1 : 'System' message, displayed as standard text in the chat.
 2 : 'Status' message, displayed as an action bar above the hotbar
>
### public abstract NetworkManager getNetworkManager () ###
>#### Method Overview ####
>No description provided
>

---
Powered by [MDDocs](https://github.com/VRCube/MDDocs)