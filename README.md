# Barnaton-CMP

TV series catalog and trailer preview application built with Kotlin Multiplatform and Compose Multiplatform, targeting Android and iOS.

## Features

- Browse TV series by category (On Air, Popular, Top Rated) from The Movie Database (TMDB) API
- Search TV series via TMDB search endpoint
- Offline-first caching for TV lists and local favorites using Room KMP + SQLite
- Series detail view with official trailers and clips fetched from `/tv/{id}/videos`
- Multiplatform video preview player via `expect`/`actual` (`Media3 ExoPlayer` on Android, `AVPlayer` on iOS)

## Tech Stack

- Kotlin `2.3.20`
- Compose Multiplatform `1.10.3` (Material 3)
- Ktor Client `3.1.3` (`okhttp` on Android, `darwin` on iOS) + `kotlinx-serialization`
- Koin `4.2.1` (dependency injection & Compose ViewModel)
- Room KMP `2.8.4` + `sqlite-bundled` `2.6.2`
- Coil `3.1.0` (`coil-compose` + `coil-network-ktor3`)
- Navigation Compose Multiplatform `2.9.2`
- AndroidX Media3 `1.3.1` (`ExoPlayer` + HLS support on Android)
- Apple `AVFoundation` / `AVKit` (`AVPlayerViewController` on iOS)

## Project Structure

```text
Barnaton-CMP/
├── composeApp/
│   └── src/
│       ├── commonMain/
│       │   └── kotlin/
│       │       ├── data/        # Local Room DB, Remote Ktor DataSource, Repository, NetworkBoundResource
│       │       ├── di/          # Koin modules & platform expect module
│       │       ├── domain/      # Domain models & UseCases
│       │       ├── ui/          # Compose screens (Home, Favorite, Detail), components, expect VideoPlayer
│       │       └── utils/       # DataMapper, NetworkError, Result
│       ├── androidMain/         # MainActivity, Application, Room builder, Media3 VideoPlayer actual
│       └── iosMain/             # MainViewController, Room builder, AVPlayer VideoPlayer actual
└── iosApp/                      # Xcode project wrapping ComposeUIViewController in SwiftUI
```

## Configuration

The TMDB API key is read from `local.properties` at build time and generated into `AppConfig`.

1. Copy the example configuration file in the project root:
   - Windows (PowerShell):
     ```powershell
     Copy-Item local.properties.example local.properties
     ```
   - macOS / Linux:
     ```bash
     cp local.properties.example local.properties
     ```
2. Open `local.properties` and set your TMDB v3 API key (and Android SDK path if not set by Android Studio):
   ```properties
   sdk.dir=/path/to/Android/Sdk
   TMDB_API_KEY=your_tmdb_api_key_here
   ```

`local.properties` is gitignored and will not be committed. You can also pass `TMDB_API_KEY` as an environment variable.

## Build and Run

### Android

- Build debug APK:
  - Windows:
    ```powershell
    .\gradlew.bat :composeApp:assembleDebug
    ```
  - macOS / Linux:
    ```bash
    ./gradlew :composeApp:assembleDebug
    ```
- Or open the project in Android Studio and run the `composeApp` configuration on an Android emulator/device (`minSdk 24`, `compileSdk 36`).

### iOS

1. Open `iosApp/iosApp.xcodeproj` in Xcode on macOS (or use the iOS run configuration in Android Studio / Fleet with KMP plugin).
2. Select an iOS simulator or device (`iOS 15.3+`) and run. Xcode triggers `:composeApp:embedAndSignAppleFrameworkForXcode` during the build phase.
