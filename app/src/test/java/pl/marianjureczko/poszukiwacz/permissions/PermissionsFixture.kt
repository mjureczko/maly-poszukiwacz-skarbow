package pl.marianjureczko.poszukiwacz.permissions

class PermissionsFixture(val requirements: ActivityRequirements) {

    fun getAllPermissions(): Array<String> =
        RequirementsForPhotoAndAudioTip.getSpecsArray()
            .flatMap { it.getPermissionsTextArray().asIterable() }
            .toTypedArray()

    fun getGrantResults(grantResult: Int) =
        IntArray(getAllPermissions().size) { grantResult }
}