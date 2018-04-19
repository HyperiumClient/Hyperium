[< Back](../README.md)
# HyperiumTextField #
>#### Class Overview ####
>No description provided
## Fields ##
### public int xPosition ###
>#### Field Overview ####
>No description provided
>
>**default**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;null
>
### public int yPosition ###
>#### Field Overview ####
>No description provided
>
>**default**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;null
>
## Constructors ##
### public HyperiumTextField (int, HyperiumFontRenderer, int, int, int, int) ###
>#### Constructor Overview ####
>No description provided
>
## Methods ##
### public void setText (String) ###
>#### Method Overview ####
>Sets the text of the textbox
>
### public String getText () ###
>#### Method Overview ####
>Returns the contents of the textbox
>
### public String getSelectedText () ###
>#### Method Overview ####
>returns the text between the cursor and selectionEnd
>
### public void writeText (String) ###
>#### Method Overview ####
>replaces selected text, or inserts text at the position on the cursor
>
### public void deleteWords (int) ###
>#### Method Overview ####
>Deletes the specified number of words starting at the cursor position. Negative numbers will delete words left of
 the cursor.
>
### public void deleteFromCursor (int) ###
>#### Method Overview ####
>delete the selected text, otherwsie deletes characters from either side of the cursor. params: delete num
>
### public void moveCursorBy (int) ###
>#### Method Overview ####
>Moves the text cursor by a specified number of characters and clears the selection
>
### public void setCursorPosition (int) ###
>#### Method Overview ####
>sets the position of the cursor to the provided index
>
### public void setCursorPositionEnd () ###
>#### Method Overview ####
>sets the cursors position to after the text
>
### public boolean textboxKeyTyped (char, int) ###
>#### Method Overview ####
>Call this method from your GuiScreen to process the keys into the textbox
>
### public void mouseClicked (int, int, int) ###
>#### Method Overview ####
>Args: x, y, buttonClicked
>
### public void drawTextBox () ###
>#### Method Overview ####
>Draws the textbox
>
### public void setFocused (boolean) ###
>#### Method Overview ####
>No description provided
>
### public void setSelectionPos (int) ###
>#### Method Overview ####
>No description provided
>

---
Powered by [MDDocs](https://github.com/VRCube/MDDocs)