package pl.marianjureczko.poszukiwacz

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Test
import pl.marianjureczko.poszukiwacz.activity.searching.n.COMPASS
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.screen.main.CONFIRM_ROUTE_NAME_BUTTON
import pl.marianjureczko.poszukiwacz.screen.main.DELETE_ROUTE_BUTTON
import pl.marianjureczko.poszukiwacz.screen.main.EDIT_ROUTE_BUTTON
import pl.marianjureczko.poszukiwacz.screen.main.ENTER_ROUTE_NAME_TITLE
import pl.marianjureczko.poszukiwacz.screen.main.NEW_ROUTE_BUTTON
import pl.marianjureczko.poszukiwacz.screen.main.ROUTE_NAME_TEXT_EDIT
import pl.marianjureczko.poszukiwacz.screen.treasureseditor.TREASURE_ITEM_ROW
import pl.marianjureczko.poszukiwacz.screen.treasureseditor.TREASURE_ITEM_TEXT
import pl.marianjureczko.poszukiwacz.shared.di.PortsModule
import pl.marianjureczko.poszukiwacz.ui.components.TOPBAR_SCREEN_TITLE
import pl.marianjureczko.poszukiwacz.ui.components.YES_BUTTON
import java.time.LocalDateTime
import java.time.ZoneOffset

//Prepared for Pixel 6a API 34
@UninstallModules(PortsModule::class)
@HiltAndroidTest
class MainScreenTest : UiTest() {

    @Test
    fun shouldGoToTreasureEditorScreen_whenCreatingNewRoute() {
        //given
        val button: SemanticsNodeInteraction = getNode(NEW_ROUTE_BUTTON)

        //then
        button.assertTextEquals(context.getString(R.string.new_route_button))
        button.performClick()
        composeRule.waitForIdle()

        val dialogTitle = getNode(ENTER_ROUTE_NAME_TITLE)
        dialogTitle.assertTextEquals(context.getString(R.string.route_name_prompt))

        val nameInput = getNode(ROUTE_NAME_TEXT_EDIT)
        val routeName = "TEST_" + LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        nameInput.assertExists()
            .performTextInput(routeName)
        composeRule.waitForIdle()

        performTap(CONFIRM_ROUTE_NAME_BUTTON)

        getNode(TOPBAR_SCREEN_TITLE)
            .assertTextEquals("${context.getString(R.string.route)} $routeName")
        getNode(TREASURE_ITEM_ROW)
            .assertDoesNotExist()
    }

    @Test
    fun shouldGoToTreasureEditorScreen_whenClickOnEditButton() {
        //given
//
//        composeRule.setContent {
//            // Your composable function that renders the screen
//            MainScreen { }()
//        }

        //when
        goToTreasuresEditorScreen(route!!.name)

        //then
        val screenTitle: SemanticsNodeInteraction = getNode(TOPBAR_SCREEN_TITLE)
        screenTitle.assertTextEquals("${context.getString(R.string.route)} ${route!!.name}")
        route!!.treasures.forEach { treasure ->
            val treasureText: SemanticsNodeInteraction = getNode("$TREASURE_ITEM_TEXT ${treasure.id}")
            treasureText.assertTextEquals(treasure.prettyName())
        }
    }

    @Test
    fun shouldRemoveRoute_whenClickingRemoveButton() {
        //given
        composeRule.waitForIdle()

        //when
        performTap(DELETE_ROUTE_BUTTON + route!!.name)
        performTap(YES_BUTTON)

        //then
        getNode(EDIT_ROUTE_BUTTON + route!!.name)
            .assertDoesNotExist()
    }

    @Test
    fun shouldGoToSearching_whenTapOnRoute() {
        //given
        val route: Route = super.route!!

        //when
        goToSearchingScreen(route)

        //then
        getNode(COMPASS).assertExists()
    }
}