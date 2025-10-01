"""Application entrypoint for the FastAPI service."""

from __future__ import annotations

from contextlib import asynccontextmanager

from fastapi import FastAPI

from app.api.router import api_router
from app.core.config import get_settings
from app.core.logging_config import setup_logging
from app.db.session import init_db, shutdown


def create_app() -> FastAPI:
	"""Construct and configure the FastAPI application."""

	settings = get_settings()
	setup_logging()

	@asynccontextmanager
	async def lifespan(app: FastAPI):  # pragma: no cover - exercised via integration tests
		await init_db()
		yield
		await shutdown()

	application = FastAPI(
		title=settings.app_name,
		version=settings.version,
		lifespan=lifespan,
	)
	application.include_router(api_router, prefix=settings.api_v1_prefix)

	@application.get("/healthz", tags=["health"])
	async def health_check() -> dict[str, str]:
		return {"status": "ok"}

	return application


app = create_app()


@app.get("/", include_in_schema=False)
async def root() -> dict[str, str]:
	"""Simple landing endpoint."""

	settings = get_settings()
	return {"message": f"Welcome to {settings.app_name}"}


if __name__ == "__main__":
	settings = get_settings()
	setup_logging()
	from uvicorn import run

	run(
		"app.main:app",
		host=settings.model_dump().get("dev_host", "127.0.0.1"),
		port=int(settings.model_dump().get("dev_port", 8002)),
		reload=True,
	)
