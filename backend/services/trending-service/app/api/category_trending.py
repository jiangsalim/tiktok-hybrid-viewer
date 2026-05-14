from fastapi import APIRouter, Header
from typing import Optional
from app.repository.supabase_repo import SupabaseRepo

router = APIRouter()
repo = SupabaseRepo()

@router.get("/api/v1/trending/category/{name}")
async def get_category_trending(name: str, x_device_id: Optional[str] = Header(None)):
    cached = await repo.get_trending(f"category:{name}")
    if cached and cached.get("videos"):
        return {"status": "success", "category": name, "videos": cached["videos"]}
    return {"status": "success", "category": name, "videos": []}
