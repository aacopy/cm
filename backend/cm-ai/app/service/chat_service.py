from app.model.dto.chat_input_dto import ChatInputDTO


class ChatService:
    """聊天服务"""
    @staticmethod
    def chat(chat_input_dto: ChatInputDTO):
        return chat_input_dto.value