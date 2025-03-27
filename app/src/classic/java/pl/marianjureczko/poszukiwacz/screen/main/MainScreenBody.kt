package pl.marianjureczko.poszukiwacz.screen.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.icons.twotone.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.screen.bluetooth.Mode
import pl.marianjureczko.poszukiwacz.shared.GoToBluetooth
import pl.marianjureczko.poszukiwacz.shared.GoToSearching
import pl.marianjureczko.poszukiwacz.shared.GoToTreasureEditor
import pl.marianjureczko.poszukiwacz.ui.components.AdvertBanner
import pl.marianjureczko.poszukiwacz.ui.components.EmbeddedButton
import pl.marianjureczko.poszukiwacz.ui.components.EnterTextDialog
import pl.marianjureczko.poszukiwacz.ui.components.LargeButton
import pl.marianjureczko.poszukiwacz.ui.components.YesNoDialog

const val NEW_ROUTE_BUTTON = "New route"
const val CONFIRM_ROUTE_NAME_BUTTON = "Confirm route name"
const val ENTER_ROUTE_NAME_TITLE = "Enter route name"
const val ROUTE_NAME_TEXT_EDIT = "Edit route name"
const val EDIT_ROUTE_BUTTON = "Edit route button"
const val DELETE_ROUTE_BUTTON = "Delete route button"
const val ROUTE = "Route"

private const val NO_ROUTE = "no_route"

@Composable
fun MainScreenBody(
    goToBluetooth: GoToBluetooth,
    goToTreasureEditor: GoToTreasureEditor,
    goToSearching: GoToSearching
) {
    val viewModel: MainViewModel = hiltViewModel()
    val state = viewModel.state.value
    LaunchedEffect(Unit) {
        viewModel.refresh()
    }
    Column {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
            modifier = Modifier.weight(0.99f)
        ) {
            items(state.routes) { route ->
                RouteItem(route, viewModel, goToTreasureEditor, goToSearching, goToBluetooth)
            }
        }
        Spacer(modifier = Modifier.weight(0.01f))
        LargeButton(R.string.new_route_button, description = NEW_ROUTE_BUTTON) {
            viewModel.openNewRouteDialog()
        }
        LargeButton(R.string.route_from_bluetooth_button, onClick = { goToBluetooth(Mode.ACCEPTING, NO_ROUTE) })
        EnterTextDialog(
            visible = state.newRoute.showDialog,
            hideIt = { viewModel.hideNewRouteDialog() },
            title = R.string.route_name_prompt,
            buttonDescription = CONFIRM_ROUTE_NAME_BUTTON,
            textFieldDescription = ROUTE_NAME_TEXT_EDIT,
            titleDescription = ENTER_ROUTE_NAME_TITLE
        ) { routeName -> viewModel.createNewRouteByName(routeName, goToTreasureEditor) }
        YesNoDialog(
            state.showOverrideRouteDialog,
            { viewModel.hideOverrideRouteDialog() },
            R.string.overwritting_route
        ) {
            viewModel.replaceRouteWithNewOne(state.newRoute.routeName, goToTreasureEditor)
        }
        YesNoDialog(
            state = viewModel.state.value.deleteConfirmation.showDialog,
            hideIt = { viewModel.hideConfirmDeleteDialog() },
            titleString = viewModel.state.value.deleteConfirmation.confirmationPrompt,
            onYes = {
                viewModel.state.value.deleteConfirmation.deleteCandidate?.let { route ->
                    viewModel.deleteRoute(route)
                }
            }
        )
        AdvertBanner()
    }
}

@Composable
fun RouteItem(
    item: Route,
    viewModel: MainViewModel,
    goToTreasureEditor: GoToTreasureEditor,
    goToSearching: GoToSearching,
    goToBluetooth: GoToBluetooth,
) {
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .padding(4.dp)
            .clickable { goToSearching(item.name) }
            .semantics { contentDescription = "$ROUTE ${item.name}" }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
            )
            EmbeddedButton(
                imageVector = Icons.TwoTone.Edit,
                description = EDIT_ROUTE_BUTTON + item.name
            ) { goToTreasureEditor(item.name) }
            EmbeddedButton(imageVector = Icons.TwoTone.Share) { goToBluetooth(Mode.SENDING, item.name) }
            EmbeddedButton(
                imageVector = Icons.TwoTone.Delete,
                description = DELETE_ROUTE_BUTTON + item.name
            ) {
                viewModel.openConfirmDeleteDialog(item)
            }
        }
    }
}