# Manual and automated test plan

## Functional cases
| ID | Action | Expected |
| --- | --- | --- |
| T01 | Start app first time | Today screen with zero values, no crash |
| T02 | Tap each navigation tab | Matching screen and material navigation highlight |
| T03 | Create workout | Row appears, daily activity summary updates |
| T04 | Edit workout | Updated title and stats appear after database update |
| T05 | Delete workout | Confirmation prompt, row disappears after Delete |
| T06 | Add/edit/delete food | Calories/macros and dashboard react immediately |
| T07 | Add/edit/delete sleep | Duration across midnight calculated correctly |
| T08 | Add/edit/delete water | Water progress and history update |
| T09 | Save personal goals | New targets persist after app restart |
| T10 | Toggle dark mode | UI theme switches and remains after restart |
| T11 | Share report | Native Android sharesheet opens with text report |
| T12 | Show QR | QR bitmap is displayed and scannable |
| T13 | Enter invalid values | Validation prevents invalid database writes |
| T14 | Restart app | Existing data remains |
| T15 | Rotate device while on a tab | No crash or leaked view binding |
| T16 | Test TalkBack and long content | Important controls discoverable; app scrollable |

## Automated commands
- `./gradlew testDebugUnitTest` — 23 JUnit tests for validation, summary arithmetic and sleep math.
- `./gradlew connectedDebugAndroidTest` — Espresso add-workout happy path; emulator/device required.
- `./gradlew lintDebug` — Android static checks; `./gradlew ktlintFormat ktlintCheck` — Kotlin formatting/check.
- `./gradlew assembleRelease` — produce unsigned release APK (sign before publishing).

## Coverage
The rubric requires **at least 15% unit-test coverage of logic**. Tests cover the pure logic in `TrackerMath` and `Validation` but the precise percentage **must be measured on your machine** with JaCoCo and extended if needed; the starter has not been certified for that percentage.

## Profiler
Use Android Studio > Profiler > Memory and repeatedly navigate/open/dismiss dialogs. Look for growing retained Activities/Fragments after garbage collection; do not claim leak-free status before profiling.
