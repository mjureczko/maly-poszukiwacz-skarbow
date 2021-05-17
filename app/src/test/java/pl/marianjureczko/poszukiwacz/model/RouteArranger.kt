package pl.marianjureczko.poszukiwacz.model

import com.ocadotechnology.gembus.test.CustomArranger
import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someObjects
import com.ocadotechnology.gembus.test.someText
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import java.io.File

class RouteArranger : CustomArranger<Route>() {
    companion object {
        fun savedWithFiles(storageHelper: StorageHelper): Route {
            val route = some<Route>()
            route.treasures.forEach { t ->
                t.instantiatePhotoFile(storageHelper).createNewFile()
                t.tipFileName = storageHelper.newSoundFile()
                File(t.tipFileName).createNewFile()
            }
            storageHelper.save(route)
            return route
        }
    }

    override fun instance(): Route {
        return Route(someText(), someObjects<TreasureDescription>(3).toMutableList())
    }
}