import { useState } from "react";
import type { FormEvent } from "react";
import { useAuth } from '../store/auth';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import { useNavigate, Link } from 'react-router-dom';

export default function Register() {
  const [name,setName]=useState('Demo');
  const [email,setEmail]=useState('demo@acme.io');
  const [password,setPassword]=useState('Secret@123');
  const [loading,setLoading]=useState(false);
  const { register } = useAuth();
  const nav = useNavigate();

  const onSubmit = async (e:FormEvent) => {
    e.preventDefault(); 
    setLoading(true);

    try {
       await register(email, password, name); nav('/'); }
    catch (e:any) { alert(e?.response?.data?.message || 'Register failed'); }
    finally { setLoading(false); }
  };

  return (
    <div className="mx-auto max-w-md mt-16 glass p-6 rounded-2xl bg-red-600">
      <h1 className="text-2xl font-semibold mb-4">Create your account</h1>
      <form onSubmit={onSubmit} className="space-y-4">
        {/* <div><label>Name</label><Input value={name} onChange={e=>setName(e.target.value)} /></div> */}
        <div><label>Email</label><Input value={email} onChange={e=>setEmail(e.target.value)} /></div>
        <div><label>Password</label><Input type="password" value={password} onChange={e=>setPassword(e.target.value)} /></div>
        <Button disabled={loading} type="submit">{loading?'Creating…':'Create account'}</Button>
      </form>
      <p className="mt-4 text-sm opacity-70">
        Already have an account? <Link to="/login" className="text-brand-700">Login</Link>
      </p>
    </div>
  );
}
