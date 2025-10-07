package pl.marianjureczko.poszukiwacz.screen.commemorative

import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.ui.components.GoToBadgesScreen
import pl.marianjureczko.poszukiwacz.ui.components.MenuConfig

fun commemorativeMenuConfig(
    onClickOnGuide: GoToGuide,
    onClickFacebook: () -> Unit,
    onClickBadges: GoToBadgesScreen
) = MenuConfig(onClickOnGuide, onClickFacebook)
