// src/pages/RegisterPage.tsx
import type { FormEvent } from 'react';
import { useState } from 'react';
import { useAuth } from '../store/auth';
import { motion } from 'framer-motion';
import { useNavigate, Link } from 'react-router-dom';

const fadeUp = {
  hidden: { opacity: 0, y: 20 },
  visible: { opacity: 1, y: 0 },
};

const RegisterPage: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [uiError, setUiError] = useState<string | null>(null);

  // from zustand store
  const { register, loading } = useAuth();
  const nav = useNavigate();

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setUiError(null);

    try {
      // backend /api/auth/register DOES NOT need a token
      await register(email, password);
      // register() already logs in and stores token, then fetchMe()
      nav('/app/drive');
    } catch (err: any) {
      // surface a friendly error
      const msg =
        err?.response?.data?.message ||
        err?.message ||
        'Something went wrong. Please try again.';
      setUiError(msg);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-[#f5f3ff] via-[#faf5ff] to-[#fdf2ff] flex flex-col">
      {/* Top bar with logo + link back to home */}
      <header className="h-16 flex items-center justify-between px-6 md:px-12">
        <Link to="/" className="flex items-center gap-2">
          <div className="h-9 w-9 rounded-2xl bg-gradient-to-tr from-[#4f46e5] via-[#8b5cf6] to-[#ec4899] flex items-center justify-center text-white font-semibold text-sm shadow-md">
            CD
          </div>
          <span className="font-semibold text-slate-900 text-lg tracking-tight">
            CloudDrive
          </span>
        </Link>

        <Link
          to="/login"
          className="text-sm text-slate-600 hover:text-slate-900 transition-colors"
        >
          Already have an account?{' '}
          <span className="font-semibold">Sign in</span>
        </Link>
      </header>

      {/* Main content */}
      <main className="flex-1 flex items-center justify-center px-4 pb-10">
        <motion.div
          variants={fadeUp}
          initial="hidden"
          animate="visible"
          transition={{ duration: 0.45, ease: 'easeOut' }}
          className="w-full max-w-md"
        >
          <div className="relative overflow-hidden rounded-3xl bg-white/90 backdrop-blur-xl shadow-[0_20px_60px_rgba(15,23,42,0.18)] border border-white/80 px-8 py-8 md:px-10 md:py-9">
            {/* subtle gradient blob */}
            <div className="pointer-events-none absolute -top-24 -right-10 h-40 w-40 rounded-full bg-gradient-to-br from-[#8b5cf6]/30 via-[#ec4899]/25 to-[#f97316]/20 blur-2xl opacity-80" />

            <div className="relative space-y-6">
              <div className="space-y-2">
                <h1 className="text-2xl md:text-3xl font-semibold text-slate-900">
                  Create your account
                </h1>
                <p className="text-sm text-slate-500 leading-relaxed">
                  A single workspace to upload files, share secure links, and
                  track how people interact with them.
                </p>
              </div>

              <form className="space-y-4" onSubmit={handleSubmit}>
                <div className="space-y-1.5">
                  <label
                    htmlFor="email"
                    className="block text-xs font-medium uppercase tracking-[0.18em] text-slate-500"
                  >
                    Email
                  </label>
                  <input
                    id="email"
                    type="email"
                    required
                    value={email}
                    onChange={e => setEmail(e.target.value)}
                    className="w-full rounded-xl border border-slate-200 bg-slate-50/60 focus:bg-white px-3.5 py-2.5 text-sm md:text-base text-slate-900 outline-none shadow-inner focus:ring-2 focus:ring-violet-500 focus:border-violet-500 transition"
                    placeholder="you@example.com"
                  />
                </div>

                <div className="space-y-1.5">
                  <label
                    htmlFor="password"
                    className="block text-xs font-medium uppercase tracking-[0.18em] text-slate-500"
                  >
                    Password
                  </label>
                  <input
                    id="password"
                    type="password"
                    required
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                    className="w-full rounded-xl border border-slate-200 bg-slate-50/60 focus:bg-white px-3.5 py-2.5 text-sm md:text-base text-slate-900 outline-none shadow-inner focus:ring-2 focus:ring-violet-500 focus:border-violet-500 transition"
                    placeholder="At least 8 characters"
                  />
                  <p className="text-[11px] text-slate-400">
                    Use at least 8 characters with a mix of letters and
                    numbers.
                  </p>
                </div>

                {uiError && (
                  <div className="rounded-xl bg-rose-50 border border-rose-100 px-3 py-2 text-xs text-rose-700">
                    {uiError}
                  </div>
                )}

                <button
                  type="submit"
                  disabled={loading}
                  className="w-full mt-2 inline-flex items-center justify-center rounded-xl bg-slate-900 text-white text-sm md:text-base font-semibold py-3 px-4 shadow-[0_16px_30px_rgba(15,23,42,0.55)] hover:bg-slate-800 disabled:opacity-70 disabled:cursor-not-allowed transition-colors"
                >
                  {loading ? 'Creating account…' : 'Create account'}
                </button>
              </form>

              <p className="text-xs text-slate-400 pt-1">
                By creating an account, you agree to our{' '}
                <span className="underline underline-offset-2">Terms</span> and{' '}
                <span className="underline underline-offset-2">
                  Privacy Policy
                </span>
                .
              </p>
            </div>
          </div>
        </motion.div>
      </main>
    </div>
  );
};

export default RegisterPage;
