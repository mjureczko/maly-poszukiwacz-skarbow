package pl.marianjureczko.poszukiwacz.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import java.io.File
import java.io.Serializable

@Root
data class TreasureDescription(
    @field:Element var id: Int,
    @field:Element var latitude: Double,
    @field:Element var longitude: Double,
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

    fun instantiatePhotoFile(storageHelper: StorageHelper): File {
        if (photoFileName == null) {
            photoFileName = storageHelper.newPhotoFile()
        }
        return File(photoFileName)
    }

    fun instantiateSoundFile(storageHelper: StorageHelper): File {
        if(tipFileName == null) {
            tipFileName = storageHelper.newSoundFile()
        }
        return File(tipFileName)
    }

    fun hasPhoto(): Boolean {
        return photoFileName != null && File(photoFileName).exists()
    }
}