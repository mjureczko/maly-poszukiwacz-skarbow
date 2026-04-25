package pl.marianjureczko.poszukiwacz.screen.phototip

import pl.marianjureczko.poszukiwacz.shared.GoToFacebook
import pl.marianjureczko.poszukiwacz.shared.GoToGallery
import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.ui.components.GoToBadgesScreen
import pl.marianjureczko.poszukiwacz.ui.components.MenuConfig
import pl.marianjureczko.poszukiwacz.ui.components.ViewModelProgressRestarter

fun tipPhotoMenuConfig(
    onClickOnGuide: GoToGuide,
    onClickOnFacebook: GoToFacebook,
    goToExportToGallery: GoToGallery,
    state: TipPhotoState,
    restarter: ViewModelProgressRestarter,
    onClickBadges: GoToBadgesScreen
) = MenuConfig(
    onClickOnGuide,
    onClickOnFacebook = { onClickOnFacebook(state.routeName) },
    onClickOnGallery = { goToExportToGallery(state.routeName) },
    onClickOnRestart = restarter,
    onClickBadges = onClickBadges
)
