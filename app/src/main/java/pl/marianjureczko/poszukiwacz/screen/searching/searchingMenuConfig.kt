package pl.marianjureczko.poszukiwacz.screen.searching

import pl.marianjureczko.poszukiwacz.shared.GoToFacebook
import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.ui.components.GoToBadgesScreen
import pl.marianjureczko.poszukiwacz.ui.components.MenuConfig
import pl.marianjureczko.poszukiwacz.ui.components.ViewModelProgressRestarter

fun searchingMenuConfig(
    onClickOnGuide: GoToGuide,
    goToFacebook: GoToFacebook,
    state: SharedState,
    restarter: ViewModelProgressRestarter,
    onClickBadges: GoToBadgesScreen
) = MenuConfig(onClickOnGuide, { goToFacebook(state.route.name) }, restarter, onClickBadges = onClickBadges)
