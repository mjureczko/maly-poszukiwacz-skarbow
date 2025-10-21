package pl.marianjureczko.poszukiwacz.screen.main

import pl.marianjureczko.poszukiwacz.ui.components.GoToBadgesScreen
import pl.marianjureczko.poszukiwacz.ui.components.MenuConfig

fun mainMenuConfig(
    onClickOnGuide: () -> Unit,
    onClickBadges: GoToBadgesScreen,
): MenuConfig {
    return MenuConfig(onClickOnGuide, onClickBadges = onClickBadges)
}