"""Aggregate API router for versioned endpoints."""

from __future__ import annotations

from fastapi import APIRouter

from app.api.routes import user_routes

api_router = APIRouter()
api_router.include_router(user_routes.router)

__all__ = ["api_router"]
