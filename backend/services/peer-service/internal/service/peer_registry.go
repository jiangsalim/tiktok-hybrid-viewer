package service

import (
"sync"
"time"
)

type Peer struct {
DeviceID       string    `json:"device_id"`
Status         string    `json:"status"`
BatteryPct     int       `json:"battery_pct"`
IsCharging     bool      `json:"is_charging"`
IsWifi         bool      `json:"is_wifi"`
IsScreenOff    bool      `json:"is_screen_off"`
NetworkType    string    `json:"network_type"`
CurrentLoad    string    `json:"current_load"`
LastHeartbeat  time.Time `json:"last_heartbeat"`
TrustScore     float64   `json:"trust_score"`
SuccessCount   int       `json:"success_count"`
FailureCount   int       `json:"failure_count"`
TasksCompleted int       `json:"tasks_completed"`
}

type PeerRegistry struct {
mu     sync.RWMutex
peers  map[string]*Peer
}

func NewPeerRegistry() *PeerRegistry {
pr := &PeerRegistry{
peers: make(map[string]*Peer),
}
go pr.cleanupLoop()
return pr
}

func (pr *PeerRegistry) Heartbeat(deviceID string, battery int, charging, wifi, screenOff bool, networkType, load string) {
pr.mu.Lock()
defer pr.mu.Unlock()
peer, exists := pr.peers[deviceID]
if !exists {
peer = &Peer{
DeviceID:    deviceID,
Status:      "idle",
TrustScore:  0.5,
}
pr.peers[deviceID] = peer
}
peer.BatteryPct = battery
peer.IsCharging = charging
peer.IsWifi = wifi
peer.IsScreenOff = screenOff
peer.NetworkType = networkType
peer.CurrentLoad = load
peer.LastHeartbeat = time.Now()
if peer.Status != "busy" {
peer.Status = "idle"
}
}

func (pr *PeerRegistry) GetAvailable() *Peer {
pr.mu.RLock()
defer pr.mu.RUnlock()
var best *Peer
for _, peer := range pr.peers {
if peer.Status != "idle" {
continue
}
if peer.BatteryPct < 50 {
continue
}
if !peer.IsWifi && !peer.IsCharging {
continue
}
if best == nil || peer.TrustScore > best.TrustScore {
best = peer
}
}
return best
}

func (pr *PeerRegistry) Get(deviceID string) *Peer {
pr.mu.RLock()
defer pr.mu.RUnlock()
return pr.peers[deviceID]
}

func (pr *PeerRegistry) SetStatus(deviceID, status string) {
pr.mu.Lock()
defer pr.mu.Unlock()
if peer := pr.peers[deviceID]; peer != nil {
peer.Status = status
}
}

func (pr *PeerRegistry) UpdateScore(deviceID string, delta float64) {
pr.mu.Lock()
defer pr.mu.Unlock()
if peer := pr.peers[deviceID]; peer != nil {
peer.TrustScore += delta
if peer.TrustScore > 1.0 {
peer.TrustScore = 1.0
}
if peer.TrustScore < 0.0 {
peer.TrustScore = 0.0
}
}
}

func (pr *PeerRegistry) cleanupLoop() {
for {
time.Sleep(30 * time.Second)
pr.mu.Lock()
cutoff := time.Now().Add(-60 * time.Second)
for id, peer := range pr.peers {
if peer.LastHeartbeat.Before(cutoff) && peer.Status != "busy" {
peer.Status = "offline"
peer.TrustScore *= 0.99
if peer.TrustScore < 0.1 {
delete(pr.peers, id)
}
}
}
pr.mu.Unlock()
}
}
