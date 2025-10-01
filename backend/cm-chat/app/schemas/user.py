"""Pydantic schemas for user resources."""

from __future__ import annotations

from datetime import datetime

from pydantic import BaseModel, ConfigDict


class UserBase(BaseModel):
    name: str
    age: int | None = None
    deleted: bool = False


class UserCreate(UserBase):
    """Payload for creating a user."""

    deleted: bool = False


class UserUpdate(BaseModel):
    """Payload for partially updating a user."""

    name: str | None = None
    age: int | None = None
    deleted: bool | None = None


class UserRead(UserBase):
    """Response schema for a user."""

    model_config = ConfigDict(from_attributes=True)

    id: int
    create_time: datetime
