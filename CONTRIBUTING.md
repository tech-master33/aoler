# Contributing to aoler

Thank you for contributing to aoler, the accessible home screen launcher built for BAOSP.
This guide is written to work well with screen readers and keyboard-only navigation.
Every step is numbered and linear — no visual layout is assumed.

---

## Before you start

You need:

1. A GitHub account — create one free at github.com/join
2. Git installed on your computer — download at git-scm.com
3. Android Studio or a text editor such as VS Code
4. Java 17 — download at adoptium.net
5. Android SDK with API level 34 — installed automatically by Android Studio

---

## About this project

aoler replaces the Android home screen with one built for screen reader users.
The project has three main parts:

- `app/src/main/java/org/baosp/aoler/` — all Kotlin source code
  - `LauncherActivity.kt` — the home screen itself; what the user sees when they press Home
  - `AppDrawerActivity.kt` — the full app list, opened by swiping up
  - `AppAdapter.kt` — the RecyclerView adapter that drives the app list
  - `AppInfo.kt` — data model holding each app's label, package name, and intent
  - `AccessibilityHelper.kt` — helpers that announce content and manage focus for screen readers
- `app/src/main/res/` — XML layouts, string resources, and accessibility values
- `app/src/main/AndroidManifest.xml` — registers aoler as a home screen launcher

---

## Step 1 — Fork the repository

Forking makes a personal copy of the code under your own GitHub account.

1. Open github.com/tech-master33/aoler
2. Activate the Fork button near the top of the page
3. On the next screen, activate Create fork
4. GitHub takes you to your copy at github.com/YOUR-USERNAME/aoler

---

## Step 2 — Clone your fork to your computer

Open a terminal and run these commands one at a time.
Replace YOUR-USERNAME with your actual GitHub username.

```bash
git clone https://github.com/YOUR-USERNAME/aoler.git
cd aoler
git remote add upstream https://github.com/tech-master33/aoler.git
```

Running `git remote -v` should now show both `origin` (your fork) and `upstream` (the main repo).

---

## Step 3 — Create a branch for your change

Never commit directly to `main`. Create a new branch first.

```bash
git checkout -b your-branch-name
```

Name the branch something descriptive. Examples:

- `fix/double-tap-launches-wrong-app`
- `feature/long-press-app-menu`
- `docs/update-install-steps`
- `a11y/improve-focus-order`

---

## Step 4 — Make your changes

Open the project in Android Studio or your editor.
The project uses Kotlin and ViewBinding. Key files to know:

- `LauncherActivity.kt` — handles the home screen, swipe gestures, and initial app list
- `AppDrawerActivity.kt` — full scrollable app list; open via swipe up from home
- `AppAdapter.kt` — sets content descriptions on each app item so the screen reader announces names
- `AccessibilityHelper.kt` — call `announce()` here to speak text without changing focus

### Accessibility rules for this project

aoler exists specifically for blind and visually impaired users.
Every change must follow these rules:

1. Every interactive element must have a content description — buttons, list items, and icons
2. Touch targets must be at least 48dp wide and 48dp tall — do not make them smaller
3. The focus order when swiping through the screen must be top-to-bottom, left-to-right
4. Do not use color alone to communicate information — also use text or shape
5. When an action completes (app launched, list scrolled), announce it via `AccessibilityHelper`
6. Do not add animations that cannot be disabled via the system Reduce Motion setting
7. Test every change with a screen reader turned on before submitting

---

## Step 5 — Build and test locally

```bash
chmod +x gradlew
./gradlew assembleDebug
```

The APK will be at:

```
app/build/outputs/apk/debug/app-debug.apk
```

To install on a connected Android device:

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

After installing, press the Home button on the device.
Android will ask which launcher to use — select aoler and tap Always.

### Manual testing checklist

Go through each item before submitting your pull request:

- The app builds without errors or warnings
- Pressing Home opens the aoler launcher, not the previous launcher
- The screen reader announces each app name when you touch or swipe to it
- Double-tapping an app launches it correctly
- Swiping up opens the app drawer and the screen reader announces it
- Swiping down from the drawer returns to the home screen
- All apps in the drawer are reachable by swiping — none are skipped
- Rotating the screen does not crash the launcher or lose focus
- The launcher survives a device restart and is still the default home app
- Testing with andrdscren enabled produces the same behaviour as TalkBack

---

## Step 6 — Commit your changes

Write a commit message that clearly explains what changed and why.

```bash
git add .
git commit -m "fix: app drawer does not announce item count on open

When the app drawer opened, the screen reader had no way to know
how many apps were in the list. Added an accessibility announcement
with the count when AppDrawerActivity starts."
```

Commit message format:

```
type: short summary in plain English

Longer explanation if needed. Explain the problem, not just the fix.
```

Types: `fix`, `feature`, `docs`, `refactor`, `a11y`, `build`

---

## Step 7 — Push and open a pull request

```bash
git push origin your-branch-name
```

Then:

1. Open github.com/YOUR-USERNAME/aoler
2. GitHub shows a bar saying your branch was recently pushed
3. Activate Compare and pull request
4. Fill in the title: one sentence describing the change
5. Fill in the description: what problem does this solve, how did you test it
6. Activate Create pull request

---

## Reporting a bug

1. Open github.com/tech-master33/aoler/issues
2. Activate New issue
3. Fill in the title with a short description of the problem
4. In the body, include:
   - What you were doing when the problem happened
   - What you expected to happen
   - What actually happened
   - Your Android version and device model
   - Which screen reader you were using (TalkBack, andrdscren, or other)
   - Whether the problem happens every time or only sometimes

---

## Getting help

Open a discussion at github.com/tech-master33/aoler/discussions
and describe your question. You do not need to know the solution — just describe what you are trying to do.
