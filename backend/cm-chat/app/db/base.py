"""Base metadata import for Alembic and SQLAlchemy."""

from app.db.base_class import Base
from app.models.user import User

__all__ = ["Base", "User"]
