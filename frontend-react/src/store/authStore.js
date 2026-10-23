import { create } from 'zustand';
import { authService } from '../services/authService';

const useAuthStore = create((set, get) => ({
  user: null,
  token: localStorage.getItem('token'),
  isAuthenticated: !!localStorage.getItem('token'),
  isLoading: false,
  error: null,

  signup: async (data) => {
    set({ isLoading: true, error: null });
    try {
      const response = await authService.signup(data);
      set({ error: null });
      return { success: true, data: response };
    } catch (error) {
      set({ error: error.response?.data?.detail || 'Registration failed' });
      return { success: false, error: error.response?.data?.detail || 'Registration failed' };
    } finally {
      set({ isLoading: false });
    }
  },

  signin: async (data) => {
    set({ isLoading: true, error: null });
    try {
      const response = await authService.signin(data);
      const token = response.token;
      localStorage.setItem('token', token);
      set({
        token,
        isAuthenticated: true,
        isLoading: false,
        error: null,
      });
      return { success: true, data: response };
    } catch (error) {
      set({
        error: error.response?.data?.message || 'Login failed',
        isLoading: false,
      });
      return { success: false, error: error.response?.data?.message || 'Login failed' };
    }
  },

  logout: () => {
    localStorage.removeItem('token');
    set({ user: null, token: null, isAuthenticated: false });
  },

  fetchUser: async () => {
    const token = get().token;
    if (!token) return;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const username = payload.sub || payload.userName;
      const user = await authService.getUserInfo(username);
      set({ user, isLoading: false });
    } catch (error) {
      set({ error: error.message, isLoading: false });
    }
  },

  clearError: () => set({ error: null }),
}));

export default useAuthStore;
