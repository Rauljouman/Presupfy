
import { BrowserRouter, Route, Routes } from 'react-router'
import AppLayout from './components/layout/AppLayout'
import PagePlaceholder from './components/PagePlaceholder'
import LoginPage from './features/auth/LoginPage'

const pages = [
  { path: '/', title: 'Dashboard',
    description: 'Resumen de la actividad empresarial.' },
  { path: '/presupuestos', title: 'Presupuestos',
    description: 'Consulta y gestión de presupuestos.' },
  { path: '/clientes', title: 'Clientes',
    description: 'Gestión de clientes de tu empresa.' },
  { path: '/productos', title: 'Productos y servicios',
    description: 'Catálogo comercial de tu empresa.' },
  { path: '/equipo', title: 'Equipo',
    description: 'Usuarios e invitaciones.' },
  { path: '/empresa', title: 'Mi empresa',
    description: 'Información de tu empresa.' },
  { path: '/ajustes', title: 'Ajustes',
    description: 'Perfil y configuración de cuenta.' },
]

function App() {
  return (
    <BrowserRouter>
      <Routes>
        
        <Route path="/login" element={<LoginPage />} />

        <Route
          path="/registro-empresa"
          element={
            <PagePlaceholder
              title="Registrar empresa"
              description="Aquí construiremos el formulario de alta de empresa."
            />
          }
        />

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
      </Routes>
    </BrowserRouter>
  )
}

export default App
