package pl.marianjureczko.poszukiwacz.screen.treasureselector

import pl.marianjureczko.poszukiwacz.screen.searching.SelectorSharedState
import pl.marianjureczko.poszukiwacz.shared.GoToFacebook
import pl.marianjureczko.poszukiwacz.shared.GoToGallery
import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.ui.components.GoToBadgesScreen
import pl.marianjureczko.poszukiwacz.ui.components.MenuConfig
import pl.marianjureczko.poszukiwacz.ui.components.ViewModelProgressRestarter

fun selectorMenuConfig(
    onClickOnGuide: GoToGuide,
    onClickOnFacebook: GoToFacebook,
    goToExportToGallery: GoToGallery,
    sharedState: SelectorSharedState,
    restarter: ViewModelProgressRestarter,
    onClickBadges: GoToBadgesScreen
) = MenuConfig(
    onClickOnGuide,
    onClickOnFacebook = { onClickOnFacebook(sharedState.route.name) },
    onClickOnGallery = { goToExportToGallery(sharedState.route.name) },
    onClickOnRestart = restarter,
    onClickBadges = onClickBadges
)
