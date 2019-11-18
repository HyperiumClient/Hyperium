register("chat", function(name) {
	if (!HypixelUtilities.getSetting("HypixelFriends", "auto accept")) return;

	ChatLib.say("/f accept " + ChatLib.removeFormatting(name).replace(/\[.+]/g, "").trim());
}).setCriteria("&r&eFriend request from &r${name}&r&9\n&r&a&l").setParameter("contains");