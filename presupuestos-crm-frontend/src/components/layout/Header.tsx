import { useState } from 'react'
import { useNavigate } from 'react-router'
import { LogOut, Menu } from 'lucide-react'

import { supabase } from '../../lib/supabase'

type HeaderProps = {
  onMenuClick: () => void
  isMenuOpen: boolean
}

function Header({ onMenuClick, isMenuOpen }: HeaderProps) {
  const navigate = useNavigate()
  const [loggingOut, setLoggingOut] = useState(false)
  const [error, setError] = useState('')

  async function handleLogout() {
    if (loggingOut) return

    setLoggingOut(true)
    setError('')

    try {
      const { error: signOutError } =
        await supabase.auth.signOut()

      if (signOutError) {
        setError('No se ha podido cerrar la sesión.')
        return
      }

      navigate('/login', { replace: true })
    } catch {
      setError('No se ha podido cerrar la sesión.')
    } finally {
      setLoggingOut(false)
    }
  }

  return (
    <header className="sticky top-0 z-30 flex min-h-16 shrink-0 items-center justify-between gap-3 border-b border-border bg-white px-4 md:px-6 lg:px-8">
      <div className="flex min-w-0 items-center gap-3">
        <button
          type="button"
          aria-label="Abrir menú"
          aria-controls="mobile-navigation"
          aria-expanded={isMenuOpen}
          onClick={onMenuClick}
          className="cursor-pointer rounded-md p-2 hover:bg-background focus-visible:outline-2 focus-visible:outline-accent md:hidden"
        >
          <Menu size={23} />
        </button>

        <div className="min-w-0">
          <p className="truncate text-sm font-semibold text-primary">
            Acme Soluciones, S.L.
          </p>

          <p className="hidden text-xs text-muted sm:block">
            Espacio de trabajo
          </p>
        </div>
      </div>

      <div className="flex items-center gap-3">
        {error && (
          <span role="alert" className="max-w-36 text-xs text-red-700">
            {error}
          </span>
        )}

        <button
          type="button"
          onClick={handleLogout}
          disabled={loggingOut}
          className="
            flex cursor-pointer items-center gap-2
            rounded-lg border border-border
            px-3 py-2 text-sm font-medium text-primary
            transition-colors duration-200
            hover:border-accent hover:bg-accent/5
            hover:text-accent
            focus-visible:outline-2 focus-visible:outline-accent
            disabled:cursor-not-allowed disabled:opacity-60
          "
        >
          <LogOut size={17} aria-hidden="true" />

          <span className="hidden sm:inline">
            {loggingOut ? 'Saliendo...' : 'Cerrar sesión'}
          </span>
        </button>
      </div>
    </header>
  )
}

export default Header