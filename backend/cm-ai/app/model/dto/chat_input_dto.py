from typing import Optional

from pydantic import BaseModel, Field


class ChatInputDTO(BaseModel):
    """聊天请求参数"""
    value: Optional[str] = Field(description="用户输入值")