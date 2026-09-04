# Code review: `module_compass` vs `master`

Review date: 2026-09-01 (updated)
Branch: `module_compass`
Last verified compile: `assembleDefaultAssetsClassicDebug` (per `current_task.md` Status item).

This document records the issues found while diffing `module_compass` against `master`. The build
compiles, but the design and the test situation are not healthy. The issues are grouped by severity
(A = broken features, B = removed/weakened tests, C = clean-architecture regressions, D = open
questions) and ordered by suggested-fix priority at the end.

> Related context: `current_task.md` (anchor for this task), `docs/compass-migration-solution.md`
> (original design), `docs/compass-module-dependencies.md` (member-level dependency table),
> `compass/README.md` (module contract).

---

## B. Removed or weakened tests (silent regression surface)

### B5. `LocationHolderTest` now lives in the app's tests but exercises compass code — STILL OPEN

Per the migration design, `LocationHolder` moved to `:compass` (`compass/data/LocationHolder.kt`).
The app still owns `app/src/test/java/.../usecase/LocationHolderTest.kt`, which compiles because
`:compass` is on the app's test compile classpath. If a long-term intent is to keep domain tests
in the owning module, this test should be moved to `compass/src/test/...` (matching where
`ArcCalculatorTest` was already moved). Document the placement decision.

---

## C. Bad designs / clean-architecture regressions

### C2. `compass/build.gradle` violates the "same dependency version" constraint — RESOLVED

