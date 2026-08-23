## Goal

We are in the middle of extracting compass module.
There are compilation errors.
The module extraction should be finished in such a way that it is possible to compile the project.

## Decisions

- In UpdateLocationUC, there is commented code:
  // distancesInSteps = state.value.route.treasures
  // .associate { it.id to locationCalculator.distanceInSteps(it, location) }
  // .toMap()
  It is a relevant feature, but it should not be extract to compass module in such way.
  Storing and updating `distancesInSteps` should be app (not module) responsibility, but the module should offer in the
  api something that will make distance in steps calculation available for the main app.

- UpdateLocationUC has compilation errors around `selectedTreasure`. Treasures are not from module domain - the module
  should work with locations. The responsibilities should be split so that selected treasure will stay in the app, but
  it's location will be available for the module.

- `UpdateLocationUC` keeps updating `stepsToTreasure` and `needleRotation` (required by the Compass/Steps widgets),
  but the target is provided as an `AndroidLocation?` (per invocation) instead of a treasure. The app will supply the
  selected treasure's location; when no target is set, steps are `null` and rotation is `0f`.

- The module's model classes (`compass/model/TreasureDescription.kt`, `compass/model/Route.kt`) are duplicates of the
  app's
  `pl.marianjureczko.poszukiwacz.model` classes and import the app's `StoragePort` (which is a dependency-direction
  violation).
  They are to be deleted from the module; the module API will work with locations/coordinates instead of treasures.

- `LocationCalculator` in the module will be refactored to a coordinates-based API
  (`AndroidLocation` in place of `TreasureDescription`), which also serves as the public step-distance
  API for the main app (see decision about `distancesInSteps`).

- `HunterPathService` implementation lives in the app's `SharedViewModel` (a `@HiltViewModel`, not injectable into
  another
  ViewModel). Since `CompassAndSteps` already receives `hunterPathService` as a parameter, it will be forwarded to
  `CompassViewModel` via a setter called from `LaunchedEffect` instead of constructor injection.

- App's `shared.port.LocationPort` will implement the module's `compass.data.LocationPort` interface (method signatures
  already match), plus a Hilt binding for the interface will be added in `PortsModule`.

- The two structurally identical `AndroidLocation` interfaces (`usecase.AndroidLocation` in app and
  `compass.data.AndroidLocation` in module) will be reduced so that only the one from module will remain.

- An app-side minimal `LocationCalculator` (only `distanceInKm` over locations) must be restored, because it is still
  used by
  `HunterPath.pathLengthInKm()`, `ReportGenerator`, `ReportMapSummary`, `FacebookViewModel`, `FacebookScreen`.
  The obsolete `updateLocationUC` provider in `SingletonModule` will be removed (nothing injects it anymore).

- Inside module main directory there is additional `README.md` file that holds the most important decisions regarding
  the module. Specifically it covers the module requirements. Update the `README.md` file whenever important facts or
  decisions regarding the module pops up.

- Only ONE `LocationCalculator` remains — in the compass module.

## Constraints

- The dependencies versions should not be changed. Specifically if module needs the same dependency as the app, it
  should use it in the same version.
- The app should depend on the module, but the module shall not depend on the app.

## Open questions

- Target location API shape: pass plain `targetLatitude/targetLongitude: Double?` to `CompassAndSteps`, or introduce a
  small
  `api/TargetLocation(latitude, longitude)` data class? (An `AndroidLocation` adapter for a static treasure is awkward
  because
  `accuracy`/`observedAt` are meaningless for it.)
- Known runtime caveat (not blocking compilation): both `SharedViewModel` and `CompassViewModel` call
  `locationPort.startFetching`, while the app's `LocationPort` keeps only one callback and `stopFetching` is shared.
  Acceptable for now?

## Status

- [x] Re-analysis of compilation errors after partial fixes (hilt-navigation-compose added, HunterPathService
  introduced,
  GpsAccuracy import fixed, LocationHolder import fixed).
- [x] 
    1. `UpdateLocationUC.kt`: missing comma between constructor params; remove `selectedTreasure` logic — UC updates
       currentLocation + gpsAccuracy + stepsToTreasure/needleRotation (target passed as `AndroidLocation?`) and
       delegates
       hunter path update.
