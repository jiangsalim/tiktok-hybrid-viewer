package handler

import (
"encoding/json"
"net/http"

"github.com/jiangsalim/tiktok-hybrid-viewer/gateway/internal/config"
)

type PeerHandler struct {
cfg *config.Config
}

func NewPeerHandler(cfg *config.Config) *PeerHandler {
return &PeerHandler{cfg: cfg}
}

func (h *PeerHandler) Heartbeat(w http.ResponseWriter, r *http.Request) {
w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(map[string]interface{}{
"status": "ok", "queued_tasks": 0, "heartbeat_interval_ms": 30000,
})
}

func (h *PeerHandler) Poll(w http.ResponseWriter, r *http.Request) {
w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(map[string]string{"status": "no_tasks"})
}

func (h *PeerHandler) Submit(w http.ResponseWriter, r *http.Request) {
w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(map[string]string{"status": "accepted"})
}

func (h *PeerHandler) Request(w http.ResponseWriter, r *http.Request) {
w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(map[string]interface{}{
"status": "peer_assigned", "task_id": "task_test123", "estimated_wait_ms": 4500,
})
}

func (h *PeerHandler) Status(w http.ResponseWriter, r *http.Request) {
w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(map[string]interface{}{
"status": "completed", "worker_assigned": true, "elapsed_ms": 3200,
})
}
