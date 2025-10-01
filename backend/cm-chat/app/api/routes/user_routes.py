"""User API routes."""

from __future__ import annotations

from typing import Sequence

from fastapi import APIRouter, Depends, HTTPException, status

from app.api.deps import get_user_service
from app.schemas.user import UserCreate, UserRead, UserUpdate
from app.services.exceptions import UserNotFoundError
from app.services.user_service import UserService

router = APIRouter(prefix="/users", tags=["users"])


@router.get("/", response_model=list[UserRead])
async def list_users(service: UserService = Depends(get_user_service)) -> Sequence[UserRead]:
    users = await service.list_users()
    return [UserRead.model_validate(user) for user in users]


@router.get("/{user_id}", response_model=UserRead)
async def get_user(user_id: int, service: UserService = Depends(get_user_service)) -> UserRead:
    try:
        user = await service.get_user(user_id)
        return UserRead.model_validate(user)
    except UserNotFoundError as exc:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail=str(exc)) from exc


@router.post("/", response_model=UserRead, status_code=status.HTTP_201_CREATED)
async def create_user(payload: UserCreate, service: UserService = Depends(get_user_service)) -> UserRead:
    user = await service.create_user(payload)
    return UserRead.model_validate(user)


@router.patch("/{user_id}", response_model=UserRead)
async def update_user(
    user_id: int,
    payload: UserUpdate,
    service: UserService = Depends(get_user_service),
) -> UserRead:
    try:
        user = await service.update_user(user_id, payload)
        return UserRead.model_validate(user)
    except UserNotFoundError as exc:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail=str(exc)) from exc


@router.delete("/{user_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_user(user_id: int, service: UserService = Depends(get_user_service)) -> None:
    try:
        await service.delete_user(user_id)
    except UserNotFoundError as exc:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail=str(exc)) from exc
