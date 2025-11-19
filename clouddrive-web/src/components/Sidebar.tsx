// src/components/Sidebar.tsx
import { NavLink } from 'react-router-dom';
import clsx from 'classnames';

const link = 'px-3 py-2 rounded-lg text-sm hover:bg-slate-100';
const active = 'bg-slate-900 text-white hover:bg-slate-900';

export default function Sidebar() {
  return (
    <aside className="w-60 shrink-0 border-r bg-white/60 backdrop-blur min-h-[calc(100vh-3.5rem)]">
      <nav className="p-3 space-y-1">
        <NavLink to="/drive" className={({isActive}) => clsx(link, isActive && active)}>Drive</NavLink>
        <NavLink to="/shares" className={({isActive}) => clsx(link, isActive && active)}>Shares</NavLink>
        <NavLink to="/short" className={({isActive}) => clsx(link, isActive && active)}>Short URLs</NavLink>
        <NavLink to="/settings" className={({isActive}) => clsx(link, isActive && active)}>Settings</NavLink>
      </nav>
    </aside>
  );
}
