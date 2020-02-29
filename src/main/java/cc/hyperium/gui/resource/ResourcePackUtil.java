package cc.hyperium.gui.resource;

import cc.hyperium.utils.ChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiResourcePackAvailable;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.ResourcePackListEntry;

import java.util.Arrays;
import java.util.List;

public class ResourcePackUtil {


  public static GuiResourcePackAvailable updateList(GuiTextField searchField,
      GuiResourcePackAvailable availablePacksClone,
      List<ResourcePackListEntry> availableResourcePacks, Minecraft mc, int width, int height) {
    GuiResourcePackAvailable availableResourcePacksList;
    if (searchField == null || searchField.getText().isEmpty()) {
      availableResourcePacksList = new GuiResourcePackAvailable(mc, 200, height,
          availableResourcePacks);
      availableResourcePacksList.setSlotXBoundsFromLeft(width / 2 - 4 - 200);
      availablePacksClone.registerScrollButtons(7, 8);
    } else {
      availableResourcePacksList = new GuiResourcePackAvailable(mc, 200, height,
          Arrays.asList(availablePacksClone.getList().stream()
              .filter(resourcePackListEntry -> {
                try {
                  String name = ChatColor.stripColor(resourcePackListEntry.func_148312_b().
                      replaceAll("[^A-Za-z0-9 ]", "").trim().toLowerCase());
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
                } catch (Exception e) {
                  e.printStackTrace();
                  return true;
                }
              }).toArray(ResourcePackListEntry[]::new)));

      availableResourcePacksList.setSlotXBoundsFromLeft(width / 2 - 4 - 200);
      availableResourcePacksList.registerScrollButtons(7, 8);
    }

    return availableResourcePacksList;
  }
}
