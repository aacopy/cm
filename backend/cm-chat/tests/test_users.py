"""Integration tests for user CRUD endpoints."""

from __future__ import annotations

import os
from pathlib import Path

import pytest
from httpx import ASGITransport, AsyncClient

os.environ.setdefault("ENVIRONMENT", "test")
os.environ["DATABASE_URL"] = "sqlite+aiosqlite:///./test_cm_chat.db"

from app.core.config import reset_settings_cache  # noqa: E402

reset_settings_cache()

from app.db.session import init_db, shutdown  # noqa: E402
from app.main import create_app  # noqa: E402


@pytest.fixture(scope="session")
def anyio_backend() -> str:
    return "asyncio"


@pytest.fixture(scope="session")
async def app_instance():
    application = create_app()
    await init_db()
    yield application
    await shutdown()
    db_file = Path("test_cm_chat.db")
    if db_file.exists():
        db_file.unlink()


@pytest.fixture
async def client(app_instance):
    transport = ASGITransport(app=app_instance)
    async with AsyncClient(transport=transport, base_url="http://testserver") as async_client:
        yield async_client


@pytest.mark.anyio
async def test_user_crud_flow(client: AsyncClient):
    create_payload = {
        "name": "Jane Doe",
        "age": 30,
    }

    create_resp = await client.post("/api/v1/users/", json=create_payload)
    assert create_resp.status_code == 201
    created_user = create_resp.json()
    assert created_user["name"] == create_payload["name"]
    assert created_user["deleted"] is False

    user_id = created_user["id"]

    list_resp = await client.get("/api/v1/users/")
    assert list_resp.status_code == 200
    users = list_resp.json()
    assert any(user["id"] == user_id for user in users)

    get_resp = await client.get(f"/api/v1/users/{user_id}")
    assert get_resp.status_code == 200
    assert get_resp.json()["name"] == "Jane Doe"

    update_payload = {"name": "Jane A. Doe", "age": 31}
    update_resp = await client.patch(f"/api/v1/users/{user_id}", json=update_payload)
    assert update_resp.status_code == 200
    updated = update_resp.json()
    assert updated["name"] == update_payload["name"]
    assert updated["age"] == update_payload["age"]

    delete_resp = await client.delete(f"/api/v1/users/{user_id}")
    assert delete_resp.status_code == 204

    missing_resp = await client.get(f"/api/v1/users/{user_id}")
    assert missing_resp.status_code == 404

    list_after_delete = await client.get("/api/v1/users/")
    assert all(user["id"] != user_id for user in list_after_delete.json())
