package router

import (
"net/http"

"github.com/jiangsalim/tiktok-hybrid-viewer/gateway/internal/config"
"github.com/jiangsalim/tiktok-hybrid-viewer/gateway/internal/handler"
"github.com/jiangsalim/tiktok-hybrid-viewer/gateway/internal/middleware"
)

func Setup(cfg *config.Config) http.Handler {
mux := http.NewServeMux()

healthH := handler.NewHealthHandler(cfg)
peerH := handler.NewPeerHandler(cfg)
trendingH := handler.NewTrendingHandler(cfg)
proxyH := handler.NewProxyHandler(cfg)
telemetryH := handler.NewTelemetryHandler()
configH := handler.NewConfigHandler()

mux.HandleFunc("/api/v1/health", healthH.Health)
mux.HandleFunc("/api/v1/peer/heartbeat", peerH.Heartbeat)
mux.HandleFunc("/api/v1/peer/poll", peerH.Poll)
mux.HandleFunc("/api/v1/peer/submit", peerH.Submit)
mux.HandleFunc("/api/v1/peer/request", peerH.Request)
mux.HandleFunc("/api/v1/peer/status", peerH.Status)
mux.HandleFunc("/api/v1/trending/global", trendingH.GlobalTrending)
mux.HandleFunc("/api/v1/trending/country/", trendingH.CountryTrending)
mux.HandleFunc("/api/v1/trending/category/", trendingH.CategoryTrending)
mux.HandleFunc("/api/v1/proxy/search", proxyH.ProxySearch)
mux.HandleFunc("/api/v1/proxy/thumbnail", proxyH.ProxyThumbnail)
mux.HandleFunc("/api/v1/telemetry/failure", telemetryH.ReportFailure)
mux.HandleFunc("/api/v1/telemetry/success", telemetryH.ReportSuccess)
mux.HandleFunc("/api/v1/config", configH.GetConfig)
mux.HandleFunc("/api/v1/signature/check", func(w http.ResponseWriter, r *http.Request) {
w.Header().Set("Content-Type", "application/json")
w.Write([]byte(`{"status":"ok","update_available":false}`))
})

return middleware.Chain(mux, middleware.Logging, middleware.CORS)
}