- [x] 
    2. Delete `compass/model/TreasureDescription.kt` and redesign LocationCalculator API to use AndroidLocation in place
       of app's TreasureDescription.
       2.1 Delete `compass/model/Route.kt` (resolves unresolved `StoragePort` references).

  Note (item 2 + 2.1): both module model classes are deleted. `CompassViewModel` holds
  `selectedTreasure: AndroidLocation?` (`setSelectedTreasure(...)`) with no route state; `CompassAndSteps(
  selectedTreasure: AndroidLocation?, hunterPathService, ...)` — the `route` parameter was removed. The ViewModel's
  `startFetching` callback passes `selectedTreasure` to `UpdateLocationUC`. No remaining references to
  `compass.model.*` in the project.
- [x] 
    3. Refactor `compass/domain/LocationCalculator.kt` to coordinates-based API (public step-distance API for the app).
- [x] 
    4. Refactor `CompassViewModel`: hold target coordinates instead of treasure/route (done); receive
       `hunterPathService`
       via setter;
     make its dependencies Hilt-resolvable (`@Inject` constructors on `UpdateLocationUC`, `LocationCalculator`,
     `ArcCalculator`).

  Note (item 4): `UpdateLocationUC` no longer depends on `HunterPathService` — path recording now handled through
  `LocationUpdateCallback`.
- [x] 
  5. Change `api/CompassAndSteps.kt` signature to location-based params (done); forward target to ViewModel.
- [x] 
    6. Remove `AndroidLocation` from app and use only the one from module.
- [x] 
    7. Move `shared.port.LocationPort` to compass module and remove the corresponding interface (
       `compass.data.LocationPort`); add Hilt binding in `PortsModule`; handle @MainDispatcher the same way as the
       @IoDispatcher is already handled

  Note (item 7): the concrete `LocationPort` class now lives in `compass/data/LocationPort.kt` and uses
  `@CompassIoDispatcher` / `@CompassMainDispatcher`; new `CompassMainDispatcher` qualifier added in
  `compass/api/DispatcherQualifiers.kt`. `PortsModule` provides `mainDispatcher()` annotated with both
  `@MainDispatcher` + `@CompassMainDispatcher`, and `locationPort(...)` returns the module class using the compass
  qualifiers. Imports updated in `SharedViewModel`, `TreasureEditorViewModel`, `TestLocationPort`,
  `SharedViewModelTest`, `SharedViewModelFixture`. `compass/README.md` updated. `TestPortsModule` (androidTest) still
  references the old type — left for item 12.
- [x] 
  8. Consolidate `HunterPathService` into `LocationUpdateCallback` - path recording logic moved to callback
     implementation.

  Note (item 8): `HunterPathService` interface removed. Path recording logic (previously in `addLocation`) moved to
  `LocationUpdateCallback` implementation in `SharedViewModel`. `CompassAndSteps` and `SearchingScreen` updated to
  remove `hunterPathService` parameter. All location update handling now consolidated through `LocationUpdateCallback`.
- [x] 
    9. Restore minimal app-side `LocationCalculator` (`distanceInKm`); update imports in `HunterPath`,
       `ReportGenerator`,
     `ReportMapSummary`, `FacebookViewModel`, `FacebookScreen`; remove obsolete providers from `SingletonModule`
     (`locationCalculator` pointing at deleted class, `updateLocationUC`). - It must be discussed in details before
     implementation!

  Note (item 9): restored `screen/searching/LocationCalculator.kt` with only `distanceInKm(AveragedLocation,
  AveragedLocation)` + `METERS_TO_STEPS_FACTOR` constant (used by SearchingScreenTest), keeping the
  `AndroidLocationFactory` constructor so existing call sites compile unchanged. `HunterPath` import switched to the
  app calculator; facebook/report files already imported `screen.searching.LocationCalculator` so they need no change.
  In `SingletonModule` only the `updateLocationUC` provider was removed — the `locationCalculator` provider is valid
  again after the restore and is still needed (`FacebookViewModel` injects it).
- [x] 
  10. Adapt `JustFoundTreasureDescriptionFinder` to the new coordinates-based calculator signature.

  Note (item 10): the JustFoundTreasureDescriptionFinder now builds a module `LocationWrapper` from the selected
  treasure's coordinates
  (`accuracy = 0f`, `observedAt = 0`) and calls `locationCalculator.distanceInSteps(target, userLocation)`.
  The parameterized test's stubbing was updated to matchers (`any<AndroidLocation>(), eq(userCoordinates)`).
  Follow-up (user decision): `locationCalculator` in the finder is no longer nullable — `SharedViewModel` injects the
  module `compass.domain.LocationCalculator` and passes it to the finder; `SharedViewModelFixture` updated accordingly (
  module calculator mock, obsolete `updateLocationUC` argument removed); finder test uses an import alias to
  disambiguate the module calculator from the same-package app calculator.
