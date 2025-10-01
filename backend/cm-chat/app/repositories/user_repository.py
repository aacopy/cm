"""Data access layer for user entities."""

from __future__ import annotations

from typing import Sequence

from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from app.models.user import User


class UserRepository:
    """Encapsulates persistence operations for users."""

    def __init__(self, session: AsyncSession) -> None:
        self._session = session

    @property
    def session(self) -> AsyncSession:
        """Expose the underlying session for transactional handling."""

        return self._session

    @property
    def session(self) -> AsyncSession:
        return self._session

    async def list(self) -> Sequence[User]:
        stmt = select(User).where(User.deleted.is_(False)).order_by(User.id.asc())
        result = await self._session.execute(stmt)
        return result.scalars().all()

    async def get(self, user_id: int) -> User | None:
        return await self._session.get(User, user_id)

    async def create(self, *, name: str, age: int | None = None, deleted: bool = False) -> User:
        user = User(name=name, age=age, deleted=deleted)
        self._session.add(user)
        await self._session.flush()
        await self._session.refresh(user)
        return user

    async def update(
        self,
        user: User,
        *,
        name: str | None = None,
        age: int | None = None,
        deleted: bool | None = None,
    ) -> User:
        if name is not None:
            user.name = name
        if age is not None:
            user.age = age
        if deleted is not None:
            user.deleted = deleted
        await self._session.flush()
        await self._session.refresh(user)
        return user

    async def delete(self, user: User) -> User:
        user.deleted = True
        await self._session.flush()
        await self._session.refresh(user)
        return user
