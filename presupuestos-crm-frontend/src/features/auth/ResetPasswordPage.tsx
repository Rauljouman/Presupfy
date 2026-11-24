
import { useEffect, useState, type FormEvent } from 'react'
import { Link, useNavigate } from 'react-router'
import {
  ArrowLeft,
  ArrowRight,
  CheckCircle2,
  Eye,
  EyeOff,
  LoaderCircle,
  LockKeyhole,
} from 'lucide-react'

import AuthLayout from '../../components/auth/AuthLayout'
import { supabase } from '../../lib/supabase'

function ResetPasswordPage() {
  const navigate = useNavigate()

  // Contraseñas
  const [password, setPassword] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [showPassword, setShowPassword] = useState(false)

  // Estados
  const [checking, setChecking] = useState(true)
  const [canReset, setCanReset] = useState(false)
  const [loading, setLoading] = useState(false)
  const [success, setSuccess] = useState(false)
  const [error, setError] = useState('')

  // Comprobar la sesión establecida mediante el enlace.
  useEffect(() => {
    let active = true

    const hash = new URLSearchParams(
      window.location.hash.substring(1)
    )
    const query = new URLSearchParams(window.location.search)

    const linkError = hash.get('error') || query.get('error')

    if (linkError) {
      setError('El enlace es inválido o ha caducado.')
      setChecking(false)
      return
    }

    const { data: listener } = supabase.auth.onAuthStateChange(
      (event, session) => {
        if (!active) return

        if (event === 'PASSWORD_RECOVERY' && session) {
          setCanReset(true)
          setError('')
          setChecking(false)
        }
      }
    )

    async function checkSession() {
      try {
        const { data, error: sessionError } =
          await supabase.auth.getSession()

        if (!active) return

        if (sessionError || !data.session) {
          setError(
            'No se ha podido validar el enlace. Solicita uno nuevo.'
          )
        } else {
          setCanReset(true)
        }
      } catch {
        if (active) {
          setError('No se ha podido comprobar la sesión.')
        }
      } finally {
        if (active) {
          setChecking(false)
        }
      }
    }

    void checkSession()

    return () => {
      active = false
      listener.subscription.unsubscribe()
    }
  }, [])

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()

    if (loading || !canReset) return

    setError('')

    if (password.length < 8) {
      setError('La contraseña debe tener al menos 8 caracteres.')
      return
    }

    if (password !== confirmPassword) {
      setError('Las contraseñas no coinciden.')
      return
    }

    setLoading(true)

    try {
      const { error: updateError } =
        await supabase.auth.updateUser({
          password,
        })

      if (updateError) {
        setError(
          'No se ha podido actualizar la contraseña. Comprueba los requisitos e inténtalo de nuevo.'
        )
        return
      }

      setSuccess(true)
      setPassword('')
      setConfirmPassword('')
    } catch {
      setError('Se ha producido un error de conexión.')
    } finally {
      setLoading(false)
    }
  }

  async function returnToLogin() {
    if (loading) return

    setLoading(true)
    setError('')

    try {
      const { error: signOutError } =
        await supabase.auth.signOut({ scope: 'local' })

      if (signOutError) {
        setError('No se ha podido cerrar la sesión.')
        return
      }

      navigate('/login', { replace: true })
    } catch {
      setError('No se ha podido cerrar la sesión.')
    } finally {
      setLoading(false)
    }
  }

  // 1. VALIDANDO EL ENLACE
  if (checking) {
    return (
      <AuthLayout>
        <div
          role="status"
          className="flex cursor-default select-none items-center justify-center gap-3 py-6 text-sm text-primary"
        >
          <LoaderCircle
            size={20}
            aria-hidden="true"
            className="animate-spin motion-reduce:animate-none"
          />
          Validando enlace...
        </div>
      </AuthLayout>
    )
  }

  // 2. CONTRASEÑA ACTUALIZADA
  if (success) {
    return (
      <AuthLayout>
        <div className="cursor-default select-none">

          {/* Icono alineado con el título */}
          <div className="flex items-center gap-3">
            <CheckCircle2
              size={28}
              aria-hidden="true"
              className="shrink-0 text-accent"
            />

            <h1 className="text-2xl font-semibold leading-tight tracking-tight text-primary">
              Contraseña actualizada
            </h1>
          </div>

          <p className="mt-2 text-sm leading-5 text-muted">
            Tu contraseña se ha cambiado correctamente.
            Ya puedes iniciar sesión con tus nuevas credenciales.
          </p>

          {error && (
            <p
              role="alert"
              className="mt-4 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700"
            >
              {error}
            </p>
          )}

          <button
            type="button"
            onClick={returnToLogin}
            disabled={loading}
            className="
              group mt-5 flex h-12 w-full cursor-pointer
              select-none items-center justify-center gap-3
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
                Cerrando sesión...
              </>
            ) : (
              <>
                Ir al inicio de sesión

                <ArrowRight
                  size={17}
                  aria-hidden="true"
                  className="transition-transform duration-200 group-hover:translate-x-1 motion-reduce:transition-none"
                />
              </>
            )}
          </button>
        </div>
      </AuthLayout>
    )
  }

  // 3. ENLACE INVÁLIDO O CADUCADO
  if (!canReset) {
    return (
      <AuthLayout>
        <div className="cursor-default select-none">
          <h1 className="text-2xl font-semibold leading-tight tracking-tight text-primary">
            Enlace no válido
          </h1>

          <p
            role="alert"
            className="mt-2 text-sm leading-5 text-muted"
          >
            {error || 'No se ha podido validar el enlace.'}
          </p>

          <Link
            to="/recuperar-contrasena"
            className="
              group mt-5 flex h-12 w-full cursor-pointer
              select-none items-center justify-center gap-3
              rounded-xl bg-accent px-4
              text-sm font-semibold text-white
              transition-all duration-200
              hover:-translate-y-0.5 hover:bg-[#BC5839]
              motion-reduce:transform-none
            "
          >
            Solicitar otro enlace

            <ArrowRight
              size={17}
              aria-hidden="true"
              className="transition-transform duration-200 group-hover:translate-x-1"
            />
          </Link>
        </div>
      </AuthLayout>
    )
  }

  // 4. FORMULARIO PARA ESTABLECER CONTRASEÑA
  return (
    <AuthLayout>
      {/* Encabezado */}
      <div className="mb-4 cursor-default select-none">
        <h1 className="text-[clamp(1.65rem,3vw,2rem)] font-semibold leading-tight tracking-tight text-primary">
          Nueva contraseña
        </h1>

        <p className="mt-1.5 text-sm leading-5 text-muted">
          Introduce tu nueva contraseña para recuperar
          el acceso a Presupfy.
        </p>
      </div>

      <form
        onSubmit={handleSubmit}
        noValidate
        className="space-y-4"
      >
        {/* Nueva contraseña */}
        <div className="group">
          <label
            htmlFor="new-password"
            className="mb-2 block cursor-pointer select-none text-sm font-medium text-primary"
          >
            Nueva contraseña
          </label>

          <div className="relative">
            <LockKeyhole
              size={18}
              aria-hidden="true"
              className="pointer-events-none absolute top-1/2 left-4 -translate-y-1/2 text-muted transition-colors group-focus-within:text-accent"
            />

            <input
              id="new-password"
              type={showPassword ? 'text' : 'password'}
              autoComplete="new-password"
              placeholder="Mínimo 8 caracteres"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              disabled={loading}
              required
              minLength={8}
              className="
                h-12 w-full cursor-text select-text
                rounded-xl border border-border bg-white/95
                pr-12 pl-12 text-sm text-primary caret-accent
                outline-none transition-all duration-200
                placeholder:text-muted/60
                hover:border-primary/30
                focus:border-accent focus:ring-4 focus:ring-accent/10
                disabled:cursor-not-allowed disabled:opacity-60
              "
            />

            <button
              type="button"
              disabled={loading}
              onClick={() => setShowPassword((value) => !value)}
              aria-label={
                showPassword
                  ? 'Ocultar contraseña'
                  : 'Mostrar contraseña'
              }
              aria-pressed={showPassword}
              className="
                absolute top-1/2 right-3 -translate-y-1/2
                cursor-pointer select-none rounded-md p-2
                text-muted transition-colors hover:text-accent
                focus-visible:outline-2 focus-visible:outline-accent
                disabled:cursor-not-allowed
              "
            >
              {showPassword ? (
                <EyeOff size={18} aria-hidden="true" />
              ) : (
                <Eye size={18} aria-hidden="true" />
              )}
            </button>
          </div>
        </div>

        {/* Confirmar contraseña */}
        <div className="group">
          <label
            htmlFor="confirm-password"
            className="mb-2 block cursor-pointer select-none text-sm font-medium text-primary"
          >
            Confirmar contraseña
          </label>

          <div className="relative">
            <LockKeyhole
              size={18}
              aria-hidden="true"
              className="pointer-events-none absolute top-1/2 left-4 -translate-y-1/2 text-muted transition-colors group-focus-within:text-accent"
            />

            <input
              id="confirm-password"
              type={showPassword ? 'text' : 'password'}
              autoComplete="new-password"
              placeholder="Repite tu contraseña"
              value={confirmPassword}
              onChange={(event) => setConfirmPassword(event.target.value)}
              disabled={loading}
              required
              className="
                h-12 w-full cursor-text select-text
                rounded-xl border border-border bg-white/95
                pr-4 pl-12 text-sm text-primary caret-accent
                outline-none transition-all duration-200
                placeholder:text-muted/60
                hover:border-primary/30
                focus:border-accent focus:ring-4 focus:ring-accent/10
                disabled:cursor-not-allowed disabled:opacity-60
              "
            />
          </div>
        </div>

        {/* Mensajes de error */}
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

        {/* Guardar contraseña */}
        <button
          type="submit"
          disabled={loading}
          className="
            group flex h-12 w-full cursor-pointer
            select-none items-center justify-center gap-3
            rounded-xl bg-accent px-4
            text-sm font-semibold text-white
            shadow-[0_6px_18px_rgba(213,107,73,0.22)]
            transition-all duration-200
            hover:-translate-y-0.5
            hover:bg-[#BC5839]
            hover:shadow-[0_10px_24px_rgba(213,107,73,0.28)]
            active:translate-y-0
            disabled:cursor-not-allowed disabled:opacity-60
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
              Actualizando...
            </>
          ) : (
            <>
              Guardar nueva contraseña

              <ArrowRight
                size={17}
                aria-hidden="true"
                className="transition-transform duration-200 group-hover:translate-x-1 motion-reduce:transition-none"
              />
            </>
          )}
        </button>
      </form>

      {/* Volver al login */}
      <Link
        to="/login"
        className="
          mt-5 flex cursor-pointer select-none
          items-center justify-center gap-2
          text-sm font-medium text-primary
          transition-colors duration-200
          hover:text-accent
          focus-visible:outline-2 focus-visible:outline-accent
        "
      >
        <ArrowLeft size={17} aria-hidden="true" />
        Volver al inicio de sesión
      </Link>
    </AuthLayout>
  )
}

export default ResetPasswordPage
