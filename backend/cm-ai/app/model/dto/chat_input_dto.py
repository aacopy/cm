from pydantic import BaseModel, Field

from app.model.enums.chat_input_type_enum import ChatInputTypeEnum


class ChatInputDTO(BaseModel):
    """聊天请求参数"""
    conversation_id: int = Field(description="会话ID")
    input_type: ChatInputTypeEnum = Field(description="输入类型，文本")
    value: str = Field(description="用户输入值")
