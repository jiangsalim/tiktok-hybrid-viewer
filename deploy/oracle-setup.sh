#!/bin/bash
set -e

echo "=== Oracle Cloud Always Free Setup ==="
echo ""

ORACLE_IP="${1}"
if [ -z "$ORACLE_IP" ]; then
    echo "Usage: bash oracle-setup.sh <VM_PUBLIC_IP>"
    exit 1
fi

echo "Connecting to Oracle VM at $ORACLE_IP..."
echo ""

echo "Run these commands on the Oracle VM:"
echo ""
echo "# Update system"
echo "sudo apt update && sudo apt upgrade -y"
echo ""
echo "# Install Docker"
echo "curl -fsSL https://get.docker.com | sudo bash"
echo "sudo usermod -aG docker ubuntu"
echo ""
echo "# Install Docker Compose"
echo "sudo apt install docker-compose -y"
echo ""
echo "# Clone project"
echo "git clone https://github.com/jiangsalim/tiktok-hybrid-viewer.git"
echo "cd tiktok-hybrid-viewer"
echo ""
echo "# Start all services"
echo "docker-compose up -d"
echo ""
echo "# Check status"
echo "docker-compose ps"
echo "curl http://localhost:8080/api/v1/health"
