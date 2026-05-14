package handler

import (
"encoding/json"
"net/http"

"github.com/jiangsalim/tiktok-hybrid-viewer/gateway/internal/config"
)

type ProxyHandler struct {
cfg *config.Config
}

func NewProxyHandler(cfg *config.Config) *ProxyHandler {
return &ProxyHandler{cfg: cfg}
}

func (h *ProxyHandler) ProxySearch(w http.ResponseWriter, r *http.Request) {
w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(map[string]interface{}{
"status": "success", "videos": []interface{}{},
})
}

func (h *ProxyHandler) ProxyThumbnail(w http.ResponseWriter, r *http.Request) {
w.Header().Set("Content-Type", "image/jpeg")
w.WriteHeader(http.StatusOK)
}
