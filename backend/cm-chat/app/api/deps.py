"""Shared dependency overrides for API routes."""

from __future__ import annotations

from collections.abc import AsyncIterator

from fastapi import Depends
from sqlalchemy.ext.asyncio import AsyncSession

from app.db.session import SessionLocal, get_session
from app.repositories.user_repository import UserRepository
from app.services.user_service import UserService


async def get_db() -> AsyncIterator[AsyncSession]:
    """Provide a request-scoped database session."""

    async for session in get_session():
        yield session


def get_user_service(session: AsyncSession = Depends(get_db)) -> UserService:
    """Build a user service with injected repository."""

    repository = UserRepository(session)
    return UserService(repository)
