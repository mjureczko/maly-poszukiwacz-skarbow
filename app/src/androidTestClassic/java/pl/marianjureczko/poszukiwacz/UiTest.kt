package pl.marianjureczko.poszukiwacz

import pl.marianjureczko.poszukiwacz.screen.main.EDIT_ROUTE_BUTTON

open class UiTest: AbstractUITest() {

    fun goToTreasuresEditorScreen(routeName: String) {
        composeRule.waitForIdle()
        performTap(EDIT_ROUTE_BUTTON + routeName)
    }

}