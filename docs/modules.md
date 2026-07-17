# Project Structure & Code Organization

The architecture utilizes the Integrated Subprojects approach.
Modules are built as separate folders within the main repository and compiled into standard Android Archive (.aar)
files.

* Local Development (maly-poszukiwacz-skarbow): The app consumes the module directly via implementation(project(":
  compass-feature")). This provides an instant feedback loop during development.
* External Consumption (nowy-poszukiwacz): The module is packaged into an .aar and consumed as a remote dependency.
* Encapsulation: The module utilizes standard Android packaging (data, domain, presentation). All internal logic,
  repositories, and ViewModels are marked with the internal visibility modifier. Only the public Composable wrapper is
  exposed to the host app.

# Dependency Management

Dependency versions are strictly synchronized across distinct repositories using a shared Gradle Version Catalog
combined with Git Submodules.

* The Single Source of Truth: A libs.versions.toml file defines all library and plugin versions (e.g., Compose BOM,
  Hilt).
* Distribution via Git Submodule: This TOML file is placed in a dedicated Git repository. Both maly-poszukiwacz-skarbow
  and nowy-poszukiwacz include this repository as a Git Submodule.
* Conflict Resolution: The .aar is compiled against the module's declared versions. If the consuming app requests a
  newer version (e.g., a newer Compose BOM), Gradle automatically upgrades the module's transitive dependencies to match
  the app, ensuring compatibility.

# Publishing & Distribution

The compiled .aar files are distributed using GitHub Packages.

* Pipeline: The maven-publish plugin exposes the release build variant of the module.
* Security: Accessing the packages requires a Personal Access Token (PAT) with package read/write permissions. These
  credentials must be stored locally in gradle.properties (ignored by Git) or provided via CI/CD environment variables
  to prevent token leakage.

# Dependency Injection (Hilt)

Hilt seamlessly merges dependency graphs across module boundaries, provided the host app also utilizes Hilt.

* The widget's ViewModel is annotated with @HiltViewModel.
* Inside the public Composable, the ViewModel is instantiated via hiltViewModel(). The host app is completely unaware of
  this initialization—it simply calls the Composable, and Hilt provisions the required internal ViewModel and its
  dependencies.

# Two-Way Data Flow & The LaunchedEffect Deep Dive

Communication between the host app's Screen and the module's Widget uses strictly unidirectional data flow and state
hoisting.

* Output (Module -> App): Handled via lambda functions (e.g., onWidgetOutput: (String) -> Unit). The widget calls this
  function, and the host app's Screen ViewModel processes the result.
* Input (App -> Module): Handled by passing data as parameters. This requires bridging the declarative UI (Compose) with
  the stateful backend (ViewModel) using LaunchedEffect.

## Deep Dive: Bridging App Data to the Widget's ViewModel

Here is the exact mechanism for passing data from the app into the widget's ViewModel:

```kotlin
   @Composable
fun CompassWidget(
    appInputData: String,
    viewModel: CompassViewModel = hiltViewModel()
) {
    // FLOW 1: App -> Module
    LaunchedEffect(appInputData) {
        viewModel.onExternalDataReceived(appInputData)
    }
}
```

Why is LaunchedEffect necessary?
In Compose, the CompassWidget function describes the UI.
If the host app changes appInputData, Compose triggers a "recomposition"—it runs the CompassWidget function again from
top to bottom with the new data.
If you called viewModel.onExternalDataReceived(appInputData) directly inside the Composable without LaunchedEffect, that
function would execute on every single frame rendering or minor state change.
This causes severe performance issues, infinite loops, and redundant processing.

How LaunchedEffect solves this:

1. The Key: The parameter passed to LaunchedEffect (appInputData) acts as a trigger key.
2. The Boundary: It creates a safe coroutine boundary between the UI and the ViewModel.
3. The Execution: When Compose runs the CompassWidget function, LaunchedEffect looks at the key.

* If appInputData is exactly the same as the last time, it ignores the block of code inside.
* If appInputData has changed, it cancels any previous execution of that block and runs it anew, feeding the fresh data
  safely into your ViewModel.

This guarantees your ViewModel only processes external data when the data actually changes, not just because the screen
happened to redraw.
 
# UI & Theming

The module is entirely theme-agnostic. It does not define its own typography, color palettes, or shape configurations.

* Inheritance: The module builds its UI using standard MaterialTheme references (e.g.,
  MaterialTheme.colorScheme.primary).
* Resolution: Because the host app wraps the widget inside its own Theme { ... } block, Compose's CompositionLocal
  system dynamically injects the app's specific colors and fonts into the widget at runtime.
* Overrides: For widget-specific elements that require rigid styling (e.g., a critical warning color), those colors are
  exposed as Composable parameters with sensible MaterialTheme fallbacks, allowing the host app to override them if
  necessary.
