
import { Menu } from 'lucide-react'

type HeaderProps = {
  onMenuClick: () => void
  isMenuOpen: boolean
}

function Header({ onMenuClick, isMenuOpen }: HeaderProps) {
  return (
    <header className="sticky top-0 z-30 flex h-16
      shrink-0 items-center justify-between border-b
      border-border bg-white px-4 md:px-6 lg:px-8">

      <div className="flex min-w-0 items-center gap-3">
        <button
          type="button"
          aria-label="Abrir menú"
          aria-controls="mobile-navigation"
          aria-expanded={isMenuOpen}
          onClick={onMenuClick}
          className="rounded-md p-2 hover:bg-background
            focus-visible:outline-2 focus-visible:outline-accent
            md:hidden"
        >
          <Menu size={23} />
        </button>

        <div className="min-w-0">
          <p className="truncate text-sm font-semibold text-primary">
            Mi empresa
          </p>
          <p className="hidden text-xs text-muted sm:block">
            Espacio de trabajo
          </p>
        </div>
      </div>

      <div className="flex items-center gap-4">
        <span className="hidden text-xs text-muted lg:block">
          Presupuestos CRM
        </span>

        <div
          aria-label="Usuario de ejemplo"
          className="flex h-9 w-9 items-center justify-center
          rounded-full border border-border bg-background
          text-xs font-semibold text-primary"
        >
          US
        </div>
      </div>
    </header>
  )
}

export default Header
