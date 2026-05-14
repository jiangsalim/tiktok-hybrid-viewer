package handler

import (
"encoding/json"
"net/http"
"strings"

"github.com/jiangsalim/tiktok-hybrid-viewer/gateway/internal/config"
)

type TrendingHandler struct {
cfg *config.Config
}

func NewTrendingHandler(cfg *config.Config) *TrendingHandler {
return &TrendingHandler{cfg: cfg}
}

func (h *TrendingHandler) GlobalTrending(w http.ResponseWriter, r *http.Request) {
w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(map[string]interface{}{
"status": "success", "videos": []interface{}{},
})
}

func (h *TrendingHandler) CountryTrending(w http.ResponseWriter, r *http.Request) {
code := strings.TrimPrefix(r.URL.Path, "/api/v1/trending/country/")
w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(map[string]interface{}{
"status": "success", "country": code, "videos": []interface{}{},
})
}

func (h *TrendingHandler) CategoryTrending(w http.ResponseWriter, r *http.Request) {
category := strings.TrimPrefix(r.URL.Path, "/api/v1/trending/category/")
w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(map[string]interface{}{
"status": "success", "category": category, "videos": []interface{}{},
})
}
