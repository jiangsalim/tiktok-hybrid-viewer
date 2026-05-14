from fastapi import APIRouter, Header
from typing import Optional
from app.repository.supabase_repo import SupabaseRepo

router = APIRouter()
repo = SupabaseRepo()

@router.get("/api/v1/trending/country/{code}")
async def get_country_trending(code: str, x_device_id: Optional[str] = Header(None)):
    cached = await repo.get_trending(f"country:{code}")
    if cached and cached.get("videos"):
        return {"status": "success", "country": code, "videos": cached["videos"]}
    return {"status": "success", "country": code, "videos": []}
