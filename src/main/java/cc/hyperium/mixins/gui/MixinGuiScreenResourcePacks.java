package cc.hyperium.mixins.gui;

import cc.hyperium.mixinsimp.gui.HyperiumGuiScreenResourcePacks;
import cc.hyperium.utils.ChatColor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.gui.GuiResourcePackAvailable;
import net.minecraft.client.gui.GuiResourcePackSelected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.ResourcePackListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KodingKing
 */
@Mixin(GuiScreenResourcePacks.class)
public class MixinGuiScreenResourcePacks extends GuiScreen {

  @Shadow
  private GuiResourcePackAvailable availableResourcePacksList;
  @Shadow
  private GuiResourcePackSelected selectedResourcePacksList;
  @Shadow
  private List<ResourcePackListEntry> availableResourcePacks;
  private HyperiumGuiScreenResourcePacks hyperiumGuiResourcePack = new HyperiumGuiScreenResourcePacks(
      (GuiScreenResourcePacks) (Object) this);

  private GuiResourcePackAvailable availablePacksClone;

  private GuiTextField searchField;

  /**
   * @reason Change buttons size
   * @author SiroQ
   */
  @Inject(method = "initGui", at = @At("RETURN"))
  public void initGui(CallbackInfo callbackInfo) {
    hyperiumGuiResourcePack.initGui(this.buttonList);

    this.availablePacksClone = this.availableResourcePacksList;
    this.searchField = new GuiTextField(3, fontRendererObj, this.width / 2 - 4 - 200,
        this.height - 24, 200, 20);
  }

  @Override
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    super.keyTyped(typedChar, keyCode);

    if (this.searchField != null) {
      this.searchField.textboxKeyTyped(typedChar, keyCode);
    }
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);

    if (this.searchField != null) {
      this.searchField.mouseClicked(mouseX, mouseY, mouseButton);
    }
  }

  @Override
  public void updateScreen() {
    super.updateScreen();

    if (this.searchField == null || this.searchField.getText().isEmpty()) {
      this.availableResourcePacksList = new GuiResourcePackAvailable(this.mc, 200, this.height,
          this.availableResourcePacks);
      this.availableResourcePacksList.setSlotXBoundsFromLeft(this.width / 2 - 4 - 200);
      this.availableResourcePacksList.registerScrollButtons(7, 8);
    } else {
      this.availableResourcePacksList = new GuiResourcePackAvailable(this.mc, 200, this.height,
          Arrays
              .asList(this.availablePacksClone.getList().stream().filter(resourcePackListEntry -> {
                try {
                  Method nameMethod = resourcePackListEntry.getClass()
                      .getDeclaredMethod("func_148312_b");
                  nameMethod.setAccessible(true);
                  String name = ChatColor
                      .stripColor((String) nameMethod.invoke(resourcePackListEntry))
                      .replaceAll("[^A-Za-z0-9 ]", "").trim().toLowerCase();
                  String text = searchField.getText().toLowerCase();

                  if (name.endsWith("zip")) {
                    name = name.subSequence(0, name.length() - 3).toString();
                  }

                  for (String s : text.split(" ")) {
                    if (!name.contains(s.toLowerCase())) {
                      return false;
                    }
                  }

                  return name.startsWith(text) || name.contains(text) || name
                      .equalsIgnoreCase(text);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                  e.printStackTrace();
                  return true;
                }
              }).toArray(ResourcePackListEntry[]::new)));
      this.availableResourcePacksList.setSlotXBoundsFromLeft(this.width / 2 - 4 - 200);
      this.availableResourcePacksList.registerScrollButtons(7, 8);
    }
  }

  /**
   * @reason Change text location
   * @author SiroQ
   */
  @Overwrite
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    this.drawBackground(0);
    this.availableResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
    this.selectedResourcePacksList.drawScreen(mouseX, mouseY, partialTicks);
    this.drawCenteredString(this.fontRendererObj, I18n.format("resourcePack.title"), this.width / 2,
        16, 16777215);
    this.drawCenteredString(this.fontRendererObj, I18n.format("resourcePack.folderInfo"),
        this.width / 2 - 102, this.height - 26, 8421504);

    this.searchField.drawTextBox();

    super.drawScreen(mouseX, mouseY, partialTicks);
  }

}
