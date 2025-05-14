import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    isAuthenticated: false,
    currentUser: ''
  }),
  actions: {
    login(user) {
      this.isAuthenticated = true
      this.currentUser = user
    },
    logout() {
      this.isAuthenticated = false
      this.currentUser = ''
    }
  }
})
