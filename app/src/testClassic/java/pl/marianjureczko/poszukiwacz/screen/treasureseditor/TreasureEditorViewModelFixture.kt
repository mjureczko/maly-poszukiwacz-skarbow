package pl.marianjureczko.poszukiwacz.screen.treasureseditor

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.ocadotechnology.gembus.test.some
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import pl.marianjureczko.poszukiwacz.TestStoragePort
import pl.marianjureczko.poszukiwacz.any
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.PhotoHelper
import pl.marianjureczko.poszukiwacz.shared.port.CameraPort
import pl.marianjureczko.poszukiwacz.usecase.AddTreasureDescriptionToRoute
import pl.marianjureczko.poszukiwacz.usecase.RemoveTreasureDescriptionFromRoute

class TreasureEditorViewModelFixture(
    val route: Route,
    val photoHelper: PhotoHelper = mock(PhotoHelper::class.java)
) {
    val stateHandleMock = mock<SavedStateHandle>()
    val locationPort = pl.marianjureczko.poszukiwacz.TestLocationPort()
    val storage = TestStoragePort(mock<Context>())
    val routeName = route.name
    val viewModel: TreasureEditorViewModel
    val addTreasureDescriptionToRoute = AddTreasureDescriptionToRoute(storage)
    val removeTreasureDescriptionFromRoute = RemoveTreasureDescriptionFromRoute(storage)
    val cameraPort = mock(CameraPort::class.java)

    init {
        storage.routes[routeName] = route
        given(stateHandleMock.get<String>(any(String::class.java))).willReturn(routeName)
        viewModel = initializeViewModel()
    }

    fun initializeViewModel(): TreasureEditorViewModel {
        return TreasureEditorViewModel(
            storage,
            stateHandleMock,
            locationPort,
            cameraPort,
            photoHelper,
            addTreasureDescriptionToRoute,
            removeTreasureDescriptionFromRoute
        )
    }

    fun setupProgress(treasureToSelect: TreasureDescription = route.treasures[0]) {
        val progress = some<TreasuresProgress> {
            routeName = route.name
            selectedTreasureDescriptionId = treasureToSelect.id
        }
        storage.progresses[route.name] = progress
    }
}