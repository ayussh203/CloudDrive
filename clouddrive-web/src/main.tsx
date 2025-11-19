// src/main.tsx
import React from 'react';
import ReactDOM from 'react-dom/client';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';

import './index.css';

// PUBLIC
import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';

import PrivateRoute from './components/PrivateRoute';
import AppLayout from './components/AppLayout';

// APP PAGES
import DrivePage from './pages/DrivePage';
import SharesPage from './pages/SharesPage';
import ShortUrlsPage from './pages/ShortUrlsPage';
import SettingsPage from './pages/SettingsPage';

const router = createBrowserRouter([
  // Public landing + auth
  { path: '/', element: <HomePage /> },
  { path: '/login', element: <LoginPage /> },
  { path: '/register', element: <RegisterPage /> },

  // Protected app
  {
    element: <PrivateRoute />,
    children: [
      {
        path: '/app',
        element: <AppLayout />,
        children: [
          { path: '/app', element: <DrivePage /> },          // default app home
          { path: '/app/drive', element: <DrivePage /> },
          { path: '/app/shares', element: <SharesPage /> },
          { path: '/app/short', element: <ShortUrlsPage /> },
          { path: '/app/settings', element: <SettingsPage /> },
        ],
      },
    ],
  },
]);

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>,
);
