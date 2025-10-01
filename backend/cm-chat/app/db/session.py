"""Database session and engine management utilities."""

from __future__ import annotations

from collections.abc import AsyncIterator

from sqlalchemy.ext.asyncio import AsyncEngine, AsyncSession, async_sessionmaker, create_async_engine

from app.core.config import get_settings

settings = get_settings()

_engine: AsyncEngine = create_async_engine(
    settings.database_url,
    echo=settings.echo_sql,
    future=True,
)

SessionLocal = async_sessionmaker(
    bind=_engine,
    expire_on_commit=False,
    autoflush=False,
    autocommit=False,
)


async def get_session() -> AsyncIterator[AsyncSession]:
    """Yield a scoped asynchronous database session."""

    async with SessionLocal() as session:
        try:
            yield session
        finally:
            await session.close()


async def init_db() -> None:
    """Initialize database schema."""

    import app.db.base  # noqa: F401

    from app.db.base import Base

    async with _engine.begin() as conn:
        await conn.run_sync(Base.metadata.create_all)


async def shutdown() -> None:
    """Dispose database engine resources."""

    await _engine.dispose()


__all__ = ["SessionLocal", "get_session", "init_db", "shutdown"]
