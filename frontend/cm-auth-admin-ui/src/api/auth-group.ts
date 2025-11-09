import request from '@/utils/http'

// 获取认证组列表
export function fetchGetAuthGroupList(params: Api.Common.CommonSearchParams) {
  return request.get<Api.SystemManage.AuthGroupPage>({
    url: '/auth/v1/authGroup/page',
    params
  })
}

// 保存认证组
export function saveAuthGroup(data: Api.SystemManage.AuthGroupSaveParams) {
  return request.post<Api.SystemManage.AuthGroupListItem>({
    url: '/auth/v1/authGroup/save',
    data
  })
}
