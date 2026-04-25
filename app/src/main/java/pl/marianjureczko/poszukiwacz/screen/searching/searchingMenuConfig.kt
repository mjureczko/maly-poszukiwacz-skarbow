package pl.marianjureczko.poszukiwacz.screen.searching

import pl.marianjureczko.poszukiwacz.shared.GoToFacebook
import pl.marianjureczko.poszukiwacz.shared.GoToGallery
import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.ui.components.GoToBadgesScreen
import pl.marianjureczko.poszukiwacz.ui.components.MenuConfig
import pl.marianjureczko.poszukiwacz.ui.components.ViewModelProgressRestarter

fun searchingMenuConfig(
    onClickOnGuide: GoToGuide,
    goToFacebook: GoToFacebook,
    goToExportToGallery: GoToGallery,
    state: SharedState,
    restarter: ViewModelProgressRestarter,
    onClickBadges: GoToBadgesScreen
) = MenuConfig(
    onClickOnGuide,
    onClickOnFacebook = { goToFacebook(state.route.name) },
    onClickOnGallery = { goToExportToGallery(state.route.name) },
    onClickOnRestart = restarter,
    onClickBadges = onClickBadges
)
