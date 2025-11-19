// src/pages/LoginPage.tsx
import type { FormEvent } from "react";
import { useState } from 'react';
import { useAuth } from '../store/auth';
import { useNavigate, Link } from 'react-router-dom';

export default function LoginPage() {
  const { login, loading } = useAuth();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const nav = useNavigate();

  async function onSubmit(e: FormEvent) {
    e.preventDefault();
    await login(email, password);
    nav('/drive');
  }

  return (
    <div className="min-h-screen grid place-items-center p-4">
      <form onSubmit={onSubmit} className="w-full max-w-md bg-white rounded-2xl p-6 shadow-soft">
        <h1 className="text-xl font-semibold mb-4">Welcome back</h1>
        <div className="space-y-3">
          <input
            className="w-full border rounded-lg px-3 py-2"
            placeholder="Email"
            type="email"
            value={email}
            onChange={e=>setEmail(e.target.value)}
            required
          />
          <input
            className="w-full border rounded-lg px-3 py-2"
            placeholder="Password"
            type="password"
            value={password}
            onChange={e=>setPassword(e.target.value)}
            required
          />
          <button
            disabled={loading}
            className="w-full rounded-lg bg-slate-900 text-white py-2 hover:opacity-90 disabled:opacity-50"
          >
            {loading ? 'Signing in…' : 'Sign in'}
          </button>
          <p className="text-sm text-slate-500">
            New here? <Link className="text-brand-700 hover:underline" to="/register">Create account</Link>
          </p>
        </div>
      </form>
    </div>
  );
}
