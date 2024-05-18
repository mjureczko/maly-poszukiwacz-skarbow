package pl.marianjureczko.poszukiwacz.activity.facebook

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.HunterPath
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.StorageHelper

class FacebookViewModel(private val state: SavedStateHandle) : FacebookReportModel, ViewModel() {
    private val TAG = javaClass.simpleName
    override lateinit var progress: TreasuresProgress
        private set
    override var hunterPath: HunterPath? = null
        private set
    lateinit var elements: List<ElementDescription>
        private set

    override lateinit var route: Route

    fun initialize(context: Context, hunterPath: HunterPath?, progress: TreasuresProgress) {
        this.progress = progress
        this.hunterPath = hunterPath
        val elements = mutableListOf<ElementDescription>()
        elements.add(ElementDescription(0, Type.TREASURES_SUMMARY, true, context.getString(R.string.collected_treasures)))
        val treasure = context.getString(R.string.treasure)
        progress.commemorativePhotosByTreasuresDescriptionIds.forEach { (id, photo) ->
            elements.add(ElementDescription(0, Type.COMMEMORATIVE_PHOTO, true, "$treasure $id", orderNumber = id, photo = photo))
        }
        elements.add(ElementDescription(0, Type.MAP, true, context.getString(R.string.treasures_map)))
        elements.add(ElementDescription(0, Type.MAP_ROUTE, true, context.getString(R.string.route_on_map)))
        elements.add(ElementDescription(0, Type.MAP_SUMMARY, true, context.getString(R.string.treasures_map_summary)))
        this.route = StorageHelper(context).loadRoute(progress.routeName)
        this.elements = elements
    }

    fun getElement(position: Int): ElementDescription = elements[position]

    fun getElementsCount(): Int = elements.size

    override fun getSummaryElement(): ElementDescription = elements[0]

    override fun getCommemorativePhotoElements(): List<ElementDescription> = elements.filter { it.type == Type.COMMEMORATIVE_PHOTO }

    override fun getMap(): ElementDescription? = elements.find { it.type == Type.MAP }

    override fun getMapRoute(): ElementDescription? = elements.find { it.type == Type.MAP_ROUTE }

    override fun getMapSummary(): ElementDescription? = elements.find { it.type == Type.MAP_SUMMARY }
}