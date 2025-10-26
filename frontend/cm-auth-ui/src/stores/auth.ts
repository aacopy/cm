import { computed, reactive, toRefs } from 'vue'

export type UserProfile = {
  account: string
  displayName: string
  email?: string
  mobile?: string
  lastLoginAt?: string
  token?: string
}

type AuthState = {
  user: UserProfile | null
}

const state = reactive<AuthState>({
  user: null
})

export function useAuthStore() {
  const setUser = (profile: UserProfile) => {
    state.user = profile
  }

  const clearUser = () => {
    state.user = null
  }

  return {
    ...toRefs(state),
    setUser,
    clearUser,
    isAuthenticated: computed(() => Boolean(state.user))
  }
}
