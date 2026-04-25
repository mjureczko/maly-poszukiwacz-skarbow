package pl.marianjureczko.poszukiwacz.screen.result

import pl.marianjureczko.poszukiwacz.screen.searching.ResultSharedViewModel
import pl.marianjureczko.poszukiwacz.shared.GoToFacebook
import pl.marianjureczko.poszukiwacz.shared.GoToGallery
import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.ui.components.GoToBadgesScreen
import pl.marianjureczko.poszukiwacz.ui.components.MenuConfig
import pl.marianjureczko.poszukiwacz.ui.components.ViewModelProgressRestarter

fun resultsMenuConfig(
    onClickOnGuide: GoToGuide,
    onClickOnFacebook: GoToFacebook,
    goToExportToGallery: GoToGallery,
    sharedViewModel: ResultSharedViewModel,
    restarter: ViewModelProgressRestarter,
    onClickBadges: GoToBadgesScreen
) = MenuConfig(
    onClickOnGuide,
    onClickOnFacebook = { onClickOnFacebook(sharedViewModel.getRouteName()) },
    onClickOnGallery = { goToExportToGallery(sharedViewModel.getRouteName()) },
    onClickOnRestart = restarter,
    onClickBadges = onClickBadges,
)