package pl.marianjureczko.poszukiwacz.activity.bluetooth

import androidx.lifecycle.ViewModel
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.XmlHelper

class BluetoothViewModel : ViewModel() {
    private val TAG = javaClass.simpleName
    private val xmlHelper = XmlHelper()

    lateinit var route: Route
    var thread: CancellableThread? = null

    fun setup(routeXml: String) {
        route = xmlHelper.loadFromString(routeXml)
    }
}