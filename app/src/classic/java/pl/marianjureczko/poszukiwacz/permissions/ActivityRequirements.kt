//package pl.marianjureczko.poszukiwacz.permissions
//
//import android.Manifest
//import pl.marianjureczko.poszukiwacz.R
//
//interface ActivityRequirements {
//    fun getPermission(): String?
//    fun getSpecsArray(): Array<PermissionsSpec>
//    fun getMessage(): Int
//    fun getMessageForPermanentDenial(): Int
//}
//
//object RequirementsForPhotoAndAudioTip : ActivityRequirements {
//    val camera = PermissionsSpec.CAMERA
//    val microphone = PermissionsSpec.MICROPHONE
//    override fun getPermission(): String? = null
//    override fun getSpecsArray(): Array<PermissionsSpec> = arrayOf(camera, microphone)
//    override fun getMessage(): Int = R.string.missing_photo_and_audio_permission
//    override fun getMessageForPermanentDenial(): Int = R.string.missing_photo_and_audio_permission
//}
//
//object RequirementsForBluetooth: ActivityRequirements {
//    val bluetooth = PermissionsSpec.BLUETOOTH
//    override fun getPermission(): String? = null
//    override fun getSpecsArray(): Array<PermissionsSpec> = arrayOf(bluetooth)
//    override fun getMessage(): Int = R.string.missing_bluetooth_permission
//    override fun getMessageForPermanentDenial(): Int = R.string.missing_bluetooth_permission
//}
//
//object RequirementsForExternalStorage: ActivityRequirements {
//    val externalStorage = PermissionsSpec.EXTERNAL_STORAGE
//    override fun getPermission(): String? = null
//    override fun getSpecsArray(): Array<PermissionsSpec> = arrayOf(externalStorage)
//    override fun getMessage(): Int = R.string.missing_external_storage_permission
//    override fun getMessageForPermanentDenial(): Int = R.string.missing_external_storage_permission
//}