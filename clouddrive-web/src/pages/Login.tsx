import { useState } from "react";
import type { FormEvent } from "react";
import { useAuth } from '../store/auth';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import { useNavigate, Link } from 'react-router-dom';

export default function Login() {
  const [email,setEmail]=useState('user1@example.com');
  const [password,setPassword]=useState('Secret@123');
  const [loading,setLoading]=useState(false);
  const { login } = useAuth();
  const nav = useNavigate();

  const onSubmit = async (e:FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try { await login(email, password); nav('/'); }
    catch (e:any) { alert(e?.response?.data?.message || 'Login failed'); }
    finally { setLoading(false); }
  };

  return (
    <div className="mx-auto max-w-md mt-16 glass p-6 rounded-2xl">
      <h1 className="text-2xl font-semibold mb-4">Welcome back</h1>
      <form onSubmit={onSubmit} className="space-y-4">
        <div><label>Email</label><Input value={email} onChange={e=>setEmail(e.target.value)} /></div>
        <div><label>Password</label><Input type="password" value={password} onChange={e=>setPassword(e.target.value)} /></div>
        <Button disabled={loading} type="submit">{loading?'Signing in…':'Sign in'}</Button>
      </form>
      <p className="mt-4 text-sm opacity-70">
        No account? <Link to="/register" className="text-brand-700">Register</Link>
      </p>
    </div>
  );
}
