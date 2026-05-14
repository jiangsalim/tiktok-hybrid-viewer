package handler

import (
"encoding/json"
"net/http"

"github.com/jiangsalim/tiktok-hybrid-viewer/peer-service/internal/service"
)

type PeerHandler struct {
registry       *service.PeerRegistry
router         *service.TaskRouter
trust          *service.TrustEngine
anomaly        *service.AnomalyDetector
}

func NewPeerHandler(r *service.PeerRegistry, tr *service.TaskRouter, te *service.TrustEngine, ad *service.AnomalyDetector) *PeerHandler {
return &PeerHandler{registry: r, router: tr, trust: te, anomaly: ad}
}

func (h *PeerHandler) Heartbeat(w http.ResponseWriter, r *http.Request) {
var req struct {
DeviceID    string `json:"device_id"`
BatteryPct  int    `json:"battery_pct"`
IsCharging  bool   `json:"is_charging"`
IsWifi      bool   `json:"is_wifi"`
IsScreenOff bool   `json:"is_screen_off"`
NetworkType string `json:"network_type"`
CurrentLoad string `json:"current_load"`
}
json.NewDecoder(r.Body).Decode(&req)
h.registry.Heartbeat(req.DeviceID, req.BatteryPct, req.IsCharging, req.IsWifi, req.IsScreenOff, req.NetworkType, req.CurrentLoad)
queued := 0
if h.router.QueueLength() > 0 {
queued = 1
}
json.NewEncoder(w).Encode(map[string]interface{}{
"status":                "ok",
"queued_tasks":          queued,
"heartbeat_interval_ms": 30000,
})
}

func (h *PeerHandler) Poll(w http.ResponseWriter, r *http.Request) {
var req struct {
DeviceID string `json:"device_id"`
}
json.NewDecoder(r.Body).Decode(&req)
task := h.router.PollTask(req.DeviceID)
if task == nil {
json.NewEncoder(w).Encode(map[string]string{"status": "no_tasks"})
return
}
json.NewEncoder(w).Encode(map[string]interface{}{
"status": "task_available",
"task": map[string]interface{}{
"task_id":         task.TaskID,
"encrypted_query": task.EncryptedQuery,
"created_at":      task.CreatedAt,
"ttl_seconds":     task.TTLSeconds,
},
})
}

func (h *PeerHandler) Submit(w http.ResponseWriter, r *http.Request) {
var req struct {
DeviceID  string `json:"device_id"`
TaskID    string `json:"task_id"`
Status    string `json:"status"`
Result    string `json:"result,omitempty"`
ErrorCode string `json:"error_code,omitempty"`
}
json.NewDecoder(r.Body).Decode(&req)
ok := h.router.SubmitResult(req.TaskID, req.DeviceID, req.Status, req.Result, req.ErrorCode)
if !ok {
json.NewEncoder(w).Encode(map[string]string{"status": "invalid_task"})
return
}
if req.Status == "completed" {
h.registry.UpdateScore(req.DeviceID, 0.02)
} else {
h.registry.UpdateScore(req.DeviceID, -0.10)
}
json.NewEncoder(w).Encode(map[string]string{"status": "accepted"})
}

func (h *PeerHandler) Request(w http.ResponseWriter, r *http.Request) {
var req struct {
RequesterID    string `json:"requester_id"`
EncryptedQuery string `json:"encrypted_query"`
TimeoutMs      int    `json:"timeout_ms"`
}
json.NewDecoder(r.Body).Decode(&req)
available := h.registry.GetAvailable()
if available == nil {
json.NewEncoder(w).Encode(map[string]interface{}{
"status": "no_peers_available",
"reason": "no_workers_online",
})
return
}
task := h.router.CreateTask(req.RequesterID, req.EncryptedQuery, req.TimeoutMs)
json.NewEncoder(w).Encode(map[string]interface{}{
"status":             "peer_assigned",
"task_id":            task.TaskID,
"estimated_wait_ms":  4500,
})
}

func (h *PeerHandler) Status(w http.ResponseWriter, r *http.Request) {
var req struct {
RequesterID string `json:"requester_id"`
TaskID      string `json:"task_id"`
}
json.NewDecoder(r.Body).Decode(&req)
task := h.router.GetTask(req.TaskID)
if task == nil {
json.NewEncoder(w).Encode(map[string]string{"status": "not_found"})
return
}
json.NewEncoder(w).Encode(map[string]interface{}{
"status": task.Status,
"result": task.Result,
"reason": task.ErrorCode,
})
}
