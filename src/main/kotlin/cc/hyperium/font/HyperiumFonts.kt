package cc.hyperium.font

import java.awt.Font

object HyperiumFonts {
    val regular = HyperiumFontRenderer(
        Font.createFont(
            Font.TRUETYPE_FONT,
            javaClass.getResourceAsStream("/assets/hyperium/fonts/Roboto-Regular.ttf")
        ), 20f, 2
    )
    val medium = HyperiumFontRenderer(
        Font.createFont(
            Font.TRUETYPE_FONT,
            javaClass.getResourceAsStream("/assets/hyperium/fonts/Roboto-Medium.ttf")
        ), 20f, 2
    )
    val mediumSmall = HyperiumFontRenderer(
        Font.createFont(
            Font.TRUETYPE_FONT,
            javaClass.getResourceAsStream("/assets/hyperium/fonts/Roboto-Medium.ttf")
        ), 16f, 2
    )
    val bold = HyperiumFontRenderer(
        Font.createFont(
            Font.TRUETYPE_FONT,
            javaClass.getResourceAsStream("/assets/hyperium/fonts/Roboto-Bold.ttf")
        ), 20f, 2
    )
    val title = HyperiumFontRenderer(
        Font.createFont(
            Font.TRUETYPE_FONT,
            javaClass.getResourceAsStream("/assets/hyperium/fonts/Roboto-Bold.ttf")
        ), 48f, 2
    )
    val titleLarge = HyperiumFontRenderer(
        Font.createFont(
            Font.TRUETYPE_FONT,
            javaClass.getResourceAsStream("/assets/hyperium/fonts/Roboto-Bold.ttf")
        ), 84f, 2
    )
}
