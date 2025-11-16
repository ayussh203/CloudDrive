// src/lib/api.ts
import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:5000',
});

api.interceptors.request.use((config) => {
  const t = localStorage.getItem('jwt');
  console.log('Attaching token to request:', t);
  if (t) config.headers.Authorization = `Bearer ${t}`;
  return config;
});

export default api;
