# Required release APK

The assessment requires a real signed release APK at `apk/app-release.apk` with a size <=60MB.
This archive is source-only: **do not rename a debug APK to a release APK**.
In Android Studio: Build → Generate Signed App Bundle or APK → APK → create/select a secure signing key → release → Finish.
Copy the built `app-release.apk` into this folder, install it on your test phone/emulator, and commit the APK to your submission repository.
