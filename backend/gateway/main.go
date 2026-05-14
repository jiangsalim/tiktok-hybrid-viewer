package main

import (
"log"
"net/http"
"os"

"github.com/jiangsalim/tiktok-hybrid-viewer/gateway/internal/config"
"github.com/jiangsalim/tiktok-hybrid-viewer/gateway/internal/router"
)

func main() {
cfg := config.Load()

r := router.Setup(cfg)

port := os.Getenv("PORT")
if port == "" {
port = "8080"
}

log.Printf("Gateway starting on port %s (host: %s)", port, cfg.Host)
if err := http.ListenAndServe(":"+port, r); err != nil {
log.Fatalf("Gateway failed to start: %v", err)
}
}
