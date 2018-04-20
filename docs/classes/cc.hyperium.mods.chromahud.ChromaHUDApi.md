[< Back](../README.md)
# ChromaHUDApi #
>#### Class Overview ####
>No description provided
## Fields ##
### public static final String VERSION ###
>#### Field Overview ####
>No description provided
>
>**default**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3.0-Hyperium
>
## Methods ##
### public static ChromaHUDApi getInstance () ###
>#### Method Overview ####
>No description provided
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ChromaHUD Api Instance
>
### public List getButtonConfigs (String) ###
>#### Method Overview ####
>No description provided
>
### public List getTextConfigs (String) ###
>#### Method Overview ####
>No description provided
>
### public List getStringConfigs (String) ###
>#### Method Overview ####
>No description provided
>
### public List getElements () ###
>#### Method Overview ####
>No description provided
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;All Display Elements the client is
>
### public void registerTextConfig (String, TextConfig) ###
>#### Method Overview ####
>Register a text CONFIG
>
>### Parameters ###
>**type**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;String ID for DisplayItem to show and activate on for CONFIG
>
>**config**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Config object
>
### public void registerStringConfig (String, StringConfig) ###
>#### Method Overview ####
>Register a String CONFIG
>
>### Parameters ###
>**type**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;String ID for DisplayItem to show and activate on for CONFIG
>
>**config**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Config object
>
### public void registerButtonConfig (String, ButtonConfig) ###
>#### Method Overview ####
>Register a button CONFIG
>
>### Parameters ###
>**type**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;String ID for DisplayItem to show and activate on for CONFIG
>
>**config**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Config object
>
### public void register (ChromaHUDParser) ###
>#### Method Overview ####
><p>Add a parser to the ChromaHUD runtime. Must be done before FMLPostInitialization event</p>
>
>### Parameters ###
>**parser**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A valid ChromaHUDParser object for an addon.
>
### protected void post (JsonHolder) ###
>#### Method Overview ####
>Internal method to setup system once all items have been registered
>
>### Parameters ###
>**config**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Config data from file
>
### public DisplayItem parse (String, int, JsonHolder) ###
>#### Method Overview ####
>Parse DisplayItem from CONFIG
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;DisplayItem instance created, null if the system was unable to resolve type
>
>### Parameters ###
>**type**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;type of item
>
>**ord**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ordinal inside element
>
>**item**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Other JSON data that is stored
>
### public String getName (String) ###
>#### Method Overview ####
>ID -> Human readable name for a DisplayItem
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Display name of DisplayItem
>
>### Parameters ###
>**type**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;type of DisplayItem to retrieve name for
>
### public List getParsers () ###
>#### Method Overview ####
>No description provided
>

---
Powered by [MDDocs](https://github.com/VRCube/MDDocs)