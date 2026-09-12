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
- compileSdk/targetSdk: 36; minSdk: 23.
- `compose_version`, `kotlin_version`, `hilt_version` are defined as `ext.*` in the root `build.gradle` `buildscript`
  block
  and reused via `$compose_version` etc. (no version catalog yet, despite modules.md mentioning one for cross-repo
  sharing).
- UI Refactoring: `Compass` and `Steps` need to be siblings inside a Row container, extracted from the nested Box-Column
  structure.
  This enables them to be modularized together into the `:compass` module.

- Publication automation: Option A — tag-driven releases. Pushing a git tag `compass-v<version>` (e.g.
  `compass-v1.0.0`) triggers `.github/workflows/publish_compass.yml`, which runs the `:compass` unit tests and then
  publishes. Rejected: Option B manual `workflow_dispatch` (version would exist only in the workflow run, not in git
  history) and Option C main-branch SNAPSHOTs (additive, can be added to the same workflow later if ever needed).
- Artifact version source: `COMPASS_VERSION` property in `gradle.properties` (overridable via `-PCOMPASS_VERSION=...`).
  The workflow derives the version from the tag (`${GITHUB_REF_NAME#compass-v}`) and passes it as `-PCOMPASS_VERSION`.
- CI auth: ephemeral `GITHUB_TOKEN` with `permissions: { contents: read, packages: write }` — no PAT, no stored
  secret for same-repo publishing. Caveat: `gpr.user`/`gpr.key` properties (from `~/.gradle/gradle.properties` or the
  `GRADLE_PROPERTIES` CI secret) take precedence over those env vars when present.
- Publish repository URL fixed from `https://maven.pkg.github.com/marianjureczko/maly-poszukiwacz-skarbow` to
  `https://maven.pkg.github.com/mjureczko/maly-poszukiwacz-skarbow` — the actual repo owner (per `git remote origin`).
  Reason: the ephemeral `GITHUB_TOKEN` can only publish into the package namespace of its own repository, so the old
  URL (different owner) would make GITHUB_TOKEN-based publishing fail.
- Consumer-side (nowy-poszukiwacz) repository/dependency/PAT setup: documented in `docs/development.md`
  ("Module release" → "Consumer setup"); not implemented — separate repository.
- Publishing documentation consolidated into `docs/development.md` ("Module release"): content of modules.md's
  "Publishing & Distribution" + "Consumer setup" merged with the former "Module release" section — single source of
  truth; modules.md keeps only a short pointer. Local-publish procedure now includes a `-SNAPSHOT` example
  (`-PCOMPASS_VERSION=1.0.1-SNAPSHOT`) with the caveat that GitHub Packages has no Maven snapshot semantics — even
  SNAPSHOT versions are immutable, so each publish needs a unique version string (e.g. `1.0.1-SNAPSHOT-2`).

### Implementation Requirements for `:compass` module

To produce a `compass.aar` artifact, the module needs:

- **Build configuration**: Update `compass/build.gradle` with Hilt plugin, kapt, play-services-location dependency,
  correct SDK versions (36)
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
- [x] Move `Compass` and `Steps` Compose components + their `SearchingViewModel` logic into `:compass`
  (presentation, `internal` ViewModels, domain, data/GPS layers per modules.md encapsulation).
  See `docs/compass-migration-solution.md` for detailed plan.
- [x] Espresso tests works after compass modularisation
- [x] Path tracking work correctly after compass modularisation
- [x] Automatize module publication (distributed using GitHub Packages)
- [ ] Verify the publish workflow end-to-end by pushing the first tag (e.g. `compass-v1.0.0`) — user action; it
  triggers a real publication to GitHub Packages.

### Remaining for next session:

- [ ] Adopt a shared Gradle Version Catalog + Git submodule (per modules.md) so `compose_version`, `kotlin_version`,
  `hilt_version` are no longer `ext.*` in the root `build.gradle` but sourced from `libs.versions.toml`.

