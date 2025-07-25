package pl.marianjureczko.poszukiwacz.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import pl.marianjureczko.poszukiwacz.shared.port.storage.StoragePort
import java.io.File
import java.io.Serializable

@Root
data class TreasureDescription(
    @field:Element(name = "id") var id: Int,
    @field:Element(name = "latitude") var latitude: Double,
    @field:Element(name = "longitude") var longitude: Double,
    @field:Element(required = false) var qrCode: String?,
    @field:Element(required = false) var tipFileName: String?,
    @field:Element(required = false) var photoFileName: String?,
    @field:Element(required = false) var movieFileName: String?,
    @field:Element(required = false) var subtitlesFileName: String?
) : Serializable {

    companion object{
        fun nullObject() = TreasureDescription()
    }

    constructor(id: Int, latitude: Double, longitude: Double) : this(id, latitude, longitude, null, null, null, null, null)
    constructor() : this(0, 0.0, 0.0, null, null, null, null, null)

    fun prettyName(): String = "[$id] $latitude $longitude"

    fun instantiatePhotoFile(storagePort: StoragePort): File {
        if (photoFileName == null) {
            photoFileName = storagePort.newPhotoFile()
        }
        return File(photoFileName)
    }

    fun hasPhoto(): Boolean {
        return photoFileName != null && File(photoFileName).exists()
    }
}