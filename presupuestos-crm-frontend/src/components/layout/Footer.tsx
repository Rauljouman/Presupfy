
import { Link } from 'react-router'
import { Phone } from 'lucide-react'

type FooterProps = {
  variant?: 'dark' | 'light'
}

const legalLinks = [
  { label: 'Privacidad', path: '/privacidad' },
  { label: 'Términos', path: '/terminos' },
  { label: 'Cookies', path: '/cookies' },
  { label: 'Aviso legal', path: '/aviso-legal' },
]

const SUPPORT_PHONE = '678912345'
const LEGAL_ENTITY = 'RaulJouman S.L.'

function Footer({ variant = 'light' }: FooterProps) {
  const isDark = variant === 'dark'

  return (
    <footer
      className={`w-full shrink-0 ${
        isDark
          ? 'bg-[#142C3E] text-white'
          : 'border-t border-border bg-white text-primary'
      }`}
    >
      <div className="w-full px-3 py-3 sm:px-6 lg:px-8">

        {/* PARTE SUPERIOR */}
        <div className="flex items-start justify-between gap-3">

          {/* Marca */}
          <div className="min-w-0">
            <p className="text-xs font-semibold sm:text-sm">
              Presupfy
            </p>

            <p
              className={`mt-0.5 text-[10px] leading-4 sm:text-xs ${
                isDark ? 'text-white/65' : 'text-muted'
              }`}
            >
              Gestión de presupuestos empresariales
            </p>
          </div>

          {/* Contacto */}
          <div className="shrink-0 text-right">
            <a
              href={`tel:${SUPPORT_PHONE}`}
              className="inline-flex cursor-pointer items-center justify-end gap-1 text-[11px] font-medium hover:underline sm:text-xs"
            >
              <Phone size={12} aria-hidden="true" />
              {SUPPORT_PHONE}
            </a>

            <p
              className={`mt-0.5 text-[10px] leading-4 sm:text-xs ${
                isDark ? 'text-white/65' : 'text-muted'
              }`}
            >
              {LEGAL_ENTITY}
            </p>
          </div>
        </div>

        {/* DIVIDER */}
        <div
          className={`mt-3 border-t pt-2.5 ${
            isDark ? 'border-white/15' : 'border-border'
          }`}
        >
          {/* PARTE INFERIOR */}
          <div className="flex items-start justify-between gap-3">

            <nav
              aria-label="Enlaces legales"
              className="flex min-w-0 flex-wrap gap-x-3 gap-y-1"
            >
              {legalLinks.map((link) => (
                <Link
                  key={link.path}
                  to={link.path}
                  className={`cursor-pointer whitespace-nowrap text-[10px] transition-colors hover:underline sm:text-xs ${
                    isDark
                      ? 'text-white/70 hover:text-white'
                      : 'text-muted hover:text-primary'
                  }`}
                >
                  {link.label}
                </Link>
              ))}
            </nav>

            <p
              className={`shrink-0 whitespace-nowrap text-[10px] sm:text-xs ${
                isDark ? 'text-white/55' : 'text-muted'
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
