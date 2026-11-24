import type { ReactNode } from 'react'
import { Link, NavLink } from 'react-router'
import { ArrowLeft } from 'lucide-react'

import Footer from '../../components/layout/Footer'

type LegalLayoutProps = {
  title: string
  description: string
  children: ReactNode
}

const links = [
  { to: '/privacidad', label: 'Privacidad' },
  { to: '/terminos', label: 'Términos' },
  { to: '/cookies', label: 'Cookies' },
  { to: '/aviso-legal', label: 'Aviso legal' },
]

function LegalLayout({
  title,
  description,
  children,
}: LegalLayoutProps) {
  return (
    <div className="min-h-screen bg-background text-primary">
      <header className="border-b border-border bg-white/90">
        <div className="mx-auto flex max-w-5xl items-center justify-between gap-4 px-4 py-5 sm:px-6">
          <Link
            to="/login"
            className="cursor-pointer text-xl font-semibold tracking-tight text-primary"
          >
            Presupfy
          </Link>

          <Link
            to="/login"
            className="flex cursor-pointer items-center gap-2 text-sm text-muted transition-colors hover:text-accent"
          >
            <ArrowLeft size={16} aria-hidden="true" />
            <span>Volver</span>
          </Link>
        </div>
      </header>

      <main className="mx-auto max-w-5xl px-4 py-8 sm:px-6 sm:py-12">
        <div className="mb-8">
          <p className="mb-2 text-xs font-semibold uppercase tracking-[0.14em] text-accent">
            Información legal
          </p>

          <h1 className="text-3xl font-semibold tracking-tight sm:text-4xl">
            {title}
          </h1>

          <p className="mt-3 max-w-2xl text-sm leading-6 text-muted">
            {description}
          </p>
        </div>

        <div className="grid gap-6 md:grid-cols-[190px_minmax(0,1fr)]">
          <nav
            aria-label="Documentos legales"
            className="flex flex-wrap content-start gap-2 md:flex-col"
          >
            {links.map((link) => (
              <NavLink
                key={link.to}
                to={link.to}
                className={({ isActive }) =>
                  `cursor-pointer rounded-lg px-3 py-2.5 text-sm transition-colors ${
                    isActive
                      ? 'bg-primary font-medium text-white'
                      : 'text-muted hover:bg-white hover:text-primary'
                  }`
                }
              >
                {link.label}
              </NavLink>
            ))}
          </nav>

          <article className="min-w-0 rounded-2xl border border-border bg-white p-5 shadow-sm sm:p-8">
            <div
              role="note"
              className="mb-7 rounded-lg border border-amber-200 bg-amber-50 p-3 text-sm leading-6 text-amber-900"
            >
              Borrador en desarrollo. Este documento necesita
              completar y revisar los datos reales del servicio
              antes de su publicación.
            </div>

            <div className="space-y-7 text-sm leading-7 text-primary/85">
              {children}
            </div>
          </article>
        </div>
      </main>

      <Footer variant="light" />
    </div>
  )
}

export default LegalLayout
