[< Back](../README.md)
# KeyBindHandler #
>#### Class Overview ####
>No description provided
## Fields ##
### public static HyperiumBind nameHistory ###
>#### Field Overview ####
>No description provided
>
>**default**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;null
>
### public HyperiumBind friends ###
>#### Field Overview ####
>No description provided
>
>**default**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;null
>
### public HyperiumBind queue ###
>#### Field Overview ####
>No description provided
>
>**default**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;null
>
### public HyperiumBind debug ###
>#### Field Overview ####
>No description provided
>
>**default**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;null
>
### public HyperiumBind flossDance ###
>#### Field Overview ####
>No description provided
>
>**default**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;null
>
### public HyperiumBind dab ###
>#### Field Overview ####
>No description provided
>
>**default**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;null
>
### public HyperiumBind guikey ###
>#### Field Overview ####
>Opens GUI on Z key pressed oof - ConorTheOreo
>
>**default**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;null
>
## Constructors ##
### public KeyBindHandler () ###
>#### Constructor Overview ####
>No description provided
>
## Methods ##
### public void onKeyPress (KeypressEvent) ###
>#### Method Overview ####
>No description provided
>
### public void onGameShutdown (GameShutDownEvent) ###
>#### Method Overview ####
>No description provided
>
### public HyperiumBind getBinding (String) ###
>#### Method Overview ####
>Grabs a binding from the registered keybindings list, this is case-insensitive and
 any key/name may be provided without fear of causing issues
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;a keybinding instance or null if nothing was found
>
>### Parameters ###
>**name**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the name or id of the keybinding
>
### public void registerKeyBinding (HyperiumBind) ###
>#### Method Overview ####
>Registers a Hyperium KeyBinding here & in the game code so it shows up in the
 controls menu, allowing the user to modify the keyblind
>
>### Parameters ###
>**bind**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the hyperium key we wish to register
>
### public KeyBindConfig getKeyBindConfig () ###
>#### Method Overview ####
>Getter for the amazing KeyBind config
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the keybind config
>
### protected TreeMap getKeybinds () ###
>#### Method Overview ####
>Getter for the all the registered key bindings, this is package
 private to allow for saving and loading
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the keybinds
>

---
Powered by [MDDocs](https://github.com/VRCube/MDDocs)