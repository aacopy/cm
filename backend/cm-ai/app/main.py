import dotenv
import uvicorn
from fastapi import FastAPI

from app.api.router import api_router


def create_app() -> FastAPI:
    app = FastAPI()
    app.include_router(router=api_router)
    return app

app = create_app()

if __name__ == "__main__":
    dotenv.load_dotenv()
    uvicorn.run(app, host="0.0.0.0", port=8002)
