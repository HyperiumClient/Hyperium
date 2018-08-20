package cc.hyperium.mods.tabtoggle;

import cc.hyperium.Hyperium;
import cc.hyperium.event.EventBus;
import cc.hyperium.mods.AbstractMod;

public class TabToggleMod extends AbstractMod {

  @Override
  public AbstractMod init() {
    EventBus.INSTANCE.register(new TabToggleEventListener());

    Hyperium.CONFIG.register(new TabToggleSettings());

    return this;
  }

  @Override
  public Metadata getModMetadata() {
    return new Metadata(this, "Tab Toggle", "1.0", "KodingKing");
  }
}
