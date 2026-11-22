# FitTrack

A native **offline-first fitness tracker** in Kotlin for activity, nutrition, sleep, hydration and personal daily goals.

> **Windows first run:** Read [FIRST_RUN_WINDOWS.md](FIRST_RUN_WINDOWS.md) and run `SETUP_WRAPPER_WINDOWS.bat` before importing into Android Studio. This downloads and verifies the official Gradle Wrapper JAR (not bundled).

> **Project status:** Android Studio source is included. This generated package was **not compiled in an Android SDK environment**. You must perform a local Gradle sync, build, emulator test and signed APK export before submitting.

## 1. Idea
People who want to improve fitness often track workouts, food, water and sleep in different places. FitTrack combines these daily records into one app, supporting user-selected goals and quick feedback without accounts or internet access.

## 2. How it works
The bottom navigation has five screens: **Today, Activity, Food, Sleep, Goals**. Activity records duration, workout type and burned calories. Food records meals, calories and macronutrients. Sleep records bedtime, wake time, wake-up date and subjective quality; its duration is calculated automatically across midnight. Today reads the Room database reactively and displays totals and progress against configurable goals. Water can be added, edited and removed from Today. Every main log supports Create, Read, Update and Delete through the UI, with confirmation before deletion. Goals and logs remain in the local Room SQLite database after a restart, while the chosen light/dark theme persists in app preferences. Users can explicitly share a daily text report via Android's Share Intent or display it as a QR code generated on device using ZXing. No account, internet access, sensor permission or API key is needed.

## 3. Technologies / architecture
| Part | Technology |
| --- | --- |
| Language | Kotlin (AGP 9 built-in Kotlin) + ktlint plugin 14.2.0 |
| SDK | `minSdk=24`, `compileSdk=37`, `targetSdk=37` |
| UI | Material Design 3 XML layouts + AppCompat fragments |
| Navigation | BottomNavigationView + FragmentManager |
| Pattern | MVVM + Repository + manual constructor dependency injection |
| Persistence | Room 2.8.4 / SQLite |
| Reactive updates | Room Flow → LiveData → fragments |
| Async writes | Kotlin Coroutines in ViewModel |
| QR | ZXing Core 3.5.4 |
| Sharing | Android `ACTION_SEND` Intent |
| Tests | JUnit 4, Espresso |

Full design: [Architecture](docs/ARCHITECTURE.md).

## 4. User flow
1. Launch → Today dashboard with defaults (2,200 kcal, 45 activity minutes, 8h sleep, 2,000 ml water).
2. Visit Activity/Food/Sleep, press Add entry, enter values and Save.
3. See new records, tap Edit to update and Delete to remove them.
4. Return to Today and monitor calculated progress; add water and manage History.
5. Visit Goals to change daily targets and theme.
6. Use Share report or Show QR to communicate daily results intentionally.

## 5. How to run
1. Install current Android Studio supporting AGP 9.5.x, **JDK 17**, and Android 17 **SDK 37**.
2. On Windows, run `SETUP_WRAPPER_WINDOWS.bat` once to install the official Gradle Wrapper JAR with checksum verification. Then File → Open → select this `FitTrack` folder (the folder containing `settings.gradle.kts`).
3. Allow Android Studio to download Gradle 9.6.0 and dependencies on the first sync (internet required for setup only).
4. Create an Android Virtual Device, preferably Pixel with Android API 35, 36 or 37, or connect a real Android 7.0+ device.
5. Click Run ▶ for `app`.
6. CLI (after syncing): `./gradlew assembleDebug` and `./gradlew testDebugUnitTest`.

> If your IDE prompts to install Android SDK components, accept. The official wrapper JAR must be installed by `SETUP_WRAPPER_WINDOWS.bat` first; see [Windows setup guide](FIRST_RUN_WINDOWS.md).

## 6. Tests / quality
- JUnit tests in `app/src/test/...` verify duration, summaries, clamping and input validation.
- Espresso test in `app/src/androidTest/...` adds a workout and checks it appears.
- Run `./gradlew testDebugUnitTest connectedDebugAndroidTest lintDebug` with a running emulator.
- Profiler: check fragment retention during repeated navigation before defense.
- **Target:** >=15% logic coverage (measure locally; not yet verified).
- Kotlin formatting: `./gradlew ktlintFormat` then `./gradlew ktlintCheck`. The plugin is configured, but a clean check still needs verification on your machine.

Detailed manual test cases: [Test plan](docs/TEST_PLAN.md).

## 7. Test accounts
FitTrack has no authentication; All functionalities are available immediately on a local installation.

## 8. APK
**Submission location:** [`apk/app-release.apk`](apk/README.md) (**required by rubric**, <=60 MB).

Source-only starter: an APK cannot be provided until this project is built and signed in Android Studio. Use **Build → Generate Signed App Bundle or APK → APK**, select `release`, then copy your generated APK to `apk/app-release.apk`. Never commit a private keystore or passwords.

## 9. GitHub submission and authorship
Every commit shows each step of the requirements. They are in the develop branch, which has it's specific branch rules. Folders .idea/ и local.properties are not included, because the history doesn't need to be filled with settings on local level.

## 10. Future development
- Calories burned and eaten are **user-entered estimates**, not auto-calculated from sensors or nutrition databases.
- The “logged net kcal” value is food minus logged exercise; it does **not** estimate basal metabolism or total energy expenditure.
- Sleep uses one latest record per wake-up date in the daily dashboard; multiple records can still be saved in the log.
- Dashboard totals are based on the device's local date and refresh on observation/screen reopen.
- No account, wearable integration, GPS, remote sync or professional medical guidance is provided.
