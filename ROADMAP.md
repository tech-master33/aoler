# aoler Roadmap

This document describes what we plan to build next, why each item matters,
and what state each one is in. It is updated as things change.

If you want to work on something listed here, open an issue or discussion first
so we can coordinate and avoid duplicated effort.

If something important to you is missing, open a feature request at
github.com/tech-master33/aoler/issues — the issue templates will ask you
the right questions.

---

## How to read this document

Each item has a status:

- **Planned** — we intend to build this but have not started
- **In progress** — actively being worked on
- **Needs help** — no one is currently assigned, good place to contribute
- **Done** — shipped and in the nightly build

Items are roughly ordered by priority within each section.

---

## Home screen

### Pinned favourites row — Planned

**What it is:** A short list of frequently used apps pinned to the top of the home screen,
reachable in one or two swipes from anywhere.

**Why it matters:** The current home screen shows all apps in a single scrollable column.
A user whose most-used app is near the bottom of an alphabetical list has to swipe past
dozens of items every time. Pinned favourites would put the apps they need most at the top,
always in the same position.

**Proposed approach:** Store up to five pinned apps in SharedPreferences. Show them in a
fixed section at the top of the `LauncherActivity` RecyclerView, separated from the main
list by a labelled divider that the screen reader announces ("Favourites, 3 items").
Long-press an app to add or remove it from favourites.

**Where to start:** `LauncherActivity.kt` and `AppAdapter.kt` — add a two-section
RecyclerView with a dedicated `ViewType` for pinned items.

---

### App search — Planned

**What it is:** Type to filter the app list as you search.

**Why it matters:** On a phone with many apps installed, scrolling through a full
alphabetical list to find one app takes a long time with a screen reader — every item
is announced as you pass it. A search field that filters the list in real time would
reduce dozens of swipes to a few keystrokes.

**Proposed approach:** Add a `SearchView` or `EditText` at the top of `AppDrawerActivity`.
Filter the adapter as the user types. Announce the number of results after each keystroke
("12 apps match"). Clear the search and return full list when the field is emptied.

---

### Recently used apps section — Planned

**What it is:** A small section at the top of the home screen showing the last three or
four apps the user opened, in the order they were used.

**Why it matters:** Most people use the same handful of apps most of the time.
Showing recently used apps means the user almost never has to scroll the full list.
Combined with pinned favourites, the home screen becomes much faster to navigate.

**Proposed approach:** Store a recency list in SharedPreferences, updated every time an app
is launched. Show the list above the main column on `LauncherActivity`.
Announce it as "Recently used: Phone, Messages, Browser" on first focus.

---

### Battery and time status bar — Planned

**What it is:** A non-visual status announcement — when the user focuses the top of the
home screen, the screen reader announces the current time and battery level.

**Why it matters:** Checking the time or battery requires navigating away from the home
screen to the status bar, which is difficult with a screen reader. Adding an announcement
at a known, consistent position on the home screen means the user can always get this
information in two swipes.

**Proposed approach:** A focusable view at the top of `LauncherActivity` with a content
description that reads the current time and battery percentage. Updated every minute.
No visual element is required — the view can be zero-height for sighted users.

---

## App drawer

### Alphabetical jump navigation — Planned

**What it is:** Swipe with two fingers to jump to the next letter group in the app list,
rather than moving one app at a time.

**Why it matters:** On a phone with 50+ apps, reaching "WhatsApp" from the top of an
alphabetical list takes 40+ swipes through every app in between. Letter-group jumping
reduces this to three or four swipes.

**Proposed approach:** Group the adapter data by first letter. A two-finger-swipe-right
gesture jumps focus to the first app in the next letter group. Announce the group letter
on jump ("W — 3 apps").

**Where to start:** `AppDrawerActivity.kt` and `AppAdapter.kt` — add `SectionIndexer`
and handle the jump gesture in the activity's `dispatchTouchEvent`.

---

### Sort order options — Needs help

**What it is:** Let users choose how the app list is sorted — alphabetically (current),
by most recently installed, or by most frequently used.

**Why it matters:** Alphabetical order is predictable but not always the fastest.
A user who installs apps regularly may prefer newest-first during setup.
A user who has settled into a routine may prefer most-used first.