Resolved on 2026-09-05: `compass/build.gradle:44` (`play-services-location`) downgraded from
`21.2.0` to `21.1.0` to match the app's `app/build.gradle:165` (`play-services-location:21.1.0`).
The `current_task.md` constraint ("If module needs the same dependency as the app, it should
use it in the same version") is now satisfied for the one artifact name both modules share.

The other two lifecycle items the original C2 entry called out were different artifacts
(`lifecycle-viewmodel-compose:2.5.1` is unique to compass; `lifecycle-extensions:2.2.0` is
unique to the app) and `lifecycle-runtime-ktx:2.5.1` was already aligned. They don't violate
the "same artifact name → same version" rule and the build resolves them fine. The
`lifecycle-viewmodel-compose:2.5.1` stays because `lifecycle-runtime-ktx:2.5.1` is also at 2.5.1
in both modules and the compose-viewmodel artifact follows the same 2.5.x line.

Verification: `:app:assembleDefaultAssetsClassicDebug -x test` → BUILD SUCCESSFUL;
`:compass:testDebugUnitTest` → BUILD SUCCESSFUL.

### C3. `compass/build.gradle` declares `material3` twice — STILL OPEN

```gradle
implementation "androidx.compose.material3:material3:1.3.2"
implementation "androidx.compose.material3:material3-android:1.3.2"
```

`material3-android` is the platform-specific artifact that already pulls in `material3`. Two
`implementation` lines means two separate Maven artefacts on the compile classpath. Either drop
`material3` (Android variant includes it) or drop `material3-android`. Same in `app/build.gradle`
(lines 146-147).

### C4. `compass/build.gradle` test-depends on `':app'` (`testShared`) — STILL OPEN

```gradle
testImplementation project(path: ':app', configuration: 'testShared')
```

The module's unit tests pull in classes from the app's `testShared` (`TestLocation`,
`TestLocationPort`, `TestCameraPort`, `TestQrScannerPort`, `TestExternalStoragePort`). That creates
a circular test-time dependency:

- `app` depends on `compass` (`implementation project(':compass')`),
- `compass`'s unit tests depend on `app`'s `testShared`.

Clean-architecture wise, the module should not need the host app's test fixtures. The module's own
tests (`CompassViewModelTest`, `ArcCalculatorTest`, `UpdateLocationUCTest`) don't reference any
`testShared` class. Either:

- delete the `testImplementation project(path: ':app', ...)` line (it's dead — none of
  `compass/src/test/*.kt` imports any `testShared` class), or
- if shared test helpers are needed, lift them to a third `:testing` module that both `:app` and
  `:compass` can depend on.

Note: `compass/src/test/java/pl/marianjureczko/poszukiwacz/compass/TestLocation.kt` shows up as
`AD` in `git status`, but it is **not** in the existing test sources tree (see `list_files` —
the only test files are `CompassViewModelTest.kt`, `domain/ArcCalculatorTest.kt`,
`domain/UpdateLocationUCTest.kt`). It appears to have been deleted from the working tree but
left in the index. Worth a `git rm` for cleanliness.

### C5. `AndroidLocation.distanceTo()` is now dead in production but still in the public API — STILL OPEN

`compass/.../api/AndroidLocation.kt` exposes `fun distanceTo(dest: AndroidLocation): Float`, and
`LocationWrapper` still implements it with `Location.distanceBetween`. But the new
`LocationCalculator.distanceInMeters` no longer calls `distanceTo` — it computes haversine directly.
Result: `LocationWrapper.distanceTo` is unused in production (callers: nothing in the diff), and so
is `TestLocation.distanceTo` (the only thing left that uses it is the dead `setDistance` mechanism
in A1/A2).

If the API no longer needs `distanceTo`, remove it from the interface (and from `LocationWrapper` +
`TestLocation`). Keeping it around is a misleading public surface and a future foot-gun.

### C6. `TestLocationPort` no longer delegates — FIXED

Previously, `TestLocationPort` delegated to a `LocationPortImpl` built with `mock()`s, which could
never produce a real `LocationResult`. The current `TestLocationPort` is now a clean test-double
that records the callback and lets the test fire it manually. Mixing that pattern with the
`LocationCallback.onLocationResult` stubbing in `SharedViewModelTest` is no longer a problem
because the GPS-callback tests use a different path (`SharedViewModel` no longer constructs a
`LocationPort` in tests; it goes through `TestLocationPort`). C6 is **resolved** by A1.

### C7. `SharedViewModel.createLocationUpdateCallback()` returns a new lambda every recomposition — STILL OPEN

`SearchingScreen.kt` line 182:

```kotlin
locationUpdateCallback = viewModel.createLocationUpdateCallback(),
```

This is invoked inline in a composable — i.e. on every recomposition it produces a fresh lambda.
The module's `CompassAndSteps.LaunchedEffect(selectedTreasure, locationUpdateCallback)` will re-key
on every recomposition and re-fire `viewModel.setLocationUpdateCallback(...)`. Not a functional bug
(the setter just overwrites the reference) but:

- it increases GC churn (small),
- it makes "the callback changed" indistinguishable from "the parent recomposed", so
  `LaunchedEffect` re-runs unnecessarily.

Tie the callback to state changes that actually matter, e.g. hoist it into a `remember { ... }` or
only supply it once when the ViewModel is created.

### C8. `CompassAndSteps` uses `hiltViewModel()` without a key — same
`CompassViewModel` per NavBackStackEntry — STILL OPEN

This is fine in Compose+Navigation, but worth noting: because `CompassViewModel` now owns
`LocationPort.startFetching` itself and never gets a callback from the app at construction time
(it's wired via a setter in `LaunchedEffect`), there is a window between
`init { locationPort.startFetching(...) }` and `LaunchedEffect` where the callback is `null` and
`UpdateLocationUC` won't fire for the host. Anything that needs the very first GPS fix will miss
it. The flow currently works because the first location update happens later than the recomposition
that sets the callback, but it's a timing dependency. Document it or move the callback wiring into
the constructor (it can't be DI because it lives in the app).

### C9. `setSelectedTreasure` triggers `recalculate()` only on the cached `currentLocation` — STILL OPEN

`CompassViewModel.recalculate()` reads `_state.value.currentLocation.getCurrentUserLocation()`. If
`setSelectedTreasure` is called before the first GPS fix, `recalculate()` is a no-op. There's no
listener that re-fires `recalculate` when the first location arrives and the treasure is already set
— actually `recalculateIfNeeded()` does that on every location update. Good. But: when
`selectedTreasure` is set after a `currentLocation` is already known, `recalculate()` does run;
when the order is reversed (no location yet, treasure selected, then first fix),
`recalculateIfNeeded()` triggers it on the first fix. So actually OK. (Worth a test that covers
both orderings — see A4.)

### C10. `CompassAndSteps` API surface requires a `@CompassIoDispatcher` even when the consumer doesn't use
`gpsJob` — STILL OPEN

OK, that's by design. But for tests like the espresso
`SearchingScreenTest.shouldUpdateNavigationWidgets_whenLocationIsUpdated`, the `TestPortsModule`
provides a real `LocationPort.create(...)` under the hood (via `TestLocationPort`'s delegate).
The test then relies on `delegate.startFetching(...)` to do something. With the current `delegate`,
it actually calls `fusedLocationClient.requestLocationUpdates(...)` against mocks — which never
produces a `LocationResult`, so the test would never observe a step change. That's why A1 had to be
fixed first. (A1 is fixed; this concern is moot now.)

### C11. `compass/src/main/java/.../compass/data/UpdateLocationCallback.kt` typealias is **still
** used internally — STILL PRESENT, but now used

```kotlin
typealias UpdateLocationCallback = (AndroidLocation) -> Unit
```

The typealias is no longer the public callback (`api.LocationUpdateCallback` is the public
`fun interface`). However, it is still referenced as the parameter type of
`LocationPortImpl.updateLocationCallback` (`compass/.../data/LocationPortImpl.kt` line 33) and
inside `LocationPort.startFetching` signatures. So it does serve a small internal-adapter role.
Document that, or inline the lambda type where used.

### C12.
`CompassState` is a public data class but the consuming code can only see it via the module's ViewModel state — STILL OPEN

Per the migration rule "anything outside `compass.api` is internal", `CompassState` and
`compass.viewmodel.CompassViewModel` are not strictly internal (no `internal` modifier on either).
Since the module is `com.android.library` and the app gets the types via classpath, the visibility
is public. Consider marking `CompassState` `internal` to the module (it's only read by the module's
`Compass` and `Steps` composables and by `CompassViewModel`).

### C13. `cartesianToPolar` in `CartesianCalculator` is **not** dead — RESOLVED

The original report claimed `CartesianCalculator` was dead. That was incorrect: `ArcCalculator`
uses it (per-member calls in `compass/.../domain/ArcCalculator.kt`):

```kotlin
class ArcCalculator {
    private val cartesian = CartesianCalculator()

    fun degree(...): Double {
        val cos = cartesian.cos(...)
        return when (cartesian.quarter(...)) {
            ...
        }
    }
}
```

And `Quarter` is the enum returned from `cartesian.quarter(...)`. The class is exercised by
`ArcCalculatorTest` (parameterised test at lines 12-41). C13 was a false positive.

### C14. `current_task.md` is in the diff; context anchor is leaking — STILL OPEN

The context-anchoring rules say to ignore `current_task.md`/`next_task.md`. `git status` shows
`?? next_task.md` (untracked), and `current_task.md` is staged/unstaged-modified in some
branches. Other untracked items in `git status`:

```
?? .cgcignore
?? .cline/
?? .cline_sound_enabled
?? .clineignore
?? app/defaultAssetsClassic/
?? app/kalinowiceCustom/
?? app/pegowCustom/
?? app/release/
?? app/src/classic/java/pl/marianjureczko/poszukiwacz/screen/bluetooth/BluetoothBondReceiver.kt
?? app/src/testSharedClone
?? gradle/gradle-daemon-jvm.properties
?? next_task.md
?? docs/module_compass_review.md
```

Plus `AD compass/src/test/java/pl/marianjureczko/poszukiwacz/compass/TestLocation.kt` — added to
the index but missing from the working tree. Verify the `.gitignore` actually ignores them;
otherwise the context anchor is leaking into the repo. Some of those should also be git-ignored.

### C15. `README.md` contains stale compilation errors — STILL OPEN

`README.md` lines 11-31 still contain an old compilation error log ("Compilation fails with", the
old `compass.model.TreasureDescription.kt` errors, the `HunterPathPort.kt` reference) followed by
a literal `LocationHolder` code block. This was a debugging artifact; clean it up so the README is
project-facing.

### C16. `BluetoothBondReceiver.kt` and `testSharedClone` are untracked — STILL OPEN

`app/src/classic/java/pl/marianjureczko/poszukiwacz/screen/bluetooth/BluetoothBondReceiver.kt` is
still untracked (`?? ...` in `git status`). Either delete it or add it to git. Same applies to
`app/src/testSharedClone/`, which is a directory-wide clone of `app/src/testShared/` (verified
via `diff -r app/src/testShared/java app/src/testSharedClone/java` — output is empty, so they are
bit-identical). The "clone" suggests a parallel copy that should be either merged into
`testShared` and removed, or kept as an explicit override mechanism for a flavor — the latter
seems unlikely given the contents are identical. Either merge or remove.

### C17. `strings.xml` split is now consistent — RESOLVED

The diff previously left strings used by the compass UI inside the app's `strings.xml`. They have
now been moved into `compass/src/main/res/values/strings.xml`:

- `medium_gps_signal`
- `low_gps_signal`
- `no_gps_signal`

A spot-check shows no remaining references to those keys outside the module's
`Compass.kt`. The corresponding `values-pl/strings.xml` is missing in `compass/src/main/res/` —
the Polish translations of those three strings still live in `app/src/main/res/values-pl/strings.xml`
if they exist there. Verify by adding Polish translations to `compass/src/main/res/values-pl/`
before removing the originals from the app.

The two Polish strings (`steps_to_treasure` in the app, used by `SearchingScreen.kt`) reference
`[%1$d] %2$d kroków do skarbu`, which appears to no longer be displayed by `Steps.kt` (the module
just shows the number). Worth confirming that the unused string can be deleted from the app.

---

## D. Open questions worth resolving

- The `distancesInSteps` map in `SharedState` is only consumed by `SelectorScreen`. Is the
  `updateDistancesInSteps` recomputation on every GPS fix (B2) the desired behaviour, or should
  the module expose a one-shot distance API and let the app compute on demand?
- Are `TestLocation` / `TestLocationPort` worth keeping in `app/src/testShared`, or should they
  move to `compass/src/test` (where they're now needed)?
- Should `compassState.lastLocationUpdateTime` `Date` field (used only by `scheduleGpsCheck`)
  be a `Long` (epoch ms) instead of a `Date` to avoid JVM-side allocations on every tick?
- The `CompassViewModel.recalculateIfNeeded()` is called from inside the `locationPort.startFetching`
  callback. If the callback is fired on the main thread (after `mainDispatcher` switch in
  `LocationPortImpl`), is there a risk of `init { ... startFetching ... }` racing with the very
  first `LaunchedEffect`? Worth a small instrumented test.
- Should `RestartProgressUC` (module) be deleted, or should `SharedViewModel.restartProgress()`
  call it after the app's UC to clear `CompassState.currentLocation`/`lastLocationUpdateTime`?
  Today, module state survives a route restart.

---

## Suggested order of work

1. ~~**Fix A1** (callback never fires)~~ — **DONE**.
2. ~~**Restore test coverage** for `CompassViewModel` (`setSelectedTreasure`/`scheduleGpsCheck`/
   `onCleared`) and for the module's `CompassState` contract lost in A3 (A4 + A3).~~ **DONE**.
3. ~~**Reconcile `distancesInSteps` duplication** (B2 / D)~~ — **DONE** (2026-09-04). The
   app/module split is intentional and documented; the within-module double-compute has been
   removed from `CompassViewModel`'s GPS callback and is locked in by
   `SHOULD invoke distanceInSteps exactly once per GPS fix WHEN a target is selected` in
   `CompassViewModelTest`. See B2 entry above for the full rationale.
4. ~~**Decide dual `ResetProgressUC`** (B6) — either wire it into `SharedViewModel.restartProgress()`
   or delete the module class.~~ **DONE**
5. ~~**Remove dead `gpsJob`/`respawn` test-only fields** from `SharedViewModel` (B1).~~ **DONE**
   (2026-09-04 — fields and `//for test only` block deleted from `SharedViewModel.kt`; the
   `Job` import was removed too; the only consumer in `SharedViewModelFixture.kt:62` had its
   `result.respawn = false` line deleted as well).
6. **Clean up test smells** — empty `cos()` test + misleading parameterised-test name (B4),
   ~~parameterise `JustFoundTreasureDescriptionFinderTest` with a non-empty list (B3)~~ **DONE**
   (2026-09-04 — B3 split into two test classes, each pinning its own branch; the parameterized
   test is now deterministic and covers the non-KNOWLEDGE branch only), move `LocationHolderTest`
   to the module if agreed (B5).
7. **Trim the API surface** (`distanceTo` from `AndroidLocation`, plus drop `setDistance` from
   `TestLocation` once `distanceTo` is gone) (C5, also closes A2).
8. **Resolve the typealias ambiguity** in `LocationPortImpl.updateLocationCallback` (C11) —
   either keep with a comment, or replace with `LocationUpdateCallback`.
9. ~~**Dep-version alignment + duplicate deps** (C2, C3).~~ **C2 DONE** (2026-09-05 — see C2
   entry above; C3 — duplicate `material3` declarations — still open).
10. **Decide test-time circular dependency** (C4) — drop `testImplementation project(path: ':app',
    configuration: 'testShared')` if no module test needs it.
11. **Fix the dual-qualifier DI smell** (C1) — either revert to one set of qualifiers, or move them
    to a dedicated `:core-di` sub-project.
12. **Module-visibility hardening** (`internal` on `CompassState`, `CompassViewModel`) (C12).
13. **Repo hygiene**: confirm `.gitignore` covers `current_task.md`, `next_task.md`, `.cline*`,
    `app/{defaultAssetsClassic,kalinowiceCustom,pegowCustom,release}`, `app/src/testSharedClone/`,
    `gradle/gradle-daemon-jvm.properties`, build outputs. Remove or commit
    `BluetoothBondReceiver.kt`. Clean up `README.md` (C14, C15, C16).
14. **Strings audit** (C17) — confirm the three new compass strings have Polish translations in
    the module, and that the app's leftover `steps_to_treasure` (if no longer displayed) is removed.