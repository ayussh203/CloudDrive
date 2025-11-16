// src/store/auth.ts
import { create } from 'zustand';
import api from '../lib/api';

type User = { id: number; email: string; name?: string };

type AuthState = {
  user: User | null;
  token: string | null;
  login: (email: string, password: string) => Promise<void>;
  register: (email: string, password: string, name?: string) => Promise<void>;
  fetchMe: () => Promise<void>;
  logout: () => void;
};

export const useAuth = create<AuthState>((set) => ({
  user: null,
  token: localStorage.getItem('jwt'),

  async login(email, password) {
    const { data } = await api.post('/api/auth/login', { email, password });
    localStorage.setItem('jwt', data.token);
    set({ token: data.token });
    await useAuth.getState().fetchMe();
  },

  async register(email, password, name) {
    await api.post('/api/auth/register', { email, password, name });
    // Auto-login after register (this is why you saw login called)
    await useAuth.getState().login(email, password);
  },

  async fetchMe() {
    const { data } = await api.get<User>('/api/secure/me'); // returns {id,email,name}
    set({ user: data });
  },

  logout() {
    localStorage.removeItem('jwt');
    set({ user: null, token: null });
  },
}));
