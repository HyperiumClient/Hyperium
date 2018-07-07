package cc.hyperium.mixins.gui;

import cc.hyperium.utils.JsonHolder;
import net.minecraft.client.gui.GuiIngameMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiIngameMenu.class)
public interface MixinGuiIngameMenu2 {
  @Accessor
  void setBaseAngle(int baseAngle);
  @Accessor
  void setLastUpdate(long lastUpdate);
  @Accessor
  void setData(JsonHolder data);
}
