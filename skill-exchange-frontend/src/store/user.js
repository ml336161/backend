import { defineStore } from 'pinia'
import { getToken, setToken, removeToken, getUser, setUser } from '../utils/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: getToken(),
    user: getUser(),
    unreadCount: 0
  }),
  
  getters: {
    isLoggedIn: (state) => !!state.token,
    isAdmin: (state) => state.user && state.user.role === 'admin',
    userId: (state) => state.user ? state.user.id : null
  },
  
  actions: {
    setToken(token) {
      this.token = token
      setToken(token)
    },
    
    setUser(user) {
      this.user = user
      setUser(user)
    },
    
    setUnreadCount(count) {
      this.unreadCount = count
    },
    
    logout() {
      this.token = null
      this.user = null
      this.unreadCount = 0
      removeToken()
    }
  }
})