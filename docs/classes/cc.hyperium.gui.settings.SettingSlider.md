[< Back](../README.md)
# SettingSlider #
>#### Class Overview ####
>No description provided
## Fields ##
### public float sliderValue ###
>#### Field Overview ####
>No description provided
>
>**default**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;null
>
### public boolean dragging ###
>#### Field Overview ####
>No description provided
>
>**default**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;null
>
## Constructors ##
### public SettingSlider (int, int, int, String, float) ###
>#### Constructor Overview ####
>Sets up a slider.
>
## Methods ##
### protected void mouseDragged (Minecraft, int, int) ###
>#### Method Overview ####
>Fired when the mouse button is dragged. Equivalent of
 MouseListener.mouseDragged(MouseEvent e).
>
### public boolean mousePressed (Minecraft, int, int) ###
>#### Method Overview ####
>Returns true if the mouse has been pressed on this control. Equivalent of
 MouseListener.mousePressed(MouseEvent e).
>
### public void mouseReleased (int, int) ###
>#### Method Overview ####
>Fired when the mouse button is released. Equivalent of
 MouseListener.mouseReleased(MouseEvent e).
>
### protected int getHoverState (boolean) ###
>#### Method Overview ####
>Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over
 this button and 2 if it IS hovering over this button.
>

---
Powered by [MDDocs](https://github.com/VRCube/MDDocs)