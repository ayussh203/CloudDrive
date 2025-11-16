import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../store/auth';
import Button from './Button';

export default function Navbar() {
  const { user, logout, token } = useAuth();
  const nav = useNavigate();

  return (
    <header className="sticky top-0 z-10 border-b border-black/10 dark:border-white/10 bg-white/70 dark:bg-black/30 backdrop-blur-md">
      <div className="mx-auto max-w-6xl flex h-14 items-center justify-between px-4">
        <Link to="/" className="font-semibold text-brand-800 dark:text-brand-200">
          CloudDrive
        </Link>
        <nav className="flex items-center gap-3">
          {token ? (
            <>
              <span className="text-sm opacity-80">{user?.email}</span>
              <Button variant="ghost" onClick={() => { logout(); nav('/login'); }}>
                Logout
              </Button>
            </>
          ) : (
            <>
              <Link to="/login" className="opacity-80 hover:opacity-100">Login</Link>
              <Link to="/register" className="opacity-80 hover:opacity-100">Register</Link>
            </>
          )}
        </nav>
      </div>
    </header>
  );
}
