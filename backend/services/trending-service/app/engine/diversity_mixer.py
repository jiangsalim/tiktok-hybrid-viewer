from typing import List, Dict, Any
import random

class DiversityMixer:
    def __init__(self):
        self.max_same_category = 3
        self.max_same_creator = 2

    def mix(self, videos: List[Dict[str, Any]]) -> List[Dict[str, Any]]:
        if not videos:
            return videos

        categories: Dict[str, int] = {}
        creators: Dict[str, int] = {}
        result = []
        remaining = list(videos)
        random.shuffle(remaining)

        for _ in range(len(videos)):
            best = None
            for video in remaining:
                cat = self._get_category(video)
                creator = video.get("author", {}).get("username", "")

                cat_count = categories.get(cat, 0)
                creator_count = creators.get(creator, 0)

                if cat_count < self.max_same_category and creator_count < self.max_same_creator:
                    best = video
                    break

            if best is None and remaining:
                best = remaining[0]

            if best:
                result.append(best)
                remaining.remove(best)
                cat = self._get_category(best)
                creator = best.get("author", {}).get("username", "")
                categories[cat] = categories.get(cat, 0) + 1
                creators[creator] = creators.get(creator, 0) + 1

        return result

    def _get_category(self, video: Dict[str, Any]) -> str:
        hashtags = video.get("hashtags", [])
        if not hashtags:
            return "general"
        return hashtags[0] if isinstance(hashtags[0], str) else str(hashtags[0])
