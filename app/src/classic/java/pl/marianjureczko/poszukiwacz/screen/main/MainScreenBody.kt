package pl.marianjureczko.poszukiwacz.screen.main

import android.widget.Toast
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.DeleteRoute
import pl.marianjureczko.poszukiwacz.shared.GoToSearching
import pl.marianjureczko.poszukiwacz.ui.components.AdvertBanner
import pl.marianjureczko.poszukiwacz.ui.components.EmbeddedButton
import pl.marianjureczko.poszukiwacz.ui.components.EnterTextDialog
import pl.marianjureczko.poszukiwacz.ui.components.LargeButton
import pl.marianjureczko.poszukiwacz.ui.components.YesNoDialog

@Composable
fun MainScreenBody(goToSearching: GoToSearching) {
    val viewModel: MainViewModel = hiltViewModel()
    val state = viewModel.state.value
    Column(/*Modifier.background(PrimaryBackground)*/) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
            modifier = Modifier.weight(0.99f)
        ) {
            items(state.routes) { route ->
                RouteItem(route, viewModel, { viewModel.deleteRoute(route) }, goToSearching)
            }
        }
        Spacer(modifier = Modifier.weight(0.01f))
        LargeButton(R.string.new_route_button) {
            viewModel.openNewRouteDialog()
        }
        val context = LocalContext.current
        LargeButton(R.string.route_from_bluetooth_button) {
            Toast.makeText(context, "Clicked Bluetooth", Toast.LENGTH_SHORT).show()
        }
        EnterTextDialog(
            state = state.showNewRouteDialog,
            hideIt = { viewModel.hideNewRouteDialog() },
            title = R.string.route_name_prompt
        ) { routeName -> viewModel.createNewRouteByName(routeName) }
        YesNoDialog(
            state.showOverrideRouteDialog,
            { viewModel.hideOverrideRouteDialog() },
            R.string.overwritting_route
        ) {
            viewModel.replaceRouteWithNewOne(state.newRouteName)
        }
        AdvertBanner()
    }
}

@Composable
fun RouteItem(item: Route, viewModel: MainViewModel, onDelete: DeleteRoute, goToSearching: GoToSearching) {
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .padding(4.dp)
            .clickable { goToSearching(item.name) }
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
            EmbeddedButton(Icons.TwoTone.Edit) { print("TODO") }
            EmbeddedButton(Icons.TwoTone.Share) { print("TODO") }
            EmbeddedButton(Icons.TwoTone.Delete) { viewModel.openConfirmDeleteDialog() }

            YesNoDialog(
                state = viewModel.state.value.showConfirmDeleteDialog,
                hideIt = { viewModel.hideConfirmDeleteDialog() },
                title = R.string.overwritting_route,
                onYes = onDelete
            )
        }
    }
}