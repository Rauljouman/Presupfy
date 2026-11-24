import { BrowserRouter, Route, Routes } from 'react-router'

import AppLayout from './components/layout/AppLayout'
import ProtectedRoute from './components/auth/ProtectedRoute'
import PagePlaceholder from './components/PagePlaceholder'
import LoginPage from './features/auth/LoginPage'
import ForgotPasswordPage from './features/auth/ForgotPasswordPage'
import ResetPasswordPage from './features/auth/ResetPasswordPage'
import RegisterCompanyPage from './features/auth/RegisterCompanyPage'

const pages = [
  {
    path: '/',
    title: 'Dashboard',
    description: 'Resumen de la actividad empresarial.',
  },
  {
    path: '/presupuestos',
    title: 'Presupuestos',
    description: 'Consulta y gestión de presupuestos.',
  },
  {
    path: '/clientes',
    title: 'Clientes',
    description: 'Gestión de clientes de tu empresa.',
  },
  {
    path: '/productos',
    title: 'Productos y servicios',
    description: 'Catálogo comercial de tu empresa.',
  },
  {
    path: '/equipo',
    title: 'Equipo',
    description: 'Usuarios e invitaciones.',
  },
  {
    path: '/empresa',
    title: 'Mi empresa',
    description: 'Información de tu empresa.',
  },
  {
    path: '/ajustes',
    title: 'Ajustes',
    description: 'Perfil y configuración de cuenta.',
  },
]

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Autenticación pública */}
        <Route path="/login" element={<LoginPage />} />

        <Route
          path="/recuperar-contrasena"
          element={<ForgotPasswordPage />}
        />

        <Route
          path="/restablecer-contrasena"
          element={<ResetPasswordPage />}
        />

        <Route
          path="/recuperar-contrasena"
          element={
            <PagePlaceholder
              title="Recuperar contraseña"
              description="Formulario de recuperación pendiente."
            />
          }
        />

        <Route
          path="/registro-empresa"
          element={<RegisterCompanyPage />}
        />

        <Route
          path="/registro-empresa"
          element={
            <PagePlaceholder
              title="Registrar nueva empresa"
              description="Formulario de registro pendiente de implementación."
            />
          }
        />

        {/* ÁREA EMPRESARIAL */}
        <Route element={<ProtectedRoute />}>
          <Route element={<AppLayout />}>
            {pages.map((page) => (
              <Route
                key={page.path}
                path={page.path}
                element={
                  <PagePlaceholder
                    title={page.title}
                    description={page.description}
                  />
                }
              />
            ))}
          </Route>
        </Route>

        {/* ÁREA SUPERADMIN */}
        <Route element={<ProtectedRoute onlySuperadmin />}>
          <Route
            path="/admin/empresas"
            element={
              <PagePlaceholder
                title="Administración de empresas"
                description="Administración de la plataforma Presupfy."
              />
            }
          />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
