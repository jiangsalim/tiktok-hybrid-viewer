package handler

import (
"encoding/json"
"net/http"

"github.com/jiangsalim/tiktok-hybrid-viewer/gateway/internal/config"
)

type HealthHandler struct {
cfg *config.Config
}

func NewHealthHandler(cfg *config.Config) *HealthHandler {
return &HealthHandler{cfg: cfg}
}

func (h *HealthHandler) Health(w http.ResponseWriter, r *http.Request) {
response := map[string]interface{}{
"host":    h.cfg.Host,
"status":  "healthy",
"version": "1.0.0",
}
if h.cfg.Host == "render" {
response["remaining_monthly_hours"] = nil
} else {
response["remaining_monthly_hours"] = nil
}
w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(response)
}
