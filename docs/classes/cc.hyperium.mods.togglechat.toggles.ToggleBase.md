[< Back](../README.md)
# ToggleBase #
>#### Class Overview ####
>A lite-wight version of ToggleBase
## Constructors ##
### public ToggleBase () ###
>#### Constructor Overview ####
>Default constructor for ToggleBase
>
## Methods ##
### public abstract String getName () ###
>#### Method Overview ####
>Returns the name of the specified ToggleBase
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the name of the specified ToggleBase, cannot be null
>
### public abstract boolean shouldToggle (String) ###
>#### Method Overview ####
>Checks the given text to see if it should be toggled
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;true if the message matches the toggle test
>
>### Parameters ###
>**message**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;message to test
>
### public abstract boolean isEnabled () ###
>#### Method Overview ####
>Checks to see if the given chat is enabled
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;true if the player wants to see the given chat
>
### public abstract void setEnabled (boolean) ###
>#### Method Overview ####
>Sets the message to be toggled or not. Is used in
      toggle loading
>
>### Parameters ###
>**enabled**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;used in loading to set the toggled enabled/disabled
>
### public abstract LinkedList getDescription () ###
>#### Method Overview ####
>Gets the description of the specified toggle,
      this will show up in the main toggle gui
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;description of the toggle, can be null
>
### public final boolean hasDescription () ###
>#### Method Overview ####
>Confirms if the toggle has a description
      returns false if the description is null or empty
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;true if the description is valid
>
### public String getDisplayName () ###
>#### Method Overview ####
>Gets the display format for the button.
      Will be formatted when loaded
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The button display format
>
### public boolean useFormattedMessage () ###
>#### Method Overview ####
>Should the shouldToggle method use the
      formatted chat for the regular check?
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;true if the formatted message should
      be used
>
### public final LinkedList asLinked (T[]) ###
>#### Method Overview ####
>Assistance in linked-list creation
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;a list view of the specified array
>
>### Parameters ###
>**entry**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the array by which the list will be backed
>
### public final boolean containsIgnoreCase (String, String) ###
>#### Method Overview ####
>Checks if the message contains something without
      being case-sensitive
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;true if it contains it
>
>### Parameters ###
>**message**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The message to check
>
>**contains**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the contents
>
### public String getStatus (boolean) ###
>#### Method Overview ####
>No description provided
>

---
Powered by [MDDocs](https://github.com/VRCube/MDDocs)