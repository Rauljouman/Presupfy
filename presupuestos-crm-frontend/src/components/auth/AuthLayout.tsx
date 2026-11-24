
import type { ReactNode } from 'react'

type AuthLayoutProps = {
  children: ReactNode
  wide?: boolean
}

function AuthLayout({
  children,
  wide = false,
}: AuthLayoutProps) {
  return (
    <div className="relative min-h-screen overflow-hidden bg-background cursor-default select-none">
      {/* Imagen de fondo */}
      <div
        aria-hidden="true"
        className="pointer-events-none absolute inset-0 bg-cover bg-center"
        style={{
          backgroundImage: "url('/login-building.jpg')",
        }}
      />

      {/* Degradado */}
      <div
        aria-hidden="true"
        className="pointer-events-none absolute inset-0 bg-[linear-gradient(135deg,rgba(20,44,62,0.48),rgba(20,44,62,0.24))]"
      />

      {/* Contenido */}
      <main className="relative z-10 flex min-h-screen items-center justify-center px-4 py-8 sm:px-6 md:px-8">
        <div
          className={`
            login-enter relative w-full
            ${wide ? 'max-w-[620px]' : 'max-w-[480px]'}
            rounded-[24px] border border-white/60
            bg-white/70 p-6 sm:p-8
            shadow-[0_24px_70px_rgba(10,28,43,0.25)]
            backdrop-blur-lg backdrop-saturate-150
          `}
        >
          {children}
        </div>
      </main>
    </div>
  )
}

export default AuthLayout
