# ZeroLaunch — Ultra-Lightweight Native Android Launcher Project

This package contains the complete, zero-dependency Android source project (`< 500 KB` compiled APK size) for **ZeroLaunch**.

When compiled to an `.apk` and installed on your phone:
1. Pressing your phone's **Home button** opens **ZeroLaunch** (`android.intent.category.HOME`).
2. It automatically detects and lists **all real installed apps** on your Android phone via `PackageManager`.
3. Typing in the search bar **auto-launches your real phone apps** when a single match is found.
4. It reads **real hardware RAM usage** via Android's `ActivityManager.MemoryInfo`.

---

## How to Get Your `.apk` in 2 Minutes (Even Without a PC!)

### Option A: Compile in the Cloud via GitHub Actions (No PC or Android Studio Required)
1. Create a free repository on [GitHub](https://github.com/new) and upload the files from this ZIP.
2. Tap the **Actions** tab in your GitHub repository and run the **Build ZeroLaunch Android APK** workflow (it runs automatically on upload).
3. In ~90 seconds, download **`ZeroLaunch-Minimal-Launcher-APK` (`app-debug.apk`)** from the workflow summary page and install it on your phone!

### Option B: Build Locally in Android Studio / Command Line
1. Unzip this folder and open it in **Android Studio** (or run `gradle assembleDebug` in terminal).
2. Copy `app/build/outputs/apk/debug/app-debug.apk` to your phone and install it.
3. Go to **Android Settings → Apps → Default Apps → Home App** and select **ZeroLaunch**.
