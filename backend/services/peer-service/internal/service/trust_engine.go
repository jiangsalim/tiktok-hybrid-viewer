package service

type TrustEngine struct{}

func NewTrustEngine() *TrustEngine {
return &TrustEngine{}
}

func (te *TrustEngine) CalculateScore(successCount, failureCount int, avgResponseMs int64, ageDays float64) float64 {
total := successCount + failureCount
if total == 0 {
return 0.5
}
successRate := float64(successCount) / float64(total)
responseScore := 1.0
if avgResponseMs > 10000 {
responseScore = 0.5
} else if avgResponseMs > 5000 {
responseScore = 0.75
}
ageScore := 1.0
if ageDays < 1 {
ageScore = 0.7
} else if ageDays < 7 {
ageScore = 0.85
}
return successRate * 0.5 + responseScore * 0.3 + ageScore * 0.2
}

func (te *TrustEngine) GetTier(score float64) string {
switch {
case score >= 0.8:
return "trusted"
case score >= 0.5:
return "neutral"
case score >= 0.2:
return "probation"
default:
return "blocked"
}
}
