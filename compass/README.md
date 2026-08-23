# Compass module

## Module responsibilities

- Shows on UI compass with rotating needle showing direction to selected target.
- Shows on UI distance in steps to given target.
- Offers an API to calculate distance to given target.
- Fetches the user's GPS location (via `LocationPort`).

## API

All public API lives in the `pl.marianjureczko.poszukiwacz.compass.api` package.
Anything outside that package is considered internal and **must not** be referenced by consumers.

### Hilt / DI requirements

- The module expects to be used with Hilt — its `CompassViewModel`, `LocationCalculator`, `UpdateLocationUC`  and
  `ArcCalculator` are obtained through injection.
- The consuming app must provide Hilt bindings for:
  - A `CoroutineDispatcher` qualified with `@CompassIoDispatcher`.
  - A `CoroutineDispatcher` qualified with `@CompassMainDispatcher`.
  - `LocationPort` (contains a factory method that can deliver a alid implementation).
  - A `FusedLocationProviderClient` (used by the default `LocationPort` binding).

### `AndroidLocation` (in `compass.api`)

An interface representing a geographic location. Its concrete implementation (`LocationWrapper` ) is internal.

Properties:

- `latitude: Double`
- `longitude: Double`
- `accuracy: Float` - estimated accuracy radius in meters.
- `observedAt: Long` - time of observation, in milliseconds since epoch.

### `LocationPort` (in `compass.api`)

Interface for fetching the user's GPS location.
The `ACCESS_FINE_LOCATION` permission must be granted by the consumer before calling `startFetching`.

### `LocationUpdateCallback` (in `compass.api`)

`fun interface` for receiving `AndroidLocation` updates.
So that the locaion updates may be used outside the module:

```kotlin
LocationUpdateCallback { location -> /* ... */ }
```

### `LocationCalculator` (in `compass.api`)

Provides step-distance and meter calculations over two `AndroidLocation` values. It is the **single source of truth for
step-distance calculations**.

### `CompassAndSteps` composable (in `compass.api`)

The main entry point for embedding the compass + steps UI.
Uses internally `hiltViewModel()`.
