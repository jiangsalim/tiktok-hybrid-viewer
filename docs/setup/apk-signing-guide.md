# APK Signing Guide

## Create Keystore (One Time)
```bash
keytool -genkey -v -keystore tiktok-viewer.keystore \
  -alias tiktokviewer -keyalg RSA -keysize 2048 \
  -validity 10000 -storepass YOUR_PASSWORD
