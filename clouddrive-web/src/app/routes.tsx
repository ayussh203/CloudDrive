import { createBrowserRouter } from 'react-router-dom';
import App from './App';
// import Login from '../pages/Login';
// import Register from '../pages/Register';
// import Dashboard from '../pages/Dashboard';
// import FileDetail from '../pages/FileDetail';
import Login from '../pages/Login';
import Register from '../pages/Register';
import Dashboard from '../pages/Dashboard';
import FileDetail from '../pages/FileDetail';



export const router = createBrowserRouter([
  { path: '/', element: <App />, children: [
    { index: true, element: <Dashboard /> },
    { path: 'files/:id', element: <FileDetail /> },
  ]},
  { path: '/login', element: <Login /> },
  { path: '/register', element: <Register /> },
]);
