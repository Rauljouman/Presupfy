
import { NavLink } from 'react-router'
import {
  LayoutDashboard, FileText, Users, Package,
  UserRound, Building2, Settings, X
} from 'lucide-react'

type SidebarProps = {
  isOpen: boolean
  onClose: () => void
}

const menuItems = [
  { label: 'Dashboard', path: '/', icon: LayoutDashboard },
  { label: 'Presupuestos', path: '/presupuestos', icon: FileText },
  { label: 'Clientes', path: '/clientes', icon: Users },
  { label: 'Productos y servicios', path: '/productos', icon: Package },
  { label: 'Equipo', path: '/equipo', icon: UserRound },
  { label: 'Mi empresa', path: '/empresa', icon: Building2 },
  { label: 'Ajustes', path: '/ajustes', icon: Settings },
]

function Sidebar({ isOpen, onClose }: SidebarProps) {
  return (
    <>
      {isOpen && (
        <button
          type="button"
          aria-label="Cerrar navegación"
          onClick={onClose}
          className="fixed inset-0 z-40 bg-black/50 md:hidden"
        />
      )}

      <aside
        id="mobile-navigation"
        aria-label="Navegación del CRM"
        className={`
          fixed inset-y-0 left-0 z-50 flex w-64
          flex-col bg-primary px-3 py-6 text-white
          transition-transform duration-200
          md:sticky md:top-0 md:h-screen md:w-18
          md:shrink-0 md:translate-x-0
          lg:w-64 lg:px-4
          ${isOpen ? 'translate-x-0' : '-translate-x-full'}
        `}
      >
        <div className="mb-10 flex items-center justify-between px-2">
          <span className="font-semibold md:hidden lg:block">
            Presupuestos CRM
          </span>

          <span className="hidden font-bold md:block lg:hidden">
            P.
          </span>

          <button
            type="button"
            onClick={onClose}
            aria-label="Cerrar menú"
            className="md:hidden"
          >
            <X size={22} />
          </button>
        </div>

        <nav className="flex flex-col gap-1">
          {menuItems.map((item) => {
            const Icon = item.icon

            return (
              <NavLink
                key={item.path}
                to={item.path}
                end={item.path === '/'}
                title={item.label}
                onClick={onClose}
                className={({ isActive }) =>
                  `flex min-h-11 items-center gap-3 rounded-md
                   px-3 py-3 transition-colors
                   focus-visible:outline-2 focus-visible:outline-white
                   ${isActive
                     ? 'bg-accent text-white'
                     : 'text-white/75 hover:bg-white/10 hover:text-white'
                   }`
                }
              >
                <Icon size={20} className="shrink-0" />
                <span className="md:hidden lg:block">
                  {item.label}
                </span>
              </NavLink>
            )
          })}
        </nav>
      </aside>
    </>
  )
}

export default Sidebar
