[< Back](../README.md)
# IKey #
>#### Class Overview ####
>Used as the base class for all keys with a few essential methods and fields
## Fields ##
### protected final Minecraft mc ###
>#### Field Overview ####
>No description provided
>
>**default**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;null
>
### protected final KeystrokesMod mod ###
>#### Field Overview ####
>No description provided
>
>**default**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;null
>
### protected final int xOffset ###
>#### Field Overview ####
>No description provided
>
>**default**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;null
>
### protected final int yOffset ###
>#### Field Overview ####
>No description provided
>
>**default**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;null
>
## Constructors ##
### public IKey (KeystrokesMod, int, int) ###
>#### Constructor Overview ####
>No description provided
>
## Methods ##
### protected void drawChromaString (String, int, int) ###
>#### Method Overview ####
>No description provided
>
### protected abstract void renderKey (int, int) ###
>#### Method Overview ####
>Renders the key at the specified x and y location
>
### protected final int getXOffset () ###
>#### Method Overview ####
>Gets the x offset of the key
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;x offset of the key
>
### protected final int getYOffset () ###
>#### Method Overview ####
>Gets the y offset of the key
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;y offset
>
### protected final int getColor () ###
>#### Method Overview ####
>Gets the color of the text whilst the key is not being pressed
 <p>
 if chroma this will return the current generated chroma color
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the color from settings or chroma if its enabled
>
### public final int getPressedColor () ###
>#### Method Overview ####
>Gets the color of the text whilst the key is being pressed
 <p>
 this will not be used if chroma is enabled
>
>**returns**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the color from settings or chroma if its enabled
>
### protected final void drawCenteredString (String, int, int, int) ###
>#### Method Overview ####
>Draws a centered string without a background shadow at the specified location
      with the given color
>
>### Parameters ###
>**text**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;text to draw
>
>**x**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the x position for the text
>
>**y**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the y position for the text
>
>**color**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the texts color
>

---
Powered by [MDDocs](https://github.com/VRCube/MDDocs)