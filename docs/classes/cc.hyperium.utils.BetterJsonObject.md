[< Back](../README.md)
# BetterJsonObject #
>#### Class Overview ####
>The solution to the missing methods in Google's implementation
      of json, this class contains many useful methods such as pretty
      printing and file writing, as well as optional methods
## Constructors ##
### public BetterJsonObject () ###
>#### Constructor Overview ####
>The quickest BetterJsonObject constructor, because why not
>
### public BetterJsonObject (String) ###
>#### Constructor Overview ####
>The default constructor the BetterJsonObject class, uses a json string
      as the parameter and attempts to load it, a new JsonObject will be
      created if the input is null, empty or cannot be loaded into a json format
>
>### Parameters ###
>**jsonIn**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the json string to be parsed
>
### public BetterJsonObject (JsonObject) ###
>#### Constructor Overview ####
>The alternative constructor for the BetterJsonObject class, this uses
      another JsonObject as the data set. A new JsonObject will be created
      if the input is null
>
>### Parameters ###
>**objectIn**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the object to be used
>
## Methods ##
### public String optString (String) ###
>#### Method Overview ####
>The optional string method, returns an empty string if
      the key is null, empty or the data does not contain
      the key. This will also return an empty string if the data value
      is not a string
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the value in the json data set or empty if the key cannot be found
>
>### Parameters ###
>**key**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the key the value will be loaded from
>
### public String optString (String, String) ###
>#### Method Overview ####
>The optional string method, returns the default value if
      the key is null, empty or the data does not contain
      the key. This will also return the default value if
      the data value is not a string
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the value in the json data set or the default if the key cannot be found
>
>### Parameters ###
>**key**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the key the value will be loaded from
>
### public int optInt (String) ###
>#### Method Overview ####
>The optional int method, returns 0 if
      the key is null, empty or the data does not contain
      the key. This will also return 0 if the data value
      is not a string
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the value in the json data set or empty if the key cannot be found
>
>### Parameters ###
>**key**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the key the value will be loaded from
>
### public int optInt (String, int) ###
>#### Method Overview ####
>The optional int method, returns the default value if
      the key is null, empty or the data does not contain
      the key. This will also return the default value if
      the data value is not a number
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the value in the json data set or the default if the key cannot be found
>
>### Parameters ###
>**key**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the key the value will be loaded from
>
### public double optDouble (String) ###
>#### Method Overview ####
>The optional double method, returns 0.0D if
      the key is null, empty or the data does not contain
      the key. This will also return 0.0D if the data value
      is not a string
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the value in the json data set or empty if the key cannot be found
>
>### Parameters ###
>**key**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the key the value will be loaded from
>
### public double optDouble (String, double) ###
>#### Method Overview ####
>The optional double method, returns the default value if
      the key is null, empty or the data does not contain
      the key. This will also return the default value if
      the data value is not a number
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the value in the json data set or the default if the key cannot be found
>
>### Parameters ###
>**key**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the key the value will be loaded from
>
### public boolean optBoolean (String) ###
>#### Method Overview ####
>The optional boolean method, returns false if
      the key is null, empty or the data does not contain
      the key. This will also return false if the data value
      is not a string
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the value in the json data set or empty if the key cannot be found
>
>### Parameters ###
>**key**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the key the value will be loaded from
>
### public boolean optBoolean (String, boolean) ###
>#### Method Overview ####
>The optional boolean method, returns the default value if
      the key is null, empty or the data does not contain
      the key. This will also return the default value if
      the data value is not a boolean
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the value in the json data set or the default if the key cannot be found
>
>### Parameters ###
>**key**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the key the value will be loaded from
>
### public boolean has (String) ###
>#### Method Overview ####
>No description provided
>
### public JsonElement get (String) ###
>#### Method Overview ####
>No description provided
>
### public JsonObject getData () ###
>#### Method Overview ####
>Returns the data the information is being loaded from
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the loading data
>
### public BetterJsonObject addProperty (String, String) ###
>#### Method Overview ####
>Adds a string to the to the json data file with the
      key that it'll be associated with
>
>### Parameters ###
>**key**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the key
>
>**value**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the value
>
### public BetterJsonObject addProperty (String, Number) ###
>#### Method Overview ####
>Adds a number to the to the json data file with the
      key that it'll be associated with
>
>### Parameters ###
>**key**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the key
>
>**value**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the value
>
### public BetterJsonObject addProperty (String, Boolean) ###
>#### Method Overview ####
>Adds a boolean to the to the json data file with the
      key that it'll be associated with
>
>### Parameters ###
>**key**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the key
>
>**value**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the value
>
### public BetterJsonObject add (String, BetterJsonObject) ###
>#### Method Overview ####
>Adds another BetterJsonObject into this one
>
>### Parameters ###
>**key**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the key
>
>**object**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the object to add
>
### public void writeToFile (File) ###
>#### Method Overview ####
>This feature is a HUGE WIP and may not work, it is safer
      to use the toString method with a BufferedWriter instead

 We are not responsible for any overwritten files, please use this carefully
>
>### Parameters ###
>**file**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;File to write to
>
### public String toString () ###
>#### Method Overview ####
>Returns the data values toString method
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the data values toString
>
### public String toPrettyString () ###
>#### Method Overview ####
>Returns the pretty printed data String with
      indents and other things
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;pretty printed data
>

---
Powered by [MDDocs](https://github.com/VRCube/MDDocs)