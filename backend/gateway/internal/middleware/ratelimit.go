package middleware

import (
"net/http"
"sync"
"time"
)

func RateLimit(next http.Handler) http.Handler {
return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
deviceID := r.Header.Get("X-Device-Id")
if deviceID == "" {
next.ServeHTTP(w, r)
return
}
if !allowRequest(deviceID) {
w.Header().Set("Content-Type", "application/json")
w.WriteHeader(http.StatusTooManyRequests)
w.Write([]byte(`{"error":"rate_limited","retry_after_ms":15000}`))
return
}
next.ServeHTTP(w, r)
})
}

var (
requestCounts = make(map[string][]time.Time)
mu            sync.Mutex
)

func allowRequest(deviceID string) bool {
mu.Lock()
defer mu.Unlock()
now := time.Now()
window := now.Add(-1 * time.Minute)
var recent []time.Time
for _, t := range requestCounts[deviceID] {
if t.After(window) {
recent = append(recent, t)
}
}
requestCounts[deviceID] = recent
if len(recent) >= 60 {
return false
}
requestCounts[deviceID] = append(requestCounts[deviceID], now)
return true
}
