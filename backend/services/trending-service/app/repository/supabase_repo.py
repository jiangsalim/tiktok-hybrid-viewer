import os
import json
from datetime import datetime, timezone
from typing import Optional, Dict, Any
from supabase import create_client, Client

class SupabaseRepo:
    def __init__(self):
        url = os.getenv("SUPABASE_URL", "")
        key = os.getenv("SUPABASE_ANON_KEY", "")
        self.client: Optional[Client] = None
        if url and key:
            try:
                self.client = create_client(url, key)
            except Exception:
                self.client = None

    async def get_trending(self, cache_key: str) -> Optional[Dict[str, Any]]:
        if not self.client:
            return None
        try:
            result = self.client.table("trending_cache") \
                .select("*") \
                .eq("cache_key", cache_key) \
                .execute()
            if result.data and len(result.data) > 0:
                row = result.data[0]
                videos = json.loads(row.get("video_ids_json", "[]"))
                return {"videos": videos}
        except Exception:
            pass
        return None

    async def store_trending(self, cache_key: str, videos: list, source: str = "aggregated"):
        if not self.client:
            return
        try:
            self.client.table("trending_cache").upsert({
                "cache_key": cache_key,
                "video_ids_json": json.dumps(videos),
                "source": source,
                "video_count": len(videos),
                "ttl_minutes": 60,
            }).execute()
        except Exception:
            pass
