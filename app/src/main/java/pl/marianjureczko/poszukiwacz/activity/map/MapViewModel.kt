package pl.marianjureczko.poszukiwacz.activity.map

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import pl.marianjureczko.poszukiwacz.model.Route

class MapViewModel(private val state: SavedStateHandle) : ViewModel() {
    lateinit var route: Route

    fun setup(route: Route) {
        this.route = route
    }
}
