package pl.marianjureczko.poszukiwacz.screen.facebook

import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.ui.components.GoToBadgesScreen
import pl.marianjureczko.poszukiwacz.ui.components.MenuConfig

fun facebookMenuConfig(
    onClickOnGuide: GoToGuide,
    onClickBadges: GoToBadgesScreen
) = MenuConfig(onClickOnGuide)
