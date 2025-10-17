from pydantic import BaseModel


class StreamChatVO(BaseModel):
    """流式聊天响应参数"""
    id: str
    content: str