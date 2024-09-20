package pl.marianjureczko.poszukiwacz.permissions

class PermissionsFixture(val requirements: pl.marianjureczko.poszukiwacz.permissions.ActivityRequirements) {

    fun getAllPermissions(): Array<String> =
        pl.marianjureczko.poszukiwacz.permissions.RequirementsForPhotoAndAudioTip.getSpecsArray()
            .flatMap { it.getPermissionsTextArray().asIterable() }
            .toTypedArray()

    fun getGrantResults(grantResult: Int) =
        IntArray(getAllPermissions().size) { grantResult }
}