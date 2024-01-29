package pl.marianjureczko.poszukiwacz.activity.result

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.commemorative.CommemorativeInputData
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.StorageHelper

class ResultActivityViewModel(private val state: SavedStateHandle) : ViewModel() {

    companion object {
        const val ERROR_MSG = "errorMsg"
        const val PROGRESS = "progress"
    }

    var treasure: Treasure? = null
        private set
    var progress: TreasuresProgress?
        get() = state.get<TreasuresProgress?>(PROGRESS)
        private set(value) = state.set(PROGRESS, value)
    private var currentTreasureDescription: TreasureDescription? = null
    var errorMsg: String?
        get() = state.get<String>(ERROR_MSG)
        private set(value) = state.set(ERROR_MSG, value)

    /**
     * Checks what kind of treasure was found and adds it to the progress if it's a mew one.
     * If there nothing to add, sets an appropriate error message in errMsg.
     */
    fun initialize(
        app: AppCompatActivity,
        storageHelper: StorageHelper,
        treasure: Treasure?,
        progress: TreasuresProgress?,
        currentTreasureDescription: TreasureDescription?
    ) {
        this.treasure = treasure
        this.progress = progress
        this.currentTreasureDescription = currentTreasureDescription

        if (treasure == null) {
            errorMsg = app.resources.getString(R.string.not_a_treasure_msg)
        } else {
            val p = this.progress
            p?.let {
                if (it.contains(treasure)) {
                    errorMsg = app.resources.getString(R.string.treasure_already_taken_msg)
                } else {
                    it.collect(treasure)
                    if (currentTreasureDescription != null) {
                        it.collect(currentTreasureDescription)
                    }
                    // to save the updated progress in the quasi persistent state
                    this.progress = it
                    storageHelper.save(it)
                }
            }
        }
    }

    fun addCommemorativePhoto(storageHelper: StorageHelper, commemorativePhotoAbsolutePath: String) {
        currentTreasureDescription?.let {
            progress?.let {
                it.addCommemorativePhoto(currentTreasureDescription!!, commemorativePhotoAbsolutePath)
                storageHelper.save(it)
                // to save the updated progress in the quasi persistent state
                progress = it
            }
        }
    }

    fun isError(): Boolean = this.errorMsg != null

    fun canMakeCommemorativePhoto(): Boolean = !isError() && currentTreasureDescription != null

    fun currentTreasureHasCommemorativePhoto(): Boolean =
        if (progress != null && currentTreasureDescription != null) {
            progress!!.getCommemorativePhoto(currentTreasureDescription!!) != null
        } else {
            false
        }

    fun commemorativeInputData(): CommemorativeInputData? {
        if (!currentTreasureHasCommemorativePhoto()) {
            return null
        }
        return CommemorativeInputData(
            progress!!.getCommemorativePhoto(currentTreasureDescription!!)!!,
            progress!!
        )

    }

    fun activityResult(): Intent {
        val data = Intent()
        progress?.let {
            data.putExtra(
                ResultActivity.RESULT_OUT,
                ResultActivityOutput(progress, currentTreasureDescription, !isError())
            )
        }
        return data
    }
}