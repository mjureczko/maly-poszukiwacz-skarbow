package pl.marianjureczko.poszukiwacz.permissions

import pl.marianjureczko.poszukiwacz.R

interface ActivityRequirements {
    fun getSpecsArray(): Array<PermissionsSpec>
    fun getMessage(): Int
    fun getMessageForPermanentDenial(): Int
}

object RequirementsForDoingPhoto : ActivityRequirements {
    val camera = PermissionsSpec.CAMERA

    override fun getSpecsArray(): Array<PermissionsSpec> = arrayOf(camera)
    override fun getMessage(): Int = R.string.missing_photo_permission
    override fun getMessageForPermanentDenial(): Int = R.string.missing_photo_permission
}

object RequirementsForPhotoAndAudioTip : ActivityRequirements {
    val camera = PermissionsSpec.CAMERA
    val microphone = PermissionsSpec.MICROPHONE

    override fun getSpecsArray(): Array<PermissionsSpec> = arrayOf(camera, microphone)
    override fun getMessage(): Int = R.string.missing_photo_and_audio_permission
    override fun getMessageForPermanentDenial(): Int = R.string.missing_photo_and_audio_permission
}

object RequirementsForNavigation: ActivityRequirements {
    val location = PermissionsSpec.LOCATION

    override fun getSpecsArray(): Array<PermissionsSpec> = arrayOf(location)
    override fun getMessage(): Int = R.string.missing_location_permission
    override fun getMessageForPermanentDenial(): Int = R.string.missing_location_permission
}

object RequirementsForBluetooth: ActivityRequirements {
    val bluetooth = PermissionsSpec.BLUETOOTH

    override fun getSpecsArray(): Array<PermissionsSpec> = arrayOf(bluetooth)
    override fun getMessage(): Int = R.string.missing_bluetooth_permission
    override fun getMessageForPermanentDenial(): Int = R.string.missing_bluetooth_permission
}

object RequirementsForExternalStorage: ActivityRequirements {
    val externalStorage = PermissionsSpec.EXTERNAL_STORAGE

    override fun getSpecsArray(): Array<PermissionsSpec> = arrayOf(externalStorage)
    override fun getMessage(): Int = R.string.missing_external_storage_permission
    override fun getMessageForPermanentDenial(): Int = R.string.missing_external_storage_permission
}