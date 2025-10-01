"""Centralized logging configuration for the application."""

from __future__ import annotations

import logging
from logging.config import dictConfig

from app.core.config import get_settings


def setup_logging() -> None:
    """Configure logging for the application."""

    settings = get_settings()

    dictConfig(
        {
            "version": 1,
            "disable_existing_loggers": False,
            "formatters": {
                "standard": {
                    "format": "%(asctime)s | %(levelname)s | %(name)s | %(message)s",
                }
            },
            "handlers": {
                "default": {
                    "level": settings.log_level,
                    "class": "logging.StreamHandler",
                    "formatter": "standard",
                }
            },
            "loggers": {
                "": {
                    "handlers": ["default"],
                    "level": settings.log_level,
                },
                "uvicorn": {
                    "handlers": ["default"],
                    "level": "INFO",
                    "propagate": False,
                },
                "uvicorn.error": {
                    "handlers": ["default"],
                    "level": "INFO",
                    "propagate": False,
                },
                "uvicorn.access": {
                    "handlers": ["default"],
                    "level": "INFO",
                    "propagate": False,
                },
            },
        }
    )

    engine_logger_level = logging.INFO if settings.echo_sql else logging.WARNING
    logging.getLogger("sqlalchemy.engine").setLevel(engine_logger_level)


__all__ = ["setup_logging"]
