from fastapi import APIRouter, Header
from typing import Optional
from app.repository.supabase_repo import SupabaseRepo
from app.engine.trending_scorer import TrendingScorer
from app.engine.diversity_mixer import DiversityMixer

router = APIRouter()
repo = SupabaseRepo()
scorer = TrendingScorer()
mixer = DiversityMixer()

@router.get("/api/v1/trending/global")
async def get_global_trending(x_device_id: Optional[str] = Header(None)):
    cached = await repo.get_trending("global")
    if cached and cached.get("videos"):
        return {"status": "success", "videos": cached["videos"]}
    return {"status": "success", "videos": []}
