package pl.marianjureczko.poszukiwacz.model

import com.ocadotechnology.gembus.test.CustomArranger
import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someObjects
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import java.io.File

class RouteArranger : CustomArranger<Route>() {
    companion object {
        fun savedWithTipFiles(storageHelper: StorageHelper): Route {
            val route = some<Route>()
            route.treasures.forEach { t ->
                t.instantiatePhotoFile(storageHelper).createNewFile()
                t.tipFileName = storageHelper.newSoundFile()
                File(t.tipFileName).createNewFile()
            }
            storageHelper.save(route)
            return route
        }

        fun routeWithoutTipFiles(): Route {
            val route = some<Route>()
            route.treasures.forEach { t ->
                t.photoFileName = null
                t.tipFileName = null
            }
            return route
        }
    }

    override fun instance(): Route {
        return Route(some<String>(), someObjects<TreasureDescription>(3).toMutableList())
    }
}