#!/bin/bash
echo "Building native libraries..."
cd app/src/native/signer
cargo build --target aarch64-linux-android --release
echo "Native build complete"
