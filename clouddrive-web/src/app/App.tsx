import { Outlet } from 'react-router-dom';
import Navbar from '../components/ui/Navbar';

export default function App() {
  return (
    <div className="min-h-full">
      <Navbar />
      <main className="mx-auto max-w-6xl p-6">
        <Outlet />
      </main>
    </div>
  );
}
