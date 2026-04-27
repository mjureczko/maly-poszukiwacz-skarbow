Provide compact answers to the user.

# Persistence management

There are three levels of state persistence.

1. **View Model level.** There is the ViewModel class that allows data to survive configuration changes such as screen
   rotations.
   Each screen has a view model class (a class extending the `ViewModel`).
   Additionally, some screens use the `SharedViewModel`.

2. **Saved instance state.** Data saved this way survives system initiated process death (e.g. removing the process due
   to lack of memory).
   Saving instance state should be done through view model.
   The view model class aggregates an instance of the `SavedStateHandle`.
   When the data is changed, the view model should call `SavedStateHandle.set(variable, value)` (the map syntax can be
   used too).  
   When initializing the view model, available data should be loaded from `SavedStateHandle.get()`.
   It is used sparely as usually either 1. is enough or 3. is required.
   When 3. is required it doesn't make much sense to additionally do 2.

3. **Persistent storage.** To save data that should remain available till the user decides to remove it.
   It should be done through serialization to XML and then saving to disc using the `StorageHelper` class.
   The data that supposed to be saved should be wrapped by the view model, and the view model should handle its
   lifecycle.
   The view model should be aware of changes in such data and persist it immediately after the change.

# The project uses Build Variants

Build variants (https://developer.android.com/build/build-variants) are used to create different variants of the app.
It's a solution based on product flavors.
On top of the predefined `release` and `debug` flavors are defined the following custom flavors:

- classic
    - defaultAssets
- custom
    - kalinowice
    - ...

That leads, thanks to the `variantFilter` configuration as well as `mode` and `assets` dimension from build.gradle, to
the following variants:

- kalinowiceCustomDebug
- kalinowiceCustomRelease
- defaultAssetsClassicDebug
- defaultAssetsClassicRelease

In order to refer to one of the variant, for instance to execute unit tests, one need to execute:

```
$ ./gradlew testKalinowiceCustomDebugUnitTest
```

# Source sets

For each flavor there has been created a source set with the same name as the flavor.
There are also dedicated test source sets with the `test` prefix, i.e. `test<flavor>`
Note that the source sets contains not only source code but also resources and assets.
The pattern is not followed for the instrumented UI tests.
For those tests, the source set directory name has and `androidTest` prefix followed by all flavors required to have a
vaild build variant, e.g. `androidTestKalinowiceCustomDebug`.

# Permissions

The permission is requested on the screen where it is needed for the first time.
What permission is needed is declared by implementing the `Requirement` interface.
Implementation shall deliver three values:

- the actual permission from the `Manifest.permission` class
- message to be displayed when the permission is denied
- predicate that checks if the permission is needed on the current device

The permissions are handled using the functions from `Permissions.kt`.

In case a screen requires multiple permissions, they are requested one by one.
The callback form first permission is called when user answers to the request for granting permission and is used to
record in state that now the second permission should be requested.
The logic is delivered in the `PermissionsHandler` class.