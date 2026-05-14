#!/bin/bash
set -e

echo "=== TikTok Viewer Test Suite ==="
echo ""

echo "[1/3] Testing Go peer service..."
cd backend/services/peer-service
go test ./test/... -v 2>&1 | head -20
cd ../../..

echo ""
echo "[2/3] Testing Python trending service..."
cd backend/services/trending-service
python3 tests/test_trending_scorer.py
cd ../../..

echo ""
echo "[3/3] Testing Go telemetry service..."
cd backend/services/telemetry-service
go test ./internal/handler/... -v 2>&1 | head -10
cd ../../..

echo ""
echo "=== All tests complete ==="
