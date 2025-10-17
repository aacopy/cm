from fastapi import APIRouter

from app.model.dto.chat_input_dto import ChatInputDTO
from app.model.vo.stream_chat_vo import StreamChatVO
from app.service.chat_service import ChatService

router = APIRouter(prefix="/chat", tags=["Chat"])

@router.post("/", summary="大模型对话交互接口", response_model=StreamChatVO)
def chat(chat_input_dto: ChatInputDTO):
    """聊天接口"""
    return ChatService.chat(chat_input_dto = chat_input_dto)