**Proposed approach:** Add a sort preference to SharedPreferences with three values.
A long-press or settings gesture on the drawer opens a small menu to switch.
Announce the current sort order when the drawer opens ("App drawer, 47 apps, sorted
alphabetically").

---

### App information on long press — Planned

**What it is:** Long-pressing an app in the drawer announces its version, size,
and last used date, and offers a shortcut to its system settings page.

**Why it matters:** Finding out anything about an installed app currently requires
navigating to Settings → Apps, finding the app in that list, and reading its details —
easily 15 steps with a screen reader. A long-press shortcut reduces this to two steps.

---

## Gestures and navigation

### Gesture customisation — Planned

**What it is:** Let users reassign what each swipe direction does on the home screen and
in the app drawer.

**Why it matters:** The default gesture mappings work well for many people but not everyone.
A user with limited finger mobility may need simpler gestures. A power user may want
additional actions mapped to swipes.

**Proposed approach:** A settings screen listing each gesture (swipe up, swipe down,
two-finger swipe, long press) with a dropdown to choose its action. Settings stored in
SharedPreferences and applied in `LauncherActivity`'s `dispatchTouchEvent`.

---

### Keyboard and switch access navigation — Needs help

**What it is:** Navigate the full launcher — home screen and app drawer — using only
physical buttons, a Bluetooth keyboard, or a switch access device.

**Why it matters:** Some users cannot use a touchscreen at all. They use physical buttons,
a Bluetooth keyboard, or a switch device. Without explicit keyboard navigation support,
focus may behave unpredictably in aoler and some apps may be unreachable.

**Proposed approach:** Ensure every focusable element in `LauncherActivity` and
`AppDrawerActivity` has a correct `nextFocusDown`, `nextFocusUp`, `nextFocusRight`, and
`nextFocusLeft` set. Handle `KEYCODE_DPAD_*` and `KEYCODE_ENTER` explicitly for app launch.
Test with Switch Access enabled in Settings → Accessibility.

**Where to start:** `activity_launcher.xml` and `item_app.xml` — add `nextFocus*` attributes.

---

### Quick settings shortcut — Planned

**What it is:** A dedicated gesture from the home screen that opens the Android quick
settings panel (the one that appears when you pull down from the top of the screen).

**Why it matters:** Opening quick settings with a screen reader active requires a specific
two-finger swipe from the very top of the screen — a gesture that is hard to hit reliably.
A mapped shortcut from a known position makes it consistently reachable.

---

## Screen reader integration

### Notification badge announcements — Planned

**What it is:** When the screen reader focuses on an app icon, announce whether the app
has unread notifications and how many.

**Why it matters:** Blind users have no way to see the notification dots that appear on
app icons. They cannot know if they have unread messages or calls without opening each app.
Announcing the count on focus gives them the same information sighted users get at a glance.

**Proposed approach:** Query `NotificationListenerService` for unread counts per package
and include the count in each app item's content description —
"WhatsApp, 4 unread notifications".

**Dependencies:** Requires `BIND_NOTIFICATION_LISTENER_SERVICE` permission, which the user
must grant manually in Settings. Add a clear prompt with instructions on first run.

---

### Configurable announcement verbosity — Planned

**What it is:** Let users choose how much information the screen reader announces when
moving between apps — just the name, the name plus notification count, or the name plus
all available metadata.

**Why it matters:** Some users want maximum information on every focus event.
Others find detailed announcements slow and prefer just the app name.
Neither preference is wrong — the launcher should respect both.

---

## First-boot and setup

### First-run accessible setup wizard — Planned

**What it is:** When aoler is set as the default launcher for the first time, show a brief
guided setup — asking the user to confirm their screen reader and TTS engine, and walking
them through pinning their most-used apps.

**Why it matters:** A new user installing aoler has to discover its features on their own.
An accessible setup wizard — fully navigable by screen reader from the start — teaches the
key gestures (swipe up for drawer, swipe down for home) before the user needs them.

**Proposed approach:** Check a SharedPreferences flag on `LauncherActivity` startup.
If it is the first launch, start a `SetupWizardActivity` with a sequence of simple screens,
each with a single instruction and a "Done" button. The screen reader announces each screen's
purpose as it opens.

---

### Widget support — Long term

**What it is:** Allow Android app widgets to be placed on the home screen, with full
screen reader support for each widget's content.

**Why it matters:** Widgets give users at-a-glance information — time, weather, calendar
events — without opening an app. For a screen reader user this means fewer app launches
and less navigation. However, widgets are complex and can be made inaccessible easily.
This is a long-term item because it will only be added if it can be done accessibly.

**Status:** Not started. Will not be added until all higher-priority items are complete.
Any widget implementation must pass the same accessibility checklist as the rest of aoler.

---

## How priorities are set

Items move up the list when:

1. More users report being blocked by the missing feature
2. A contributor volunteers to lead the work
3. A dependency (another item on this list) is completed

Items are not added to this roadmap just because they are technically interesting.
Every item here has a stated impact on blind or disabled users. If you propose a feature,
the most important thing you can say is who it helps and how.
