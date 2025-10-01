"""Business logic for user resources."""

from __future__ import annotations

from sqlalchemy.exc import SQLAlchemyError

from app.repositories.user_repository import UserRepository
from app.schemas.user import UserCreate, UserUpdate
from app.services.exceptions import UserNotFoundError


class UserService:
    """Encapsulates use-cases for user management."""

    def __init__(self, repository: UserRepository) -> None:
        self._repository = repository

    async def list_users(self):
        return await self._repository.list()

    async def get_user(self, user_id: int):
        user = await self._repository.get(user_id)
        if not user or user.deleted:
            raise UserNotFoundError(f"User {user_id} not found")
        return user

    async def create_user(self, payload: UserCreate):
        user = await self._repository.create(
            name=payload.name,
            age=payload.age,
            deleted=payload.deleted,
        )
        await self._commit()
        return user

    async def update_user(self, user_id: int, payload: UserUpdate):
        user = await self.get_user(user_id)
        user = await self._repository.update(
            user,
            name=payload.name,
            age=payload.age,
            deleted=payload.deleted,
        )
        await self._commit()
        return user

    async def delete_user(self, user_id: int) -> None:
        user = await self.get_user(user_id)
        await self._repository.delete(user)
        await self._commit()

    async def _commit(self) -> None:
        try:
            await self._repository.session.commit()
        except SQLAlchemyError as exc:  # pragma: no cover - defensive
            await self._repository.session.rollback()
            raise exc
