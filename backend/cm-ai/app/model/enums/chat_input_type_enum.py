from enum import Enum


class ChatInputTypeEnum(str, Enum):
    """输入类型枚举（使用字符串值，便于前端直接传名称）"""
    TEXT = "TEXT"  # 文本输入
