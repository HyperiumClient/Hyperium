[< Back](../README.md)
# ToggleBaseHandler #
>#### Class Overview ####
>No description provided
## Constructors ##
### public ToggleBaseHandler () ###
>#### Constructor Overview ####
>No description provided
>
## Methods ##
### public LinkedHashMap getToggles () ###
>#### Method Overview ####
>No description provided
>
### public void addToggle (ToggleBase) ###
>#### Method Overview ####
>Adds the developers own ToggleBase
>
>### Parameters ###
>**toggleBase**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the developers toggle
>
### public boolean shouldToggle (String) ###
>#### Method Overview ####
>Run through all bases and check if the
      given text should be toggled
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the formatted text
>
>### Parameters ###
>**input**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;text to test
>
### public void remake () ###
>#### Method Overview ####
>Clears all toggles and readds default ones
>
### public ToggleBase getToggle (String) ###
>#### Method Overview ####
>Gets a toggle by the given name, may return null
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;a ToggleBase instance if found, or else null
>
>### Parameters ###
>**name**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the toggle's name
>
### public boolean hasToggle (String) ###
>#### Method Overview ####
>Checks to see if the registered parsers contains a parser
      with the given name.
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;true if it is registered
>
>### Parameters ###
>**name**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The toggle's name to test for
>

---
Powered by [MDDocs](https://github.com/VRCube/MDDocs)