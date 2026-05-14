import threading
import time
import logging

logger = logging.getLogger(__name__)

def start_scheduler():
    def run():
        while True:
            try:
                logger.info("Trending refresh tick")
            except Exception as e:
                logger.error(f"Trending refresh error: {e}")
            time.sleep(1800)

    thread = threading.Thread(target=run, daemon=True)
    thread.start()
