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
    @field:Element(required = false) var tipFileName: String?,
    @field:Element(required = false) var photoFileName: String?
) : Serializable {
    constructor(id: Int, latitude: Double, longitude: Double) : this(id, latitude, longitude, null, null)
    constructor() : this(0, 0.0, 0.0, null, null)

    fun prettyName(): String = "[$id] $latitude $longitude"

    fun createPhotoFile(storageHelper: StorageHelper): File {
        if (photoFileName == null) {
            photoFileName = storageHelper.newPhotoFile()
        }
        return File(photoFileName)
    }

    fun hasPhoto(): Boolean {
        return tipFileName != null && File(photoFileName).exists()
    }
}