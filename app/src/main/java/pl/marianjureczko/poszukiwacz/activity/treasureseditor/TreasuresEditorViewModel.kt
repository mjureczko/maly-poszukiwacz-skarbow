package pl.marianjureczko.poszukiwacz.activity.treasureseditor

import androidx.lifecycle.ViewModel
import pl.marianjureczko.poszukiwacz.model.Route

class TreasuresEditorViewModel : ViewModel() {
    private val TAG = javaClass.simpleName
    var route: Route = Route.nullObject()

    //TODO: check if survives process death
    var permissionToRecordAccepted = false
    var permissionToCapturePhotoAccepted = false
}