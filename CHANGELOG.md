# Changelog — aoler

All notable changes to aoler are documented here.
Format follows [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).
Nightly builds are automatically tagged; see the [releases page](https://github.com/tech-master33/aoler/releases) for APK downloads.

---

## [Unreleased]

Changes merged to `main` but not yet cut into a versioned release.

---

## [0.1.0] — 2025-06-01 (initial nightly)

### Added
- **LauncherActivity** — home screen replacing the stock Android launcher, optimised for screen reader users
- **AppDrawerActivity** — full scrollable app list with per-item content descriptions for TalkBack / andrdscren
- **AppAdapter** — single-column `RecyclerView` layout; large touch targets, linear navigation
- **AppInfo** — lightweight model holding app label, package name, and launch intent
- **AccessibilityHelper** — utility class providing screen reader integration helpers (announce, focus, scroll cues)
- `AndroidManifest.xml` with `CATEGORY_HOME` and `CATEGORY_DEFAULT` intent filters so Android recognises aoler as a launcher
- `activity_launcher.xml` and `item_app.xml` layouts with `accessibilityLiveRegion` and content description attributes
- `accessibility.xml` values file for shared string constants used by `AccessibilityHelper`
- GitHub Actions workflows:
  - `aoler-build.yml` — lint + build on every push to `main`
  - `aoler-nightly.yml` — nightly APK release (midnight UTC)
- Gradle wrapper (`gradlew`, `gradle-wrapper.jar`, `gradle-wrapper.properties`) for Gradle 8.2.1
- R8 minification + resource shrinking enabled for release builds via `proguard-rules.pro`
- Debug-keystore signing for nightly APKs — installable on any device without sideloading certs
- Integrated into BAOSP nightly bundle (`baosp-nightly.yml` job 3) alongside andrdscren and aotts

### Technical details
- minSdk 26 (Android 8.0), targetSdk 34 (Android 14)
- Kotlin + ViewBinding, JDK 17, AGP 8.2.2
- Package: `org.baosp.aoler`

---

## How the nightly build works

Each night at midnight UTC the `baosp-nightly.yml` workflow in the [baosp](https://github.com/tech-master33/baosp) repo:

1. Checks out the latest `main` of this repo
2. Runs `./gradlew clean assembleRelease` (R8-optimised, debug-signed)
3. Renames the APK to `aoler-<git-sha>.apk`
4. Uploads it as part of the combined BAOSP nightly release at  
   **[github.com/tech-master33/baosp/releases/tag/nightly](https://github.com/tech-master33/baosp/releases/tag/nightly)**

The standalone `aoler-nightly.yml` in this repo produces an independent nightly at  
**[github.com/tech-master33/aoler/releases/tag/nightly](https://github.com/tech-master33/aoler/releases/tag/nightly)**

[Unreleased]: https://github.com/tech-master33/aoler/compare/v0.1.0...HEAD
[0.1.0]: https://github.com/tech-master33/aoler/releases/tag/v0.1.0
