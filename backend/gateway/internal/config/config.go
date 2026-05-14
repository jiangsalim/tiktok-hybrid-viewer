package config

import "os"

type Config struct {
Host             string
PeerServiceURL   string
TrendingServiceURL string
ProxyServiceURL  string
TelemetryServiceURL string
SupabaseURL      string
SupabaseKey      string
}

func Load() *Config {
host := os.Getenv("RENDER")
if host == "" {
host = "local"
} else {
host = "render"
}

return &Config{
Host:             host,
PeerServiceURL:   getEnv("PEER_SERVICE_URL", "http://localhost:8081"),
TrendingServiceURL: getEnv("TRENDING_SERVICE_URL", "http://localhost:8082"),
ProxyServiceURL:  getEnv("PROXY_SERVICE_URL", "http://localhost:8083"),
TelemetryServiceURL: getEnv("TELEMETRY_SERVICE_URL", "http://localhost:8084"),
SupabaseURL:      os.Getenv("SUPABASE_URL"),
SupabaseKey:      os.Getenv("SUPABASE_ANON_KEY"),
}
}

func getEnv(key, fallback string) string {
if value := os.Getenv(key); value != "" {
return value
}
return fallback
}
