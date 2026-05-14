package router

import (
"net/http"

"github.com/jiangsalim/tiktok-hybrid-viewer/gateway/internal/config"
"github.com/jiangsalim/tiktok-hybrid-viewer/gateway/internal/handler"
"github.com/jiangsalim/tiktok-hybrid-viewer/gateway/internal/middleware"
)

func Setup(cfg *config.Config) http.Handler {
mux := http.NewServeMux()

healthHandler := handler.NewHealthHandler(cfg)
peerHandler := handler.NewPeerHandler(cfg)

mux.HandleFunc("/api/v1/health", healthHandler.Health)
mux.HandleFunc("/api/v1/peer/heartbeat", peerHandler.Heartbeat)
mux.HandleFunc("/api/v1/peer/poll", peerHandler.Poll)
mux.HandleFunc("/api/v1/peer/submit", peerHandler.Submit)
mux.HandleFunc("/api/v1/peer/request", peerHandler.Request)
mux.HandleFunc("/api/v1/peer/status", peerHandler.Status)

return middleware.Chain(
mux,
middleware.Logging,
middleware.CORS,
middleware.RateLimit,
)
}
