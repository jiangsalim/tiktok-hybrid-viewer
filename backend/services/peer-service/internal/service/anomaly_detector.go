package service

import "strings"

type AnomalyDetector struct{}

func NewAnomalyDetector() *AnomalyDetector {
return &AnomalyDetector{}
}

type AnomalyResult struct {
IsAnomalous bool
Reasons     []string
}

func (ad *AnomalyDetector) AnalyzeSubmission(errorCode string, latencyMs int64) AnomalyResult {
result := AnomalyResult{}
if latencyMs < 500 && errorCode == "" {
result.IsAnomalous = true
result.Reasons = append(result.Reasons, "impossible_response_time")
}
if strings.Contains(errorCode, "captcha") || strings.Contains(errorCode, "blocked") {
result.IsAnomalous = true
result.Reasons = append(result.Reasons, "captcha_or_block")
}
return result
}