- [x] 
  11. Adapt `SearchingScreen`'s `CompassAndSteps` call (pass selected treasure coordinates instead of treasure/route).

  Note (item 11): the call now builds a module `LocationWrapper` from the selected treasure's coordinates
  (`accuracy = 0f`, `observedAt = 0`) and no longer passes `route`. Path recording logic moved to
  `LocationUpdateCallback` implementation.
- [ ] 
  12. Verify/update `TestPortsModule` (androidTest) overrides for new bindings.
- [x] 
  13. Check if LocationWrapper can be encapsulated in compass module

  Note (item 13): DONE. `AndroidLocation` moved to `compass.api` package; a factory was added to its companion
  object: `AndroidLocation.create(latitude, longitude, accuracy, observedAt)` and
  `AndroidLocation.create(android.location.Location)` — both instantiate the module-internal `LocationWrapper`.
  All app-side `LocationWrapper(...)` constructions replaced with the factory:
  `AndroidLocationFactoryImpl`, `XmlMapper`, `SearchingScreen`, `JustFoundTreasureDescriptionFinder`,
  `SharedViewModelTest`. The app now has ZERO references to `LocationWrapper`; remaining `compass.data`
  dependencies are `LocationHolder`, `LocationPort`, `UpdateLocationCallback` (ports/state,
  acceptable). Test fallout fixed along the way: imports updated in `TestLocation`, `AndroidLocationArranger`,
  `CalculateAveragedLocationUCTest`, `LocationHolderTest`, `AddTreasureDescriptionToRouteUCTest`,
  `SharedStateTest` (removed obsolete `stepsToTreasure` ctor arg); `ArcCalculatorTest` moved to the compass
  module; obsolete `CartesianCalculatorTest` (tested removed `Quarter` API) deleted; `SharedViewModelTest`
  steps/needle assertions removed (state fields moved into the module's CompassState).
  Verified: `assembleDefaultAssetsClassicDebug`, `compileDefaultAssetsClassicDebugUnitTestKotlin`,
  `compass:compileDebugUnitTestKotlin` → BUILD SUCCESSFUL.

Follow-up (user decision):

- Deleted the restored app-side `screen/searching/LocationCalculator.kt` and the dead
  `usecase/UpdateLocationUCTest.kt` (tested the removed app UC).
- Module `LocationCalculator` gained a coordinates-based overload
  `distanceInKm(startLatitude, startLongitude, endLatitude, endLongitude)`; `HunterPath.pathLengthInKm` uses it
  over `AveragedLocation` coordinates.
- Imports switched to `compass.domain.LocationCalculator` in `HunterPath`, facebook/report files,
  androidTest files (`ReportAbstractTest`, `ReportGeneratorTest`, `HunterPathAndroidTest` now construct
  `LocationCalculator()`), finder test (alias removed).
- `SingletonModule`: `locationCalculator` provider removed (module class has `@Inject` constructor; Hilt resolves it).
- `SearchingScreenTest` expectation now uses the module formula `(distance / 0.7f).toInt()`.
  Runtime caveat: module UC computes steps via haversine coordinates, not via mocked `distanceTo`, so this espresso test
  may need rework together with item 11.
- [x] The project compiles.

  Note (compile fixes on the way to a green build):
  - Correct assemble task name is `assembleDefaultAssetsClassicDebug` (dimension order: assets × mode),
    not `assembleClassicDefaultAssetsDebug`.
  - `compass/build.gradle`: compileSdk/targetSdk aligned down to 36 to match the app (module required nothing from API
    37).
  - Re-deleted resurrected `compass/model/Route.kt` (no references to `compass.model.*` remain).
  - `PortsModule`: Dagger forbids more than one `@Qualifier` per `@Provides` method — split into separate
    providers for `@IoDispatcher`/`@CompassIoDispatcher` and `@MainDispatcher`/`@CompassMainDispatcher`
    (all returning the same dispatchers).
  - Classic flavor `AddTreasureDescriptionToRouteUC`: import switched to module `compass.data.AndroidLocation`.
  - Verified with `./gradlew :app:assembleDefaultAssetsClassicDebug -x test` → BUILD SUCCESSFUL.
 