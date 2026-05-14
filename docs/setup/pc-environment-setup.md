# PC Environment Setup Guide

## Prerequisites
- Windows/Mac/Linux PC with 8GB+ RAM
- 20GB free disk space
- Internet connection

## Step 1: Install Android Studio
- Download from developer.android.com/studio
- Run installer
- Select "Standard" installation
- This installs: Android SDK, SDK Platform, Emulator, Build Tools

## Step 2: Install Additional SDK Components
- Open Android Studio → SDK Manager
- Install:
  - Android SDK Platform 34, 35
  - Android SDK Build-Tools 35.0.0
  - NDK (Side by side) - Latest
  - CMake 3.22+

## Step 3: Install Rust for Android
```bash
rustup target add aarch64-linux-android
rustup target add armv7-linux-androideabi
rustup target add x86_64-linux-android
cargo install cargo-ndk
