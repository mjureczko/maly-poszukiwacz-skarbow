package pl.marianjureczko.poszukiwacz.permissions

import pl.marianjureczko.poszukiwacz.R

interface PermissionsSpec {
    fun getPermissionWithCodeArray(): Array<PermissionWithCode>
    fun getMessage(): Int
    fun getMessageForPermanentDenial(): Int
}

object PermissionsForPhotoAndAudioTip: PermissionsSpec {
    val camera = PermissionWithCode.CAMERA
    val microphone = PermissionWithCode.MICROPHONE

    override fun getPermissionWithCodeArray(): Array<PermissionWithCode> = arrayOf(camera, microphone)
    override fun getMessage(): Int = R.string.missing_photo_and_audio_permission
    override fun getMessageForPermanentDenial(): Int = R.string.missing_photo_and_audio_permission
}
