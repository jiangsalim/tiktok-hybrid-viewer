package handler

import (
"encoding/json"
"net/http"
)

type TelemetryHandler struct{}

func NewTelemetryHandler() *TelemetryHandler {
return &TelemetryHandler{}
}

func (h *TelemetryHandler) ReportFailure(w http.ResponseWriter, r *http.Request) {
w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(map[string]string{"status": "logged"})
}

func (h *TelemetryHandler) ReportSuccess(w http.ResponseWriter, r *http.Request) {
w.Header().Set("Content-Type", "application/json")
json.NewEncoder(w).Encode(map[string]string{"status": "logged"})
}
