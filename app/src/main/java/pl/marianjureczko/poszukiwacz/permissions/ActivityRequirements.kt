package pl.marianjureczko.poszukiwacz.permissions

import pl.marianjureczko.poszukiwacz.R

interface ActivityRequirements {
    fun getPermissionsArray(): Array<PermissionsSpec>
    fun getMessage(): Int
    fun getMessageForPermanentDenial(): Int
}

object RequirementsForPhotoAndAudioTip: ActivityRequirements {
    val camera = PermissionsSpec.CAMERA
    val microphone = PermissionsSpec.MICROPHONE

    override fun getPermissionsArray(): Array<PermissionsSpec> = arrayOf(camera, microphone)
    override fun getMessage(): Int = R.string.missing_photo_and_audio_permission
    override fun getMessageForPermanentDenial(): Int = R.string.missing_photo_and_audio_permission
}

object RequirementsForNavigation: ActivityRequirements {
    val location = PermissionsSpec.LOCATION

    override fun getPermissionsArray(): Array<PermissionsSpec> = arrayOf(location)
    override fun getMessage(): Int = R.string.missing_location_permission
    override fun getMessageForPermanentDenial(): Int = R.string.missing_location_permission
}

object RequirementsForBluetooth: ActivityRequirements {
    val bluetooth = PermissionsSpec.BLUETOOTH

    override fun getPermissionsArray(): Array<PermissionsSpec> = arrayOf(bluetooth)
    override fun getMessage(): Int = R.string.missing_bluetooth_permission
    override fun getMessageForPermanentDenial(): Int = R.string.missing_bluetooth_permission
}