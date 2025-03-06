package pl.marianjureczko.poszukiwacz.model

import com.ocadotechnology.gembus.test.CustomArranger
import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someObjects
import com.ocadotechnology.gembus.test.someString
import pl.marianjureczko.poszukiwacz.shared.port.StorageHelper
import java.io.File

class RouteArranger : CustomArranger<Route>() {
    companion object {

        fun saveWithTreasureDescription(treasureDescription: TreasureDescription, storageHelper: StorageHelper): Route {
            val route = Route(someString(), mutableListOf(treasureDescription))
            storageHelper.save(route)
            return route
        }

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

        fun withNameAndTreasureWithQrCode(name: String, qrCode: String): Route {
            val route = some<Route>().copy(name = name)
            route.treasures.first().qrCode = qrCode
            return route
        }
    }

    override fun instance(): Route {
        return Route(some<String>(), someObjects<TreasureDescription>(3).toMutableList())
    }
}