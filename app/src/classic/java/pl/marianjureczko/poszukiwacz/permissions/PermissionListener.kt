//package pl.marianjureczko.poszukiwacz.permissions
//
//interface PermissionListener {
//
//    /** This event is invoked when all permissions required by [activityRequirements] are granted to process the rest of the work. */
//    fun permissionsGranted(activityRequirements: pl.marianjureczko.poszukiwacz.permissions.ActivityRequirements)
//
//    /** This event is invoked when user permanently denied any permissions and should grant those permissions in settings menu. */
//    fun navigateToSettings()
//
//    /** This event is invoked when user denies any permissions or does not want to go further in the process. */
//    fun deny()
//
//    /** This event is invoked when user faces a rationale dialog or came from settings menu to try again to grant permissions. */
//    fun retry()
//}