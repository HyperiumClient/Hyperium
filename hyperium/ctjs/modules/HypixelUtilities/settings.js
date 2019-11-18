var HypixelUtilities = new SettingsObject("HypixelUtilities", [
  {
    name: "HypixelFriends",
    settings: [
      new Setting.Toggle("toggle", true),
      new Setting.Toggle("delete old page", true),
      new Setting.Toggle("auto accept", false),
	  new Setting.Toggle("ask to remove", true)
    ]
  }, {
    name: "CleanChat",
    settings: [
      new Setting.Toggle("PartyPlus", true),
      new Setting.Toggle("AllWhiteChat", false),
      new Setting.Toggle("BetterBreak", true)
    ]
  }
]);

HypixelUtilities.setCommand("hu").setSize(250, 75).setColor(0xfff9ac59);

Setting.register(HypixelUtilities);