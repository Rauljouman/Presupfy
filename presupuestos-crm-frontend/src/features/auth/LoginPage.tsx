
import { useState, type FormEvent } from 'react'
import { Link, useNavigate } from 'react-router'
import { AxiosError } from 'axios'
import {
  ArrowRight,
  Eye,
  EyeOff,
  LockKeyhole,
  Mail,
  LoaderCircle,
} from 'lucide-react'

import AuthLayout from '../../components/auth/AuthLayout'
import { supabase } from '../../lib/supabase'
import { api } from '../../lib/api'

type UsuarioCRM = {
  id: number
  nombre: string
  rol: 'SUPERADMIN' | 'PROPIETARIO' | 'ADMIN' | 'USUARIO'
  empresaId: number | null
}

function LoginPage() {
  const navigate = useNavigate()

  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [showPassword, setShowPassword] = useState(false)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()

    if (loading) return

    setError('')

    if (!email.trim() || !password) {
      setError('Introduce tu correo y contraseña.')
      return
    }

    setLoading(true)

    try {
      // Autenticar en Supabase
      const { error: authError } =
        await supabase.auth.signInWithPassword({
          email: email.trim(),
          password,
        })

      if (authError) {
        setError('Correo o contraseña incorrectos.')
        return
      }

      // Obtener usuario desde Spring Boot
      const response = await api.get<UsuarioCRM>(
        '/api/usuarios/me'
      )

      const usuario = response.data

      // Redirigir según el rol
      if (usuario.rol === 'SUPERADMIN') {
        navigate('/admin/empresas', { replace: true })
      } else {
        navigate('/', { replace: true })
      }

    } catch (err) {
      if (err instanceof AxiosError) {
        switch (err.response?.status) {
          case 401:
            setError('Tu sesión no ha podido validarse.')
            break

          case 403:
            setError('No tienes permiso para acceder al CRM.')
            break

          case 404:
            setError('Tu cuenta todavía no tiene acceso al CRM.')
            break

          default:
            setError(
              'No podemos conectar con el servidor. Inténtalo de nuevo.'
            )
        }
      } else {
        setError('Se ha producido un error inesperado.')
      }
    } finally {
      setLoading(false)
    }
  }

  return (
    <AuthLayout>
      {/* Encabezado */}
      <div className="mb-4 cursor-default select-none">
        <h1 className="text-[clamp(1.7rem,3vw,2.2rem)] font-semibold leading-tight tracking-tight text-primary">
          Inicio de sesión en Presupfy
        </h1>

        <p className="mt-1.5 text-sm leading-5 text-muted">
          Accede a tu espacio de trabajo y gestiona tus presupuestos.
        </p>
      </div>


      {/* Formulario */}
      <form
        onSubmit={handleSubmit}
        noValidate
        className="space-y-4"
      >
        {/* Correo */}
        <div className="group">
          <label
            htmlFor="email"
            className="mb-2 block cursor-pointer select-none text-sm font-medium text-primary"
          >
            Correo electrónico
          </label>

          <div className="relative">
            <Mail
              size={18}
              aria-hidden="true"
              className="pointer-events-none absolute top-1/2 left-4 -translate-y-1/2 text-muted transition-colors duration-200 group-focus-within:text-accent"
            />

            <input
              id="email"
              type="email"
              autoComplete="email"
              inputMode="email"
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
                focus:border-accent focus:ring-4 focus:ring-accent/10
                disabled:cursor-not-allowed disabled:opacity-60
              "
            />
          </div>
        </div>

        {/* Contraseña */}
        <div className="group">
          <div className="mb-2 flex flex-wrap items-center justify-between gap-2">
            <label
              htmlFor="password"
              className="cursor-pointer select-none text-sm font-medium text-primary"
            >
              Contraseña
            </label>

            <Link
              to="/recuperar-contrasena"
              className="
                cursor-pointer select-none
                text-xs font-medium text-accent
                transition-colors hover:text-[#BC5839]
                hover:underline
                focus-visible:outline-2 focus-visible:outline-accent
              "
            >
              ¿Has olvidado tu contraseña?
            </Link>
          </div>

          <div className="relative">
            <LockKeyhole
              size={18}
              aria-hidden="true"
              className="pointer-events-none absolute top-1/2 left-4 -translate-y-1/2 text-muted transition-colors duration-200 group-focus-within:text-accent"
            />

            <input
              id="password"
              type={showPassword ? 'text' : 'password'}
              autoComplete="current-password"
              placeholder="Introduce tu contraseña"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              disabled={loading}
              required
              className="
                h-12 w-full cursor-text select-text
                rounded-xl border border-border
                bg-white/95 pr-12 pl-12
                text-sm text-primary caret-accent
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
              aria-label={
                showPassword
                  ? 'Ocultar contraseña'
                  : 'Mostrar contraseña'
              }
              aria-pressed={showPassword}
              onClick={() => setShowPassword(!showPassword)}
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

        {/* Errores */}
        {error && (
          <div
            role="alert"
            className="cursor-default select-none rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700"
          >
            {error}
          </div>
        )}

        {/* Iniciar sesión */}
        <button
          type="submit"
          disabled={loading}
          className="
            group flex h-12 w-full cursor-pointer select-none
            items-center justify-center gap-3
            rounded-xl bg-accent px-5 font-medium text-white
            shadow-[0_6px_18px_rgba(213,107,73,0.22)]
            transition-all duration-200
            hover:-translate-y-0.5
            hover:bg-[#BC5839]
            hover:shadow-[0_10px_24px_rgba(213,107,73,0.28)]
            active:translate-y-0 active:shadow-sm
            focus-visible:outline-2 focus-visible:outline-offset-2
            focus-visible:outline-accent
            disabled:cursor-not-allowed disabled:opacity-60
            disabled:hover:translate-y-0
            motion-reduce:transition-none
            motion-reduce:hover:translate-y-0
          "
        >
          {loading ? (
            <>
              <LoaderCircle
                size={18}
                aria-hidden="true"
                className="animate-spin motion-reduce:animate-none"
              />
              Verificando acceso...
            </>
          ) : (
            <>
              Iniciar sesión
              <ArrowRight
                size={18}
                aria-hidden="true"
                className="
                  transition-transform duration-200
                  group-hover:translate-x-1
                  motion-reduce:transition-none
                  motion-reduce:group-hover:translate-x-0
                "
              />
            </>
          )}
        </button>
      </form>

      {/* Registrar nueva empresa */}
      <div className="mt-6">
        <div className="mb-4 flex cursor-default select-none items-center gap-3">
          <div
            aria-hidden="true"
            className="h-px flex-1 bg-border/80"
          />

          <span className="cursor-default select-none text-xs text-muted">
            ¿Todavía no tienes una empresa?
          </span>

          <div
            aria-hidden="true"
            className="h-px flex-1 bg-border/80"
          />
        </div>

        <Link
          to="/registro-empresa"
          className="
            flex h-12 w-full cursor-pointer select-none
            items-center justify-center
            rounded-xl border border-primary/20
            bg-white/70 px-5
            text-sm font-semibold text-primary
            transition-all duration-200
            hover:border-accent hover:bg-white/90
            hover:text-accent
            focus-visible:outline-2 focus-visible:outline-offset-2
            focus-visible:outline-accent
          "
        >
          Registrar nueva empresa
        </Link>
      </div>
    </AuthLayout>
  )
}

export default LoginPage
