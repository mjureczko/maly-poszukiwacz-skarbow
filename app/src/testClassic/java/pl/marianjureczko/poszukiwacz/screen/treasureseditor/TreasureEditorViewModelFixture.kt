package pl.marianjureczko.poszukiwacz.screen.treasureseditor

import androidx.lifecycle.SavedStateHandle
import com.ocadotechnology.gembus.test.some
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import pl.marianjureczko.poszukiwacz.TestLocationPort
import pl.marianjureczko.poszukiwacz.TestStoragePort
import pl.marianjureczko.poszukiwacz.any
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.usecase.AddTreasureDescriptionToRoute
import pl.marianjureczko.poszukiwacz.usecase.RemoveTreasureDescriptionFromRoute

class TreasureEditorViewModelFixture(
    val route: Route
) {
    private val stateHandleMock = Mockito.mock<SavedStateHandle>()
    val locationPort = TestLocationPort()
    val storage = TestStoragePort()
    val routeName = route.name
    val viewModel: TreasureEditorViewModel
    val addTreasureDescriptionToRoute = AddTreasureDescriptionToRoute(storage)
    val removeTreasureDescriptionFromRoute = RemoveTreasureDescriptionFromRoute(storage)

    init {
        storage.routes[routeName] = route
        given(stateHandleMock.get<String>(any(String::class.java))).willReturn(routeName)
        viewModel = TreasureEditorViewModel(
            storage,
            stateHandleMock,
            locationPort,
            addTreasureDescriptionToRoute,
            removeTreasureDescriptionFromRoute
        )
    }

    fun setupProgress(treasureToSelect: TreasureDescription = route.treasures[0]) {
        val progress = some<TreasuresProgress> {
            routeName = route.name
            selectedTreasure = treasureToSelect
        }
        storage.progresses[route.name] = progress
    }
}