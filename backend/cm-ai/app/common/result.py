from typing import Optional, TypeVar, Generic

from pydantic import Field
from pydantic.v1.generics import GenericModel

T = TypeVar("T")


class Result(GenericModel, Generic[T]):
    """统一返回结果模型"""
    code: int = Field(description="返回码，0表示成功，非0表示失败")
    msg: Optional[str] = Field(description="返回信息")
    data: Optional[T] = Field(description="业务数据")

    @staticmethod
    def success(data: Optional[T] = None, msg: str = "success"):
        """成功返回结果"""
        return Result(code=0, msg=msg, data=data)

    @staticmethod
    def error(code: int = -1, msg: str = "error"):
        """失败返回结果"""
        return Result(code=code, msg=msg)
