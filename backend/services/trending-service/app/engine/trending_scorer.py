import math
from datetime import datetime, timezone
from typing import List, Dict, Any

class TrendingScorer:
    def __init__(self):
        self.view_weight = 0.4
        self.like_weight = 0.25
        self.share_weight = 0.2
        self.comment_weight = 0.1
        self.freshness_weight = 0.05

    def score_video(self, video: Dict[str, Any]) -> float:
        views = video.get("play_count", 0) or 0
        likes = video.get("like_count", 0) or 0
        shares = video.get("share_count", 0) or 0
        comments = video.get("comment_count", 0) or 0

        view_score = math.log10(views + 1) / 10
        like_score = math.log10(likes + 1) / 8
        share_score = math.log10(shares + 1) / 7
        comment_score = math.log10(comments + 1) / 6

        freshness = self._calculate_freshness(video.get("created_at"))

        total = (
            view_score * self.view_weight +
            like_score * self.like_weight +
            share_score * self.share_weight +
            comment_score * self.comment_weight +
            freshness * self.freshness_weight
        )
        return round(total, 4)

    def _calculate_freshness(self, created_at: str) -> float:
        if not created_at:
            return 0.5
        try:
            created = datetime.fromisoformat(created_at.replace("Z", "+00:00"))
            now = datetime.now(timezone.utc)
            age_hours = (now - created).total_seconds() / 3600
            decay = math.exp(-age_hours / 168)
            return decay
        except Exception:
            return 0.5

    def rank_videos(self, videos: List[Dict[str, Any]], limit: int = 100) -> List[Dict[str, Any]]:
        scored = [(self.score_video(v), v) for v in videos]
        scored.sort(key=lambda x: x[0], reverse=True)
        return [v for _, v in scored[:limit]]
