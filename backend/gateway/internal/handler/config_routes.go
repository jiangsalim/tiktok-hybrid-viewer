package handler

import (
"encoding/json"
"net/http"
)

type ConfigHandler struct{}

func NewConfigHandler() *ConfigHandler {
return &ConfigHandler{}
}

func (h *ConfigHandler) GetConfig(w http.ResponseWriter, r *http.Request) {
w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(map[string]interface{}{
"config": map[string]interface{}{
"features": map[string]interface{}{
"for_you": true, "explore": true, "following": false, "live": false,
},
},
})
}
