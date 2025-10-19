from typing import AsyncGenerator
import json

from langchain_core.messages import HumanMessage
from langchain_openai import ChatOpenAI
from sse_starlette import EventSourceResponse

from app.model.dto.chat_input_dto import ChatInputDTO

class ChatService:
    """聊天服务：使用 LangChain 调用大模型并通过 SSE 返回流式输出"""

    @staticmethod
    async def chat(chat_input_dto: ChatInputDTO):
        """发起聊天请求，返回 SSE 响应。"""
        llm = ChatOpenAI(
            base_url="https://api.deepseek.com/v1",
            model="deepseek-chat",  # 替换为你的模型名称
            temperature=0.7,
            streaming=True,  # 允许流式
        )

        messages = [HumanMessage(content=chat_input_dto.value)]

        async def event_generator() -> AsyncGenerator[dict, None]:
            # 累积内容（可选，用于最终汇总）
            full_content_parts = []
            try:
                # 原生异步流：逐 chunk 输出
                async for chunk in llm.astream(messages):
                    token = chunk.content
                    if not token:
                        continue
                    full_content_parts.append(token)
                    # 发送单个 token / chunk。前端可按需拼接。
                    yield {
                        "event": "message",
                        "data": json.dumps({
                            "id": str(chat_input_dto.conversation_id),
                            "content": token
                        }, ensure_ascii=False)
                    }

                # 结束事件，附带完整内容（可选）
                yield {
                    "event": "end",
                    "data": json.dumps({
                        "id": str(chat_input_dto.conversation_id),
                        "content": "".join(full_content_parts),
                        "status": "DONE"
                    }, ensure_ascii=False)
                }
            except Exception as e:  # 错误事件
                yield {
                    "event": "error",
                    "data": json.dumps({
                        "id": str(chat_input_dto.conversation_id),
                        "error": str(e)
                    }, ensure_ascii=False)
                }

        # 返回 SSE 响应。可设置心跳保持连接 (ping)
        return EventSourceResponse(event_generator(), ping=15)
