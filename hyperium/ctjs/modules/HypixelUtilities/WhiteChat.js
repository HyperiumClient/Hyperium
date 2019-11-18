register("chat", function(name, message, event) {
	if (!HypixelUtilities.getSetting("CleanChat", "AllWhiteChat")) return;

	if (name.endsWith("&r&7")) {
		event.setCanceled(true);
		ChatLib.chat(name + "&r&f: " + ChatLib.removeFormatting(message));
	}
}).setCriteria("&r${name}: ${message}&r");
