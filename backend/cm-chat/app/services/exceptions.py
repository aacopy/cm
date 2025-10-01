"""Domain-specific service layer exceptions."""

from __future__ import annotations


class ServiceError(Exception):
    """Base service layer exception."""

    pass


class UserNotFoundError(ServiceError):
    """Raised when a user cannot be located."""

    pass


