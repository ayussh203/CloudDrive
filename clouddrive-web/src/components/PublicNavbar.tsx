// src/components/PublicNavbar.tsx
import { Link } from 'react-router-dom';

export default function PublicNavbar() {
  return (
    <nav className="sticky top-0 z-50 border-b bg-white/80 backdrop-blur">
      <div className="max-w-6xl mx-auto px-4 h-16 flex items-center justify-between">
        <Link to="/" className="flex items-center gap-3">
          <div className="h-9 w-9 rounded-2xl bg-gradient-to-br from-brand-500 to-brand-700" />
          <span className="text-lg font-semibold">CloudDrive</span>
        </Link>
        <div className="flex items-center gap-3">
          <Link to="/login" className="px-4 py-2 rounded-lg hover:bg-slate-100">Log in</Link>
          <Link to="/register" className="px-4 py-2 rounded-lg bg-slate-900 text-white">Get started</Link>
        </div>
      </div>
    </nav>
  );
}
