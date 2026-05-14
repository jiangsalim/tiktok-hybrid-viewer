import sys
import os
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..'))

def test_score_video():
    from app.engine.trending_scorer import TrendingScorer
    scorer = TrendingScorer()
    video = {
        "play_count": 1000000,
        "like_count": 50000,
        "share_count": 10000,
        "comment_count": 5000,
        "created_at": "2026-05-14T00:00:00Z"
    }
    score = scorer.score_video(video)
    assert score > 0, f"Score should be positive, got {score}"
    assert score <= 1.0, f"Score should be <= 1.0, got {score}"
    print(f"PASS: Video score = {score}")

def test_rank_videos():
    from app.engine.trending_scorer import TrendingScorer
    scorer = TrendingScorer()
    videos = [
        {"play_count": 1000, "like_count": 100, "share_count": 50, "comment_count": 20},
        {"play_count": 1000000, "like_count": 50000, "share_count": 10000, "comment_count": 5000},
        {"play_count": 100, "like_count": 5, "share_count": 2, "comment_count": 1},
    ]
    ranked = scorer.rank_videos(videos, limit=3)
    assert len(ranked) == 3, f"Should return 3 videos, got {len(ranked)}"
    assert ranked[0]["play_count"] == 1000000, "Highest play count should be first"
    print("PASS: Videos ranked correctly")

def test_diversity_mixer():
    from app.engine.diversity_mixer import DiversityMixer
    mixer = DiversityMixer()
    videos = [
        {"id": "1", "hashtags": ["cooking"], "author": {"username": "chef1"}},
        {"id": "2", "hashtags": ["cooking"], "author": {"username": "chef1"}},
        {"id": "3", "hashtags": ["comedy"], "author": {"username": "funny1"}},
        {"id": "4", "hashtags": ["tech"], "author": {"username": "tech1"}},
        {"id": "5", "hashtags": ["cooking"], "author": {"username": "chef2"}},
    ]
    mixed = mixer.mix(videos)
    assert len(mixed) == 5, f"Should return 5 videos, got {len(mixed)}"
    print("PASS: Videos mixed with diversity")

if __name__ == "__main__":
    test_score_video()
    test_rank_videos()
    test_diversity_mixer()
    print("\n=== All trending tests passed ===")
