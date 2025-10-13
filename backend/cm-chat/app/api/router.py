from fastapi import APIRouter

from app.api import chat_router

api_router = APIRouter()
api_router.include_router(chat_router.router)
