package pl.marianjureczko.poszukiwacz.activity.facebook.n

import pl.marianjureczko.poszukiwacz.activity.facebook.ElementDescription
import pl.marianjureczko.poszukiwacz.activity.facebook.FacebookReportModel
import pl.marianjureczko.poszukiwacz.activity.facebook.Type
import pl.marianjureczko.poszukiwacz.model.HunterPath
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import java.util.Random


//TODO: chack if facebook works when no path data is persisted yet
data class FacebookState(
    override val hunterPath: HunterPath?,
    override val progress: TreasuresProgress,
    override val route: Route,
    val elements: List<ElementDescription>,
    val recompose: Int = Random().nextInt()
) : FacebookReportModel {
    override fun getSummaryElement(): ElementDescription {
        return elements[0]
    }

    override fun getCommemorativePhotoElements(): List<ElementDescription> {
        return elements.filter { it.type == Type.COMMEMORATIVE_PHOTO }
    }

    override fun getMap(): ElementDescription? {
        return elements.find { it.type == Type.MAP }
    }

    override fun getMapSummary(): ElementDescription? {
        return elements.find { it.type == Type.MAP_SUMMARY }
    }

    override fun getMapRoute(): ElementDescription? {
        return elements.find { it.type == Type.MAP_ROUTE }
    }
}