// better break
register("chat", betterBreak)
	.setCriteria("${color}-----------------------------------------------------&r")
	.triggerIfCanceled(false);
register("chat", betterBreak)
	.setCriteria("${color}\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC\u25AC&r")
	.triggerIfCanceled(false);

function betterBreak(color, event) {
	// check setting
	if (!HypixelUtilities.getSetting("CleanChat", "BetterBreak")) return;

	// check to make sure color is a color code
	if (color.startsWith("&")
	&& (color.length == 2 || color.length == 4 || color.length == 6)) {
		// get rid of bold in color
		color = color.replaceAll("&l", "");

		cancel(event);

		if (color == "&9") {
			new Message(color + "&m" + ChatLib.getChatBreak("-"))
				.setChatLineId(HypixelFriends.getId()).chat();
		} else {
			// any other color
			ChatLib.chat(color + "&m" + ChatLib.getChatBreak("-"));
		}
	}
}