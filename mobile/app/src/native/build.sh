#!/bin/bash
set -e

echo "=== Building TikTok Signer Native Library ==="

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
SIGNER_DIR="$SCRIPT_DIR/signer"
JNI_DIR="$SCRIPT_DIR/jni"
OUTPUT_DIR="$SCRIPT_DIR/../main/jniLibs"

cd "$SIGNER_DIR"

echo "[1/3] Building Rust static library for ARM64..."
cargo build --target aarch64-linux-android --release 2>/dev/null || {
    echo "ARM64 build via cargo failed, trying cargo-ndk..."
    cargo ndk --target aarch64-linux-android --platform 21 -- build --release
}

echo "[2/3] Building for ARMv7..."
cargo build --target armv7-linux-androideabi --release 2>/dev/null || {
    cargo ndk --target armv7-linux-androideabi --platform 21 -- build --release
}

echo "[3/3] Building for x86_64 (emulator)..."
cargo build --target x86_64-linux-android --release 2>/dev/null || {
    cargo ndk --target x86_64-linux-android --platform 21 -- build --release
}

echo "=== Native build complete ==="
echo "Rust libraries built. Link with C wrapper via CMakeLists.txt or Android.mk"
