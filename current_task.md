## Goal

Introduce modules into the project following the design in `docs/modules.md` (Integrated Subprojects approach).
The first module should cover the `Compass` and `Steps` widgets from `SearchingScreen`, including all layers
(presentation/Compose, ViewModel, domain, data/GPS integration).

## Decisions

- Module name: `compass` (originally `compass-feature`, renamed per user request).
- Location: top-level Gradle subproject `:compass` (Integrated Subprojects, local
  `implementation(project(":compass"))`).
- Module type: Android library (`com.android.library`), configured with Compose, Hilt, and `play-services-location` (
  GPS).
- Namespace: `pl.marianjureczko.poszukiwacz.compass`.
- compileSdk/targetSdk: 37; minSdk: 23.
- `compose_version`, `kotlin_version`, `hilt_version` are defined as `ext.*` in the root `build.gradle` `buildscript`
  block
  and reused via `$compose_version` etc. (no version catalog yet, despite modules.md mentioning one for cross-repo
  sharing).
- UI Refactoring: `Compass` and `Steps` need to be siblings inside a Row container, extracted from the nested Box-Column
  structure.
  This enables them to be modularized together into the `:compass` module.

### Implementation Requirements for `:compass` module

To produce a `compass.aar` artifact, the module needs:

- **Build configuration**: Update `compass/build.gradle` with Hilt plugin, kapt, play-services-location dependency,
  correct SDK versions (37)
- **AndroidManifest.xml**: Add package declaration, GPS permissions (ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION), and
  location feature
- **Public API**: all classes requied by the core app should be located in `api` subpackage

## Constraints

## Open questions

## Status

- [x] Read `docs/modules.md` and project structure (`settings.gradle`, `build.gradle`, `app/build.gradle`,
  `SearchingScreen.kt`).
- [x] Create empty `compass` module: `compass/build.gradle` (library, Compose, Hilt, GPS) and
  `compass/src/main/AndroidManifest.xml`.
- [x] Register module in `settings.gradle` (`include ':compass'`).
- [x] Staged module scaffolding in git (`compass/build.gradle`, `compass/src/main/AndroidManifest.xml`,
  `settings.gradle`); build output excluded.
- [x] Refactored UI widgets: `Compass` and `Steps` are now in the same container (Row) in `SearchingScreenBody`.
- [x] **Android Resources** (`R.*` from host app):
  - `R.drawable.compass`, `R.drawable.arrow`, `R.drawable.steps` — move drawables into
    `compass/src/main/res/drawable/`
  - `R.string.medium_gps_signal`, `R.string.low_gps_signal`, `R.string.no_gps_signal` — move strings into
    `compass/src/main/res/values/strings.xml`
- [x] **Complete compass module build configuration**: Added
  `composeOptions { kotlinCompilerExtensionVersion compose_version }`, `maven-publish` plugin, and `publishing` block
  with GitHub Packages repository configuration. Version set to `1.0.0`.
- [x] **Enhance AndroidManifest.xml**: Already has package declaration, GPS permissions (ACCESS_FINE_LOCATION,
  ACCESS_COARSE_LOCATION), and location feature declaration.
- [ ] Move `Compass` and `Steps` Compose components + their `SearchingViewModel` logic into `:compass`
  (presentation, `internal` ViewModels, domain, data/GPS layers per modules.md encapsulation).

### Remaining for next session:

- [ ] Define public Composable wrappers (only those exposed to host app; Hilt + `hiltViewModel()`).
- [ ] Wire `app` -> `implementation(project(":compass"))` in `app/build.gradle` and update `SearchingScreen`
  imports/usages.
- [ ] Decide on flavor/build-variant handling for the module.
- [ ] Adopt a shared Gradle Version Catalog + Git submodule (per modules.md) so `compose_version`, `kotlin_version`,
  `hilt_version` are no longer `ext.*` in the root `build.gradle` but sourced from `libs.versions.toml`.
- [ ] Commit the staged module scaffolding (no commit made yet this session).

