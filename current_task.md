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
- [ ] 
  1. `UpdateLocationUC.kt`: missing comma between constructor params; remove `selectedTreasure` logic — UC only updates
     currentLocation + gpsAccuracy (+ delegates hunter path update).
- [ ] 
  2. Delete `compass/model/TreasureDescription.kt` and `compass/model/Route.kt` (resolves unresolved `StoragePort`
     references).
- [ ] 
  3. Refactor `compass/domain/LocationCalculator.kt` to coordinates-based API (public step-distance API for the app).
- [ ] 
  4. Refactor `CompassViewModel`: hold target coordinates instead of treasure/route; receive `hunterPathService` via
     setter;
     make its dependencies Hilt-resolvable (`@Inject` constructors on `UpdateLocationUC`, `LocationCalculator`,
     `ArcCalculator`).
- [ ] 
  5. Change `api/CompassAndSteps.kt` signature to location-based params; forward target + hunterPathService to
     ViewModel.
- [ ] 
  6. Remove `AndroidLocation` from app and use only the one from module.
- [ ] 
  7. Make app's `shared.port.LocationPort` implement `compass.data.LocationPort`; add Hilt binding in `PortsModule`. -
     It must be discussed in details before implementation!
- [ ] 
  8. Implement `HunterPathService` in `SharedViewModel` (`addLocation` updates+saves hunter path;
     `isLocationBeingUpdated`
     delegates to `HunterPath.isLocationBeingUpdated()`).
- [ ] 
  9. Restore minimal app-side `LocationCalculator` (`distanceInKm`); update imports in `HunterPath`, `ReportGenerator`,
     `ReportMapSummary`, `FacebookViewModel`, `FacebookScreen`; remove obsolete providers from `SingletonModule`
     (`locationCalculator` pointing at deleted class, `updateLocationUC`). - It must be discussed in details before
     implementation!
- [ ] 
  10. Adapt `JustFoundTreasureDescriptionFinder` to the new coordinates-based calculator signature.
- [ ] 
  11. Adapt `SearchingScreen`'s `CompassAndSteps` call (pass selected treasure coordinates instead of treasure/route).
- [ ] 
  12. Verify/update `TestPortsModule` (androidTest) overrides for new bindings.
- [ ] The project compiles.
 