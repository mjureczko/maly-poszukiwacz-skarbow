//package pl.marianjureczko.poszukiwacz.activity.facebook
//
//import android.content.Context
//import androidx.lifecycle.SavedStateHandle
//import androidx.lifecycle.ViewModel
//import pl.marianjureczko.poszukiwacz.R
//import pl.marianjureczko.poszukiwacz.model.HunterPath
//import pl.marianjureczko.poszukiwacz.model.Route
//import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
//import pl.marianjureczko.poszukiwacz.shared.StorageHelper
//
//class FacebookViewModel(private val state: SavedStateHandle) : pl.marianjureczko.poszukiwacz.activity.facebook.n.FacebookReportModel, ViewModel() {
//    private val TAG = javaClass.simpleName
//    override lateinit var progress: TreasuresProgress
//        private set
//    override var hunterPath: HunterPath? = null
//        private set
//    lateinit var elements: List<pl.marianjureczko.poszukiwacz.activity.facebook.n.ElementDescription>
//
//    override lateinit var route: Route
//
//    fun initialize(context: Context, hunterPath: HunterPath?, progress: TreasuresProgress) {
//        this.progress = progress
//        this.hunterPath = hunterPath
//        val elements = mutableListOf<pl.marianjureczko.poszukiwacz.activity.facebook.n.ElementDescription>()
//        elements.add(
//            pl.marianjureczko.poszukiwacz.activity.facebook.n.ElementDescription(
//                0,
//                pl.marianjureczko.poszukiwacz.activity.facebook.n.Type.TREASURES_SUMMARY,
//                true,
//                context.getString(R.string.collected_treasures)
//            )
//        )
//        val treasure = context.getString(R.string.treasure)
//        progress.commemorativePhotosByTreasuresDescriptionIds.forEach { (id, photo) ->
//            elements.add(
//                pl.marianjureczko.poszukiwacz.activity.facebook.n.ElementDescription(
//                    0,
//                    pl.marianjureczko.poszukiwacz.activity.facebook.n.Type.COMMEMORATIVE_PHOTO,
//                    true,
//                    "$treasure $id",
//                    orderNumber = id,
//                    photo = photo
//                )
//            )
//        }
//        elements.add(
//            pl.marianjureczko.poszukiwacz.activity.facebook.n.ElementDescription(
//                0,
//                pl.marianjureczko.poszukiwacz.activity.facebook.n.Type.MAP,
//                true,
//                context.getString(R.string.treasures_map)
//            )
//        )
//        elements.add(
//            pl.marianjureczko.poszukiwacz.activity.facebook.n.ElementDescription(
//                0,
//                pl.marianjureczko.poszukiwacz.activity.facebook.n.Type.MAP_ROUTE,
//                true,
//                context.getString(R.string.route_on_map)
//            )
//        )
//        elements.add(
//            pl.marianjureczko.poszukiwacz.activity.facebook.n.ElementDescription(
//                0,
//                pl.marianjureczko.poszukiwacz.activity.facebook.n.Type.MAP_SUMMARY,
//                true,
//                context.getString(R.string.treasures_map_summary)
//            )
//        )
//        this.route = StorageHelper(context).loadRoute(progress.routeName)
//        this.elements = elements
//    }
//
//    fun getElement(position: Int): pl.marianjureczko.poszukiwacz.activity.facebook.n.ElementDescription = elements[position]
//
//    fun getElementsCount(): Int = elements.size
//
//    override fun getSummaryElement(): pl.marianjureczko.poszukiwacz.activity.facebook.n.ElementDescription = elements[0]
//
//    override fun getCommemorativePhotoElements(): List<pl.marianjureczko.poszukiwacz.activity.facebook.n.ElementDescription> = elements.filter { it.type == pl.marianjureczko.poszukiwacz.activity.facebook.n.Type.COMMEMORATIVE_PHOTO }
//
//    override fun getMap(): pl.marianjureczko.poszukiwacz.activity.facebook.n.ElementDescription? = elements.find { it.type == pl.marianjureczko.poszukiwacz.activity.facebook.n.Type.MAP }
//
//    override fun getMapRoute(): pl.marianjureczko.poszukiwacz.activity.facebook.n.ElementDescription? = elements.find { it.type == pl.marianjureczko.poszukiwacz.activity.facebook.n.Type.MAP_ROUTE }
//
//    override fun getMapSummary(): pl.marianjureczko.poszukiwacz.activity.facebook.n.ElementDescription? = elements.find { it.type == pl.marianjureczko.poszukiwacz.activity.facebook.n.Type.MAP_SUMMARY }
//}