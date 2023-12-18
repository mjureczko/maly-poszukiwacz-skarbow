

# Development environment configuration

To ~/.gradle/gradle.properties add:

```
MAPBOX_DOWNLOADS_TOKEN = <token>
FACEBOOK_TOKEN = <token>
```

Both tokens can be obtained from KeePass.
When using gradle directly the token can be delivered as a parameter: `-PMAPBOX_DOWNLOADS_TOKEN=<token>` or `-PFACEBOOK_TOKEN=<token>`.

# Activities flow

```mermaid
   stateDiagram-v2
    [*] --> MainActivity
    MainActivity --> TreasuresEditorActivity : create new route\n OR\n edit existing route
    TreasuresEditorActivity --> MainActivity
    MainActivity --> BluetoothActivity : send route\n OR\n receive route
    BluetoothActivity --> MainActivity
    MainActivity --> SearchingActivity : select route\n for searching
    SearchingActivity --> MainActivity
    SearchingActivity --> TreasureSelectorActivity : select treasure\n for searching
    TreasureSelectorActivity --> SearchingActivity
    SearchingActivity --> PhotoActivity : show photo hint
    PhotoActivity --> SearchingActivity
    SearchingActivity --> ResultActivity : show treasure
    ResultActivity --> SearchingActivity
    SearchingActivity --> QrScanIntent : scan found treasure
    QrScanIntent --> SearchingActivity
    TreasuresEditorActivity --> CapturePhotoIntent : make photo\n with a hint
    CapturePhotoIntent --> TreasuresEditorActivity
    SearchingActivity --> MapActivity : show map
    MapActivity --> SearchingActivity

    MainActivity --> FacebookActivity
    FacebookActivity --> MainActivity
    TreasuresEditorActivity --> FacebookActivity
    FacebookActivity --> TreasuresEditorActivity
    BluetoothActivity --> FacebookActivity
    FacebookActivity --> BluetoothActivity
    SearchingActivity --> FacebookActivity
    FacebookActivity --> SearchingActivity
    TreasureSelectorActivity --> FacebookActivity
    FacebookActivity --> TreasureSelectorActivity
    PhotoActivity --> FacebookActivity
    FacebookActivity --> PhotoActivity
    ResultActivity --> FacebookActivity
    FacebookActivity --> ResultActivity
    TreasuresEditorActivity --> FacebookActivity
    FacebookActivity --> TreasuresEditorActivity
    MapActivity --> FacebookActivity
    FacebookActivity --> MapActivity

    note right of QrScanIntent: INTENT
    note right of CapturePhotoIntent: INTENT
```

# Persistence management

There are three levels of state persistence.

1. **View Model level.** There is the ViewModel class that allows data to survive configuration changes such as screen rotations. Each stateful activity has a view model class (a
   class
   extending the `ViewModel`).

2. **Saved instance state.** Data saved this way survives system initiated process death (e.g. removing the process due to lack of memory). Saving instance state should be done
   through view model. The view model class aggregates an instance of the `SavedStateHandle`. When the data is changed, the view model should
   call `SavedStateHandle.set(variable, value)` (
   the map syntax can be used too).  
   When initializing the view model, available data should be loaded from `SavedStateHandle.get()`.

3. **Persistent storage.** To save data that should remain available till the user decides to remove it. It should by done through serialization to XML and then saving to disc
   using the `StorageHelper` class. The data that supposed to be saved should be wrapped by the view model, and the view model should handle its lifecycle. The view model should be
   aware of changes in such data and persist it immediately after the change.

# Permissions

## Structure

```mermaid
   classDiagram
    class ActivityRequirements{
        <<interface>>
        getSpecsArray()
        getMessage()
        getMessageForPermanentDenial()
    }
    class PermissionActivity{
        <<abstract>>
        assurePermissionsAreGranted()
    }
    class PermissionsSpec{
        <<enum>>
        requestCode: Int
        getPermissionsTextArray()
    }
    class PermissionListener{
        <<interface>>
        permissionsGranted()
        navigateToSettings()
        deny()
        retry()
    }
    class PermissionManager{
        areAllPermissionsGranted()$
        isPermissionGranted()$
        requestAllPermissions()
        handleRequestPermissionsResult()
        handleResume()
    }
    PermissionActivity <|-- XyzActivity
    PermissionActivity --> PermissionManager
    PermissionActivity --> PermissionsSpec
    PermissionsSpec <|-- PermissionsForAbcd
    PermissionsForAbcd --> "1..*" PermissionWithCode
    PermissionManager --> PermissionListener
    PermissionActivity *--> PermissionListener : inner class
```

## Requesting permissions

```mermaid
   sequenceDiagram
    Start ->>+ XyzActivity: onCreate()
    XyzActivity ->>+ PermissionActivity: assurePermissionsAreGranted(:PermissionsSpec)
    PermissionActivity ->>+ ActivityRequirements: getSpecsArray()
    ActivityRequirements -->>- PermissionActivity: x
    PermissionActivity ->>+ PermissionManager: areAllPermissionsGranted(:Array<PermissionWithCode>)
    PermissionManager -->>- PermissionActivity: x
    alt all permissions granted
        PermissionActivity ->>+ XyzActivity: onPermissionsGranted()
        XyzActivity -->>- PermissionActivity: x
    else
        PermissionActivity ->>+ PermissionManager: requestAllPermissions(:PermissionsSpec)
        PermissionManager ->>+ ActivityRequirements: getSpecsArray()
        ActivityRequirements -->>- PermissionManager: x
        PermissionManager ->>+ ActivityCompat: requestPermissions(permissions,requestCode)
        ActivityCompat -->>- PermissionManager: x
        PermissionManager -->>- PermissionActivity: x
    end
    PermissionActivity -->>- XyzActivity: x
    XyzActivity -->>- Start: x
```

## Handling request permission result

```mermaid
   sequenceDiagram
    Start ->>+ PermissionActivity: onRequestPermissionsResult(requestCode,permissions,grantResults)
    PermissionActivity ->>+ PermissionManager: handleRequestPermissionsResult(:PermissionsSpec,permissions,grantResults)
    alt are all permissions granted
        PermissionManager ->>+ PermissionListener: permissionsGranted(:PermissionsSpec)
        PermissionListener ->>+ PermissionActivity: onPermissionsGranted(:PermissionsSpec)
        PermissionActivity -->>- PermissionListener: x
        PermissionListener -->>- PermissionManager: x
    else
        PermissionManager ->>+ ActivityCompat: shouldShowRequestPermissionRationale(deniedPermission)
        ActivityCompat -->>- PermissionManager: x
        alt should show rationale
            PermissionManager ->>+ PermissionManager: showPermissionRationaleDialog(:PermissionsSpec)
            PermissionManager ->>+  PermissionListener: retry()
            PermissionListener -->>- PermissionManager: x
            PermissionManager -->>- PermissionManager: x
        else
            PermissionManager ->>+ PermissionManager: showPermissionPermanentDenialDialog(:PermissionsSpec,deniedPermission)
            opt user wants to change permissions in settings
                PermissionManager ->>+ PermissionListener: navigateToSettings()
                PermissionListener -) Settings: startActivity(intent)
                PermissionListener -->>- PermissionManager: x
            end
            PermissionManager -->>- PermissionManager: x
        end

    end
    PermissionManager -->>- PermissionActivity: x
    PermissionActivity -->>- Start: x
    Settings -)+ PermissionActivity: onResume()
    PermissionActivity ->>+ PermissionManager: handleResume()
    PermissionManager -->>- PermissionActivity: x
```

Rejecting required permissions should result in a dialog where user can change his/her mind. Permissions can be rejected "permanently", then the settings need to be visited to
grant permissions.

# Releasing

To build aab file execute:

```
./gradlew bundle -PFACEBOOK_TOKEN=<token> -PMAPBOX_DOWNLOADS_TOKEN=<token> -PRELEASE_KEY_PASSWORD=<pass> -PRELEASE_STORE_PASSWORD=<pass>
```

To build apk file execute:

```
./gradlew assembleRelease -PFACEBOOK_TOKEN=<token> -PMAPBOX_DOWNLOADS_TOKEN=<token> -PRELEASE_KEY_PASSWORD=<pass> -PRELEASE_STORE_PASSWORD=<pass>
```

All tokens and passwords available in KeePass.

## Old releasing procedure

You need to push a tag to create a release. Execute:

```
git tag
```

to check existing tags (only the local ones will be printed).

To create a new release execute:

```
git tag -a X.Y -m "comment"
git push origin --tags
```

It will launch a pipeline instance that creates a new release.
