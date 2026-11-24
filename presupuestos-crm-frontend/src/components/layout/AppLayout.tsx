import { Link } from 'react-router'

type FooterProps = {
  variant?: 'dark' | 'light'
}

const legalLinks = [
  { label: 'Privacidad', path: '/privacidad' },
  { label: 'Términos', path: '/terminos' },
  { label: 'Cookies', path: '/cookies' },
  { label: 'Aviso legal', path: '/aviso-legal' },
]

const SUPPORT_PHONE = '678 912 345'
const LEGAL_ENTITY = 'RaulJouman S.L.'

function Footer({ variant = 'light' }: FooterProps) {
  const isDark = variant === 'dark'

  return (
    <footer
      className={`w-full ${
        isDark
          ? 'bg-[#142C3E] text-white'
          : 'border-t border-border bg-white text-primary'
      }`}
    >
      <div className="w-full px-4 py-4 sm:px-6 lg:px-8">
        <div className="flex flex-col gap-4 sm:flex-row sm:items-start sm:justify-between">
          <div>
            <p className="text-sm font-semibold">Presupfy</p>

            <p
              className={`mt-0.5 text-xs ${
                isDark ? 'text-white/70' : 'text-muted'
              }`}
            >
              Gestión de presupuestos empresariales
            </p>
          </div>

          <div className="text-left sm:text-right">
            <p
              className={`text-sm font-medium ${
                isDark ? 'text-white' : 'text-primary'
              }`}
            >
              {SUPPORT_PHONE}
            </p>

            <p
              className={`mt-0.5 text-xs ${
                isDark ? 'text-white/70' : 'text-muted'
              }`}
            >
              {LEGAL_ENTITY}
            </p>
          </div>
        </div>

        <div
          className={`mt-4 border-t pt-3 ${
            isDark ? 'border-white/15' : 'border-border'
          }`}
        >
          <div className="flex flex-col gap-2 sm:flex-row sm:items-center sm:justify-between">
            <nav
              aria-label="Enlaces legales"
              className="flex flex-wrap gap-x-4 gap-y-1"
            >
              {legalLinks.map((link) => (
                <Link
                  key={link.path}
                  to={link.path}
                  className={`cursor-pointer text-xs transition-colors hover:underline ${
                    isDark
                      ? 'text-white/75 hover:text-white'
                      : 'text-muted hover:text-primary'
                  }`}
                >
                  {link.label}
                </Link>
              ))}
            </nav>

            <p
              className={`text-xs ${
                isDark ? 'text-white/60' : 'text-muted'
              }`}
            >
              © {new Date().getFullYear()} Presupfy
            </p>
          </div>
        </div>
      </div>
    </footer>
  )
}

export default Footer