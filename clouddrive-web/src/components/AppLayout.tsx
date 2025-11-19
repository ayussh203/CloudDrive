// src/components/AppLayout.tsx
import Topbar from './Topbar';
import Sidebar from './Sidebar';
import { Outlet } from 'react-router-dom';

export default function AppLayout() {
  return (
    <div className="min-h-screen">
      <Topbar />
      <div className="max-w-6xl mx-auto px-4 flex gap-4 py-4">
        <Sidebar />
        <main className="flex-1">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
