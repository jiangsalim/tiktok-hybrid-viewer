#!/bin/bash

GATEWAY_URL="${1:-http://localhost:8080}"

echo "=== Backend Health Check ==="
echo "Gateway: $GATEWAY_URL"
echo ""

check_endpoint() {
    local url="$1"
    local name="$2"
    local response=$(curl -s -o /dev/null -w "%{http_code}" "$url" 2>/dev/null)
    if [ "$response" = "200" ]; then
        echo "✅ $name: OK (200)"
    else
        echo "❌ $name: FAILED ($response)"
    fi
}

check_endpoint "$GATEWAY_URL/api/v1/health" "Gateway Health"
check_endpoint "$GATEWAY_URL/api/v1/trending/global" "Trending Global"
check_endpoint "$GATEWAY_URL/api/v1/config" "Config"

echo ""
echo "=== Health check complete ==="
