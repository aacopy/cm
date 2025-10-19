from fastapi import APIRouter

from app.model.dto.chat_input_dto import ChatInputDTO
from app.service.chat_service import ChatService

router = APIRouter(prefix="/chat", tags=["Chat"])

@router.post("/", summary="大模型对话交互接口")
async def chat(chat_input_dto: ChatInputDTO):
    """聊天接口：返回 Server-Sent Events 流。"""
    return await ChatService.chat(chat_input_dto=chat_input_dto)
