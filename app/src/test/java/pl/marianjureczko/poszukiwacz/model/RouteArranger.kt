package pl.marianjureczko.poszukiwacz.model

import com.ocadotechnology.gembus.test.CustomArranger
import com.ocadotechnology.gembus.test.someObjects
import com.ocadotechnology.gembus.test.someText

class RouteArranger : CustomArranger<Route>() {
    override fun instance(): Route {
        return Route(someText(), someObjects<TreasureDescription>(3).toMutableList())
    }
}