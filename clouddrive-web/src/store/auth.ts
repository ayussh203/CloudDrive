// src/store/auth.ts
import { create } from 'zustand';
import api from '../lib/api';

type User = { id: number; email: string; name?: string } | null;

type AuthState = {
  user: User;
  token: string | null;
  loading: boolean;
  error: string | null;
  login: (email: string, password: string) => Promise<void>;
  register: (email: string, password: string, name?: string) => Promise<void>;
  fetchMe: () => Promise<void>;
  logout: () => void;
};

export const useAuth = create<AuthState>((set, get) => ({
  user: null,
  token: localStorage.getItem('jwt'),
  loading: false,
  error: null,

  async login(email, password) {
    set({ loading: true, error: null });
    const { data } = await api.post('/api/auth/login', { email, password });
    localStorage.setItem('jwt', data.token);
    set({ token: data.token });
    await get().fetchMe();
    set({ loading: false });
  },

  async register(email, password, name) {
    set({ loading: true, error: null });
    await api.post('/api/auth/register', { email, password, name });
    await get().login(email, password);
    set({ loading: false });
  },

  async fetchMe() {
    const { data } = await api.get('/api/secure/me');
    // backend returns { id, email } — adapt if you add name later
    set({ user: data });
  },

  logout() {
    localStorage.removeItem('jwt');
    set({ user: null, token: null });
  },
}));
