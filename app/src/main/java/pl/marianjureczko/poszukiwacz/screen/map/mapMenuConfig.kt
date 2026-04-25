package pl.marianjureczko.poszukiwacz.screen.map

import pl.marianjureczko.poszukiwacz.shared.GoToFacebook
import pl.marianjureczko.poszukiwacz.shared.GoToGallery
import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.ui.components.GoToBadgesScreen
import pl.marianjureczko.poszukiwacz.ui.components.MenuConfig
import pl.marianjureczko.poszukiwacz.ui.components.ViewModelProgressRestarter

fun mapMenuConfig(
    onClickOnGuide: GoToGuide,
    onClickOnFacebook: GoToFacebook,
    goToExportToGallery: GoToGallery,
    state: MapState,
    restarter: ViewModelProgressRestarter,
    onClickBadges: GoToBadgesScreen
) = MenuConfig(
    onClickOnGuide,
    onClickOnFacebook = { onClickOnFacebook(state.route.name) },
    onClickOnGallery = { goToExportToGallery(state.route.name) },
    onClickOnRestart = restarter,
    onClickBadges = onClickBadges
)
