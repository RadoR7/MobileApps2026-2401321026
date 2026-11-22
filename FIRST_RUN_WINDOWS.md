# FitTrack — exact first-run instructions for Windows 11

## Why one setup step is necessary

The previous project export used a non-standard Gradle bootstrap. This revised bundle replaces that with a **verified download of the official Gradle 9.6.0 wrapper**. The official wrapper JAR is not bundled in the ZIP: it is downloaded once by `SETUP_WRAPPER_WINDOWS.bat` and its SHA-256 checksum is checked against Gradle’s published value. **Run the setup BAT before opening the project in Android Studio.** Do not skip it.

## Steps

1. Install **Android Studio Rabbit 2 (2026.2.2) or newer**, selecting Android SDK, emulator, and Android Virtual Device in the installer. Use Android Studio's **JDK 17** for Gradle.
2. Extract this ZIP to a short normal local directory (example: `C:\AndroidProjects\FitTrack`). Avoid opening the ZIP directly or using protected/cloud synced folders.
3. Double-click `SETUP_WRAPPER_WINDOWS.bat` **inside the FitTrack folder**. It downloads the official Gradle Wrapper JAR from the Gradle project on GitHub, verifies the official SHA-256 `497c8c2a7e5031f6aa847f88104aa80a93532ec32ee17bdb8d1d2f67a194a9c7`, and saves it at `gradle\wrapper\gradle-wrapper.jar`. A green success message means this step worked.
4. Start Android Studio. Choose **File → Open**; select the `FitTrack` folder that contains `settings.gradle.kts` (not its `app` subfolder). Trust the project only if you recognize its source.
5. Choose Android Studio **Settings → Build, Execution, Deployment → Build Tools → Gradle**; choose the embedded JDK 17 / JetBrains Runtime where offered. Turn off Gradle offline mode.
6. In **Tools → SDK Manager**, install Android SDK Platform **API 37** and build tools **36.0.0**, and ensure Platform Tools and Emulator are installed. Accept licenses.
7. **File → Sync Project with Gradle Files** and allow first-time downloads. This project's plugin versions are **AGP 9.5.1, Gradle 9.6.0, KSP 2.3.6**. Gradle sync needs internet initially.
8. In **Tools → Device Manager → Create Device**, select a Pixel device and a Google APIs Android **API 35 or 36** emulator image. Finish and start the emulator.
9. Select the `app` run configuration, click ▶ Run, and launch FitTrack. If `app` is not selectable, wait for successful Gradle sync.
10. Enter sample data on Activity, Food, Sleep and Today. Close/reopen FitTrack to verify persistence. Test Edit, Delete, Goals, Share Report, Show QR.
11. Open Android Studio Terminal in the FitTrack root and run ` .\gradlew.bat assembleDebug ` then ` .\gradlew.bat testDebugUnitTest ` (remove spaces around commands when typing). For Espresso UI test run ` .\gradlew.bat connectedDebugAndroidTest ` with the emulator running.
12. Debug APK output path: `app\build\outputs\apk\debug\app-debug.apk`. For submission create a **signed release APK** from Build → Generate Signed Bundle / APK → APK, then copy it to `apk\app-release.apk` and verify its size is <=60 MB.

## If SETUP_WRAPPER_WINDOWS.bat cannot download

From a browser, go to:
`https://github.com/gradle/gradle/blob/v9.6.0/gradle/wrapper/gradle-wrapper.jar`

Click **Download raw file**, save as `FitTrack\gradle\wrapper\gradle-wrapper.jar`, and confirm the filename is `.jar`, not `.jar.txt`. In PowerShell, from the FitTrack folder:

```
(Get-FileHash .\gradle\wrapper\gradle-wrapper.jar -Algorithm SHA256).Hash
```

It must equal `497c8c2a7e5031f6aa847f88104aa80a93532ec32ee17bdb8d1d2f67a194a9c7`.

**Do not** use `WrapperBootstrap.java` from the original package. This corrected package's `gradlew` and `gradlew.bat` now use the official `org.gradle.wrapper.GradleWrapperMain`.

## Common errors

- **Could not find or load `org.gradle.wrapper.GradleWrapperMain`**: complete setup step 3; the official JAR is absent.
- **Unsupported Android Gradle plugin**: update Android Studio to Rabbit 2 2026.2.2 or newer.
- **Failed to find SDK Platform 37**: install API 37 from SDK Manager; sync again.
- **Could not resolve dependencies / timeouts**: turn off Gradle Offline mode, confirm internet, allow access to Google Maven / Maven Central / Gradle plugins.
- **No devices**: start a virtual device via Device Manager, or enable USB debugging on a physical phone and install its Windows USB driver if needed.
- **No `app` in Run menu**: Gradle sync has not completed successfully; check Build output.
- **First launch crashes**: use Logcat and read the first `FATAL EXCEPTION` stack trace rather than guessing.

The application has not been compiled in an Android SDK in the export environment, so actual build/runtime success still needs local verification. Do not assume that a successful wrapper setup alone guarantees a bug-free app.
