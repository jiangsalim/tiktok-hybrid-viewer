from fastapi import FastAPI, Header, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from app.api import global_trending, country_trending, category_trending
from app.scheduler.refresh_job import start_scheduler
import uvicorn
import os

app = FastAPI(title="TikTok Viewer - Trending Service")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["GET"],
    allow_headers=["*"],
)

app.include_router(global_trending.router)
app.include_router(country_trending.router)
app.include_router(category_trending.router)

@app.on_event("startup")
async def startup():
    start_scheduler()

@app.get("/health")
async def health():
    return {"status": "healthy", "service": "trending"}

if __name__ == "__main__":
    port = int(os.getenv("PORT", 8082))
    uvicorn.run(app, host="0.0.0.0", port=port)
