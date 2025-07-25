package pl.marianjureczko.poszukiwacz.usecase

import androidx.compose.runtime.MutableState
import pl.marianjureczko.poszukiwacz.model.HunterPath
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.screen.searching.SharedState
import pl.marianjureczko.poszukiwacz.shared.port.storage.StoragePort

class ResetProgressUC(
    private val storage: StoragePort
) {
    operator fun invoke(state: MutableState<SharedState>) {
        state.value.treasuresProgress.commemorativePhotosByTreasuresDescriptionIds.values.forEach {
            storage.removeFile(it)
        }
        val restartedProgress = TreasuresProgress().copy(
            routeName = state.value.treasuresProgress.routeName,
            selectedTreasureDescriptionId = state.value.treasuresProgress.selectedTreasureDescriptionId
        )
        state.value = state.value.copy(
            treasuresProgress = restartedProgress,
        )
        storage.save(restartedProgress)
        val emptyHunterPath = HunterPath(state.value.route.name)
        state.value = state.value.copy(
            hunterPath = emptyHunterPath,
        )
        storage.save(emptyHunterPath)
    }
}