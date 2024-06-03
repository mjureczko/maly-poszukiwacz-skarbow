package pl.marianjureczko.poszukiwacz.shared

data class PermissionStatus(
    val isGranted: Boolean = false,
    /** Is set to true once the user has denied permission. */
    val shouldShowRationale: Boolean = false,
) {
    fun refreshIsGranted(): PermissionStatus? {
        return null;
    }
}
