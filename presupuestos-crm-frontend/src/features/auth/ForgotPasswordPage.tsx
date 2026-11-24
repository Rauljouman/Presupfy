
import { useState, type FormEvent } from 'react'
import { Link } from 'react-router'
import {
  ArrowLeft,
  ArrowRight,
  CheckCircle2,
  LoaderCircle,
  Mail,
} from 'lucide-react'

import AuthLayout from '../../components/auth/AuthLayout'
import { supabase } from '../../lib/supabase'

function ForgotPasswordPage() {
  const [email, setEmail] = useState('')
  const [loading, setLoading] = useState(false)
  const [sent, setSent] = useState(false)
  const [error, setError] = useState('')

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()

    if (loading) return

    setError('')

    const cleanEmail = email.trim()

    if (!cleanEmail) {
      setError('Introduce tu correo electrónico.')
      return
    }

    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(cleanEmail)) {
      setError('Introduce un correo electrónico válido.')
      return
    }

    setLoading(true)

    try {
      const redirectTo = new URL(
        '/restablecer-contrasena',
        window.location.origin
      ).toString()

      const { error: resetError } =
        await supabase.auth.resetPasswordForEmail(cleanEmail, {
          redirectTo,
        })

      if (resetError) {
        setError(
          'No hemos podido procesar la solicitud. Inténtalo más tarde.'
        )
        return
      }

      setSent(true)
    } catch {
      setError(
        'No se ha podido conectar con el servicio. Inténtalo de nuevo.'
      )
    } finally {
      setLoading(false)
    }
  }

  return (
    <AuthLayout>
      {sent ? (
        /* CONFIRMACIÓN DEL ENVÍO */
        <div className="cursor-default select-none">
            <div className="flex items-center gap-3">
                <CheckCircle2
                size={28}
                aria-hidden="true"
                className="shrink-0 text-accent"
                />

                <h1 className="text-2xl font-semibold leading-tight tracking-tight text-primary">
                Revisa tu correo
                </h1>
            </div>

            <p className="mt-2 text-sm leading-5 text-muted">
                Si existe una cuenta asociada a ese correo,
                recibirás un enlace para restablecer tu contraseña.
            </p>

            <p className="mt-2 text-sm leading-5 text-muted">
                Comprueba también la carpeta de correo no deseado.
            </p>

            <Link
                to="/login"
                className="
                group mt-5 flex min-h-12 cursor-pointer select-none
                items-center justify-center gap-2 rounded-xl
                bg-accent px-4 text-sm font-semibold text-white
                transition-all duration-200
                hover:-translate-y-0.5 hover:bg-[#BC5839]
                focus-visible:outline-2 focus-visible:outline-accent
                motion-reduce:transform-none
                "
            >
                Volver al inicio de sesión
                <ArrowRight
                size={17}
                aria-hidden="true"
                className="transition-transform group-hover:translate-x-1"
                />
            </Link>
            </div>
      ) : (
        <>
          {/* ENCABEZADO */}
          <div className="mb-4 cursor-default select-none">
            <h1 className="text-[clamp(1.65rem,3vw,2rem)] font-semibold leading-tight tracking-tight text-primary">
              Recuperar contraseña
            </h1>

            <p className="mt-1.5 text-sm leading-5 text-muted">
              Introduce tu correo electrónico y te enviaremos
              un enlace para restablecer tu contraseña.
            </p>
          </div>

          {/* FORMULARIO */}
          <form
            onSubmit={handleSubmit}
            noValidate
            className="space-y-4"
          >
            <div className="group">
              <label
                htmlFor="recovery-email"
                className="mb-2 block cursor-pointer select-none text-sm font-medium text-primary"
              >
                Correo electrónico
              </label>

              <div className="relative">
                <Mail
                  size={18}
                  aria-hidden="true"
                  className="
                    pointer-events-none absolute
                    top-1/2 left-4 -translate-y-1/2
                    text-muted transition-colors duration-200
                    group-focus-within:text-accent
                  "
                />

                <input
                  id="recovery-email"
                  type="email"
                  inputMode="email"
                  autoComplete="email"
                  placeholder="tu@empresa.com"
                  value={email}
                  onChange={(event) => setEmail(event.target.value)}
                  disabled={loading}
                  required
                  className="
                    h-12 w-full cursor-text select-text
                    rounded-xl border border-border
                    bg-white/95 pr-4 pl-12
                    text-sm text-primary caret-accent
                    outline-none transition-all duration-200
                    placeholder:text-muted/60
                    hover:border-primary/30
                    focus:border-accent focus:ring-4
                    focus:ring-accent/10
                    disabled:cursor-not-allowed
                    disabled:opacity-60
                  "
                />
              </div>
            </div>

            {/* MENSAJE DE ERROR */}
            {error && (
              <p
                role="alert"
                className="
                  rounded-xl border border-red-200
                  bg-red-50 px-4 py-3
                  text-sm text-red-700
                "
              >
                {error}
              </p>
            )}

            {/* ENVIAR ENLACE */}
            <button
              type="submit"
              disabled={loading}
              className="
                group flex h-12 w-full cursor-pointer select-none
                items-center justify-center gap-3
                rounded-xl bg-accent px-4
                text-sm font-semibold text-white
                shadow-[0_6px_18px_rgba(213,107,73,0.22)]
                transition-all duration-200
                hover:-translate-y-0.5
                hover:bg-[#BC5839]
                hover:shadow-[0_10px_24px_rgba(213,107,73,0.28)]
                active:translate-y-0
                disabled:cursor-not-allowed
                disabled:opacity-60
                disabled:hover:translate-y-0
                motion-reduce:transform-none
              "
            >
              {loading ? (
                <>
                  <LoaderCircle
                    size={18}
                    aria-hidden="true"
                    className="animate-spin motion-reduce:animate-none"
                  />
                  Enviando enlace...
                </>
              ) : (
                <>
                  Enviar enlace de recuperación

                  <ArrowRight
                    size={17}
                    aria-hidden="true"
                    className="
                      transition-transform duration-200
                      group-hover:translate-x-1
                      motion-reduce:transition-none
                    "
                  />
                </>
              )}
            </button>
          </form>

          {/* VOLVER AL LOGIN */}
          <Link
            to="/login"
            className="
              mt-5 flex cursor-pointer select-none
              items-center justify-center gap-2
              text-sm font-medium text-primary
              transition-colors duration-200
              hover:text-accent
              focus-visible:outline-2
              focus-visible:outline-accent
            "
          >
            <ArrowLeft size={17} aria-hidden="true" />
            Volver al inicio de sesión
          </Link>
        </>
      )}
    </AuthLayout>
  )
}

export default ForgotPasswordPage
