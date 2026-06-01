# aoler — BAOSP Accessible Launcher

[![aoler Build](https://github.com/tech-master33/aoler/actions/workflows/aoler-build.yml/badge.svg)](https://github.com/tech-master33/aoler/actions/workflows/aoler-build.yml)
[![aoler Nightly](https://github.com/tech-master33/aoler/actions/workflows/aoler-nightly.yml/badge.svg)](https://github.com/tech-master33/aoler/actions/workflows/aoler-nightly.yml)

A home screen launcher built from the ground up for blind and visually impaired users. Part of the [BAOSP](https://github.com/tech-master33/baosp) ecosystem.

## Download — Latest Nightly Build

**→ [github.com/tech-master33/aoler/releases/tag/nightly](https://github.com/tech-master33/aoler/releases/tag/nightly)**

Updated automatically every night.

| File | Description |
|------|-------------|
| `aoler-*.apk` | Accessible launcher — install and set as default home app |

## What is aoler?

aoler replaces the standard Android home screen with one built for screen reader users:

- **Large touch targets** — every app icon is large enough to hit without precise aiming
- **Linear grid layout** — apps arranged in a single scrollable column, no 2D grid to navigate
- **Spoken app names on focus** — the screen reader announces each app as you touch it
- **Quick launch gestures** — swipe up to open the app drawer, swipe down to go to the first app
- **No visual clutter** — no widgets, no wallpaper overlays, no hidden folders
- **Works with andrdscren** — designed to pair with the BAOSP screen reader out of the box

## Installing on your Android device

1. Download `aoler-*.apk` from the [nightly release](https://github.com/tech-master33/aoler/releases/tag/nightly)
2. Transfer to your device and install (allow "unknown sources" if prompted)
3. Press the Home button — Android will ask which launcher to use
4. Select **aoler** and tap **Always**

To switch back to your previous launcher: Settings → Apps → Default apps → Home app.

## Related repos

| Repo | Description |
|------|-------------|
| [baosp](https://github.com/tech-master33/baosp) | Main BAOSP project — AOSP build, patches, nightly APK bundle |
| [andrdscren](https://github.com/tech-master33/andrdscren) | Screen reader source |
| [aotts](https://github.com/tech-master33/aotts) | TTS engine source |

## Build System

| Workflow | What it does | Trigger |
|----------|-------------|---------|
| **aoler Nightly** | Builds APK and posts as a release | Every night + manual |
| **aoler Build** | Full build with lint and tests | Push to main + manual |

### Building locally

```bash
git clone https://github.com/tech-master33/aoler.git
cd aoler
./gradlew clean assembleDebug
# APK at: app/build/outputs/apk/debug/
```

Requires JDK 17 and Android SDK with API level 34.

## Repository structure

```
aoler/
├── .github/
│   └── workflows/
│       ├── aoler-build.yml     ← CI build with lint + tests (push to main)
│       └── aoler-nightly.yml   ← Nightly APK release (automatic)
├── app/
│   ├── src/main/
│   │   ├── java/org/baosp/aoler/
│   │   │   ├── LauncherActivity.kt      ← Main home screen activity
│   │   │   ├── AppDrawerActivity.kt     ← Full app list
│   │   │   ├── AppAdapter.kt            ← RecyclerView adapter for app list
│   │   │   └── AccessibilityHelper.kt  ← Screen reader integration helpers
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_launcher.xml
│   │   │   │   └── item_app.xml
│   │   │   └── values/
│   │   │       ├── strings.xml
│   │   │       └── accessibility.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
├── settings.gradle
├── gradle.properties
├── gradlew
├── gradlew.bat
└── README.md
```

## License

Apache License 2.0 — same as BAOSP and AOSP.
