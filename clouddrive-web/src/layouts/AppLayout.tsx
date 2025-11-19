// src/layouts/AppLayout.tsx
import { Outlet, NavLink } from 'react-router-dom';

export default function AppLayout() {
  return (
    <div className="min-h-screen flex bg-slate-50">
      {/* Sidebar */}
      <aside className="w-[240px] shrink-0 border-r bg-white">
        <div className="h-16 px-4 flex items-center gap-3 border-b">
          <div className="h-8 w-8 rounded-xl bg-gradient-to-br from-brand-500 to-brand-700" />
          <span className="font-semibold">CloudDrive</span>
        </div>

        <nav className="p-3 flex flex-col gap-2">
          <NavLink
            to="/app/drive"
            className={({ isActive }) =>
              `px-3 py-2 rounded-lg ${isActive ? 'bg-slate-900 text-white' : 'hover:bg-slate-100'}`
            }
          >
            Drive
          </NavLink>
          <NavLink
            to="/app/shares"
            className={({ isActive }) =>
              `px-3 py-2 rounded-lg ${isActive ? 'bg-slate-900 text-white' : 'hover:bg-slate-100'}`
            }
          >
            Shares
          </NavLink>
          <NavLink
            to="/app/short"
            className={({ isActive }) =>
              `px-3 py-2 rounded-lg ${isActive ? 'bg-slate-900 text-white' : 'hover:bg-slate-100'}`
            }
          >
            Short URLs
          </NavLink>
          <NavLink
            to="/app/settings"
            className={({ isActive }) =>
              `px-3 py-2 rounded-lg ${isActive ? 'bg-slate-900 text-white' : 'hover:bg-slate-100'}`
            }
          >
            Settings
          </NavLink>
        </nav>
      </aside>

      {/* Main content */}
      <main className="flex-1">
        <header className="h-16 border-b bg-white flex items-center justify-end px-6">
          {/* You can add user menu here */}
        </header>
        <div className="p-6">
          <Outlet />
        </div>
      </main>
    </div>
  );
}
