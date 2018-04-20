[< Back](../README.md)
# MixinRenderItem #
>#### Class Overview ####
>A Mixin to the RenderItem class to provide ShinyPots support, not to be confused with the
 ItemRenderer class, this class is entirely different
## Constructors ##
### public MixinRenderItem () ###
>#### Constructor Overview ####
>No description provided
>
## Methods ##
### public void renderItem (ItemStack, IBakedModel) ###
>#### Method Overview ####
>Overrides the normal method to use our custom one
>
>### Parameters ###
>**stack**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the item to render
>
>**model**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the model of the item
>
### public void renderItem (ItemStack, IBakedModel, boolean) ###
>#### Method Overview ####
>A custom method which includes a "isInv" parameter, this specifies if the item being rendered
 is in an inventory
>
>### Parameters ###
>**stack**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the item we are rendering
>
>**model**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the model of the item we will use
>
>**isInv**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;true if the item is being rendered in an inventory
>
### public void renderItemIntoGUI (ItemStack, int, int) ###
>#### Method Overview ####
>Overrides the normal gui renderer to use our custom renderer instead
>
>### Parameters ###
>**stack**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the item to render
>
>**x**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the x location of the item
>
>**y**<br />
>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;the y location of the item
>
### protected abstract void renderModel (IBakedModel, int) ###
>#### Method Overview ####
>A shadow method, this will call the method in the the class we are modifying
>
### protected abstract void renderModel (IBakedModel, ItemStack) ###
>#### Method Overview ####
>A shadow method, this will call the method in the the class we are modifying
>
### protected abstract void setupGuiTransform (int, int, boolean) ###
>#### Method Overview ####
>A shadow method, this will call the method in the the class we are modifying
>

---
Powered by [MDDocs](https://github.com/VRCube/MDDocs)