// src/components/Topbar.tsx
import { useAuth } from '../store/auth';

export default function Topbar() {
  const { user, logout } = useAuth();
  return (
    <header className="h-14 border-b bg-white/60 backdrop-blur sticky top-0 z-40">
      <div className="h-full max-w-6xl mx-auto px-4 flex items-center justify-between">
        <div className="flex items-center gap-2">
          <div className="h-8 w-8 rounded-xl bg-gradient-to-br from-brand-500 to-brand-700 shadow-soft" />
          <span className="font-semibold">CloudDrive</span>
        </div>
        <div className="flex items-center gap-4">
          {user && <span className="text-sm text-slate-500">Signed in as <b>{user.email}</b></span>}
          {user && (
            <button
              onClick={logout}
              className="px-3 py-1.5 text-sm rounded-lg bg-slate-900 text-white hover:opacity-90"
            >
              Logout
            </button>
          )}
        </div>
      </div>
    </header>
  );
}
