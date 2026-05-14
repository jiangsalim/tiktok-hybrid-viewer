package main

import (
"log"
"net/http"
"os"

"github.com/jiangsalim/tiktok-hybrid-viewer/peer-service/internal/handler"
"github.com/jiangsalim/tiktok-hybrid-viewer/peer-service/internal/service"
)

func main() {
peerRegistry := service.NewPeerRegistry()
taskRouter := service.NewTaskRouter(peerRegistry)
trustEngine := service.NewTrustEngine()
abuseDetector := service.NewAnomalyDetector()

h := handler.NewPeerHandler(peerRegistry, taskRouter, trustEngine, abuseDetector)

mux := http.NewServeMux()
mux.HandleFunc("/api/v1/peer/heartbeat", h.Heartbeat)
mux.HandleFunc("/api/v1/peer/poll", h.Poll)
mux.HandleFunc("/api/v1/peer/submit", h.Submit)
mux.HandleFunc("/api/v1/peer/request", h.Request)
mux.HandleFunc("/api/v1/peer/status", h.Status)

port := os.Getenv("PORT")
if port == "" {
port = "8081"
}
log.Printf("Peer Service starting on port %s", port)
log.Fatal(http.ListenAndServe(":"+port, mux))
}
