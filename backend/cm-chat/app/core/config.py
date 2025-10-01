"""Application settings management using Pydantic."""

from __future__ import annotations

from functools import lru_cache
from typing import Literal

from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    """Strongly-typed application configuration."""

    model_config = SettingsConfigDict(
        env_file=".env",
        env_file_encoding="utf-8",
        extra="allow",
        case_sensitive=False,
    )

    app_name: str = "CM Chat API"
    version: str = "0.1.0"
    environment: Literal["development", "staging", "production", "test"] = "development"
    api_v1_prefix: str = "/api/v1"
    database_url: str = "sqlite+aiosqlite:///./cm_chat.db"
    echo_sql: bool = False
    log_level: str = "INFO"
    dev_host: str = "127.0.0.1"
    dev_port: int = 8002


@lru_cache(maxsize=1)
def get_settings() -> Settings:
    """Return a cached settings instance."""

    return Settings()


def reset_settings_cache() -> None:
    """Clear the cached settings; primarily useful for testing."""

    get_settings.cache_clear()
