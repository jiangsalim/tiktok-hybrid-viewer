# Quick Start

## 5-Minute Local Test
```bash
# Terminal 1: Gateway
cd backend/gateway && go run main.go

# Terminal 2: Test
curl http://localhost:8080/api/v1/health
