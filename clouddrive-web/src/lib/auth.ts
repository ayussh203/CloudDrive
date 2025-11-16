import { useAuth } from '../store/auth';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export function useAuthGuard() {
  const { token, user, fetchMe } = useAuth();
  const nav = useNavigate();

  useEffect(() => {
    (async () => {
      if (!token) { nav('/login'); return; }
      try { if (!user) await fetchMe(); }
      catch { nav('/login'); }
    })();
  }, [token, user, fetchMe, nav]);
}
