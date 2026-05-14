# Full Build Checklist

## PC Setup
- [ ] Android Studio installed
- [ ] SDK 34, 35 installed
- [ ] NDK installed
- [ ] Rust with Android targets
- [ ] Project cloned from GitHub

## Build
- [ ] Open mobile/ in Android Studio
- [ ] Gradle sync completes
- [ ] Native libraries built (bash build-native.sh)
- [ ] Debug APK builds
- [ ] Install on test device
- [ ] App launches without crash

## Test
- [ ] Search returns videos
- [ ] For You feed loads
- [ ] Video playback works
- [ ] Swipe navigation works
- [ ] Share sheet opens
- [ ] Settings navigate correctly
- [ ] Offline mode shows cached content

## Release
- [ ] Keystore created
- [ ] Release APK signed
- [ ] Upload to APKPure
- [ ] Submit for review
