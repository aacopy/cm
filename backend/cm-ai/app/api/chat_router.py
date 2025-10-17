from fastapi import APIRouter

from app.model.dto.chat_input_dto import ChatInputDTO
from app.service.chat_service import ChatService

router = APIRouter(prefix="/chat", tags=["Chat"])

@router.post("/")
def chat(chat_input_dto: ChatInputDTO):
    """聊天接口"""
    return ChatService.chat(chat_input_dto = chat_input_dto)
