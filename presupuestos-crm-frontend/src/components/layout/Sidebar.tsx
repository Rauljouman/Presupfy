
import { NavLink } from 'react-router'

import { useAuth } from '../../contexts/AuthContext'
import { getNombreCompleto, rolLabels } from '../../types/auth'
import {
  Building2,
  FileText,
  LayoutGrid,
  Package,
  Settings,
  UserRound,
  Users,
  X,
} from 'lucide-react'

type SidebarProps = {
  isOpen: boolean
  onClose: () => void
}

const navigation = [
  { to: '/', label: 'Dashboard', icon: LayoutGrid },
  { to: '/presupuestos', label: 'Presupuestos', icon: FileText },
  { to: '/clientes', label: 'Clientes', icon: Users },
  { to: '/productos', label: 'Productos y servicios', icon: Package },
  { to: '/equipo', label: 'Equipo', icon: UserRound },
  { to: '/empresa', label: 'Mi empresa', icon: Building2 },
  { to: '/ajustes', label: 'Ajustes', icon: Settings },
]

function Sidebar({ isOpen, onClose }: SidebarProps) {

  const { usuario, loading, error } = useAuth()

  const nombreCompleto = usuario
    ? getNombreCompleto(usuario)
    : loading
      ? 'Cargando usuario...'
      : 'Usuario no disponible'

  const rol = usuario
    ? rolLabels[usuario.rol]
    : ''
    
  return (
    <>
      {isOpen && (
        <button
          type="button"
          aria-label="Cerrar menú"
          onClick={onClose}
          className="fixed inset-0 z-40 bg-slate-950/45 md:hidden"
        />
      )}

      <aside
        id="mobile-navigation"
        className={`
          fixed inset-y-0 left-0 z-50 w-[240px]
          bg-[#142C3E] text-white
          transition-transform duration-300
          md:sticky md:top-0 md:h-screen md:shrink-0
          ${isOpen ? 'translate-x-0' : '-translate-x-full'}
          md:translate-x-0
        `}
      >
        <div className="flex h-full flex-col">
          {/* USUARIO ACTUAL */}
          <div className="flex min-h-20 items-center justify-between gap-3 border-b border-white/10 px-4 py-4">
            <div className="flex min-w-0 items-center gap-3">
              <div className="flex h-10 w-10 shrink-0 items-center justify-center rounded-full bg-white/10">
                <UserRound
                  size={20}
                  aria-hidden="true"
                  className="text-white/80"
                />
              </div>

              <div className="min-w-0">
                <p className="truncate text-sm font-semibold text-white">
                  {nombreCompleto}
                </p>

                <p className="mt-0.5 text-xs text-white/60">
                  {rol || (error ? 'Error al cargar usuario' : ' ')}
                </p>
              </div>
            </div>

            <button
              type="button"
              onClick={onClose}
              aria-label="Cerrar menú"
              className="shrink-0 cursor-pointer rounded-md p-1.5 text-white/70 hover:bg-white/10 md:hidden"
            >
              <X size={18} aria-hidden="true" />
            </button>
          </div>

          {/* NAVEGACIÓN */}
          <nav
            aria-label="Navegación principal"
            className="min-h-0 flex-1 overflow-y-auto px-3 py-4"
          >
            <ul className="space-y-1.5">
              {navigation.map((item) => {
                const Icon = item.icon

                return (
                  <li key={item.to}>
                    <NavLink
                      to={item.to}
                      end={item.to === '/'}
                      onClick={onClose}
                      className={({ isActive }) => `
                        flex min-h-11 cursor-pointer items-center
                        gap-3 rounded-xl px-3 py-2.5
                        text-sm font-medium transition-colors
                        ${
                          isActive
                            ? 'bg-accent text-white'
                            : 'text-white/85 hover:bg-white/10 hover:text-white'
                        }
                      `}
                    >
                      <Icon
                        size={18}
                        aria-hidden="true"
                        className="shrink-0"
                      />
                      <span>{item.label}</span>
                    </NavLink>
                  </li>
                )
              })}
            </ul>
          </nav>
        </div>
      </aside>
    </>
  )
}

export default Sidebar
