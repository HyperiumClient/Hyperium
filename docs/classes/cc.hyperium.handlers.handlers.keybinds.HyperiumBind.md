[< Back](../README.md)
# HyperiumBind #
>#### Class Overview ####
>Our implementation of the normal Minecraft KeyBinding, uses anonymous
 classes instead of using an onTick event, this is debatably better
## Constructors ##
### public HyperiumBind (String, int) ###
>#### Constructor Overview ####
>No description provided
>
### public HyperiumBind (String, int, String) ###
>#### Constructor Overview ####
>No description provided
>
## Methods ##
### public int getKeyCode () ###
>#### Method Overview ####
>Returns the current code of the key
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the key code
>
### public int getDefaultKeyCode () ###
>#### Method Overview ####
>Returns the default key code for the key
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the default key code
>
### public void setKeyCode (int) ###
>#### Method Overview ####
>Setter for the key code, sets it here and calls the super
 method to update it as well
>
>### Parameters ###
>**key**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the key
>
### public String getKeyDescription () ###
>#### Method Overview ####
>Returns the key description, this will be capitalized if the capitalize
 method returns true (see {@link #capitalizeDescription()})
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the keys description
>
### protected String getRealDescription () ###
>#### Method Overview ####
>Gets the description without the formatting, this is used for saving
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the real description of the KeyBind
>
### public void setWasPressed (boolean) ###
>#### Method Overview ####
>Setter for the WasPressed variable, if true it means the buttons
 last event was to be pressed
>
>### Parameters ###
>**wasPressed**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if the button was pressed
>
### public boolean wasPressed () ###
>#### Method Overview ####
>Was the the last event on the key a key press?
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;true if the key was last pressed
>
### public boolean capitalizeDescription () ###
>#### Method Overview ####
>Should the Key Description be capitalized, this looks neater in the
 controls menu, instead of leaving it lowercase
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;true if the description should be capitalized
>
### public void onPress () ###
>#### Method Overview ####
>Called when the button is pressed, override this ton
>
### public void onRelease () ###
>#### Method Overview ####
>No description provided
>

---
Powered by [MDDocs](https://github.com/VRCube/MDDocs)