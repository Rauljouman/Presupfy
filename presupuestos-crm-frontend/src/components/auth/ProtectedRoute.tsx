
import { useEffect, useState } from 'react'
import { Navigate, Outlet } from 'react-router'
import type { Session } from '@supabase/supabase-js'
import { supabase } from '../../lib/supabase'
import { api } from '../../lib/api'

type Rol = 'SUPERADMIN' | 'PROPIETARIO' | 'ADMIN' | 'USUARIO'

type UsuarioCRM = {
  id: number
  nombre: string
  rol: Rol
}

type ProtectedRouteProps = {
  onlySuperadmin?: boolean
}

function ProtectedRoute({
  onlySuperadmin = false,
}: ProtectedRouteProps) {
  const [session, setSession] = useState<Session | null>(null)
  const [usuario, setUsuario] = useState<UsuarioCRM | null>(null)
  const [checkingSession, setCheckingSession] = useState(true)
  const [checkingUser, setCheckingUser] = useState(false)
  const [error, setError] = useState(false)

  // 1. Consultar la sesión y escuchar sus cambios
  useEffect(() => {
    let active = true

    supabase.auth.getSession().then(({ data, error }) => {
      if (!active) return

      if (error) {
        setError(true)
      } else {
        setSession(data.session)
      }

      setCheckingSession(false)
    })

    const { data: listener } = supabase.auth.onAuthStateChange(
      (_event, newSession) => {
        if (!active) return
        setSession(newSession)
        setCheckingSession(false)
      }
    )

    return () => {
      active = false
      listener.subscription.unsubscribe()
    }
  }, [])

  // 2. Si hay sesión, consultar el usuario real de Spring Boot
  useEffect(() => {
    if (checkingSession) return

    let active = true

    if (!session) {
      setUsuario(null)
      setCheckingUser(false)
      return
    }

    async function loadUser() {
      setCheckingUser(true)
      setError(false)
      setUsuario(null)

      try {
        const response = await api.get<UsuarioCRM>(
          '/api/usuarios/me'
        )

        if (active) {
          setUsuario(response.data)
        }
      } catch {
        if (active) {
          setError(true)
        }
      } finally {
        if (active) {
          setCheckingUser(false)
        }
      }
    }

    loadUser()

    return () => {
      active = false
    }
  }, [session?.access_token, checkingSession])

  // 3. Esperar a completar las comprobaciones
  if (checkingSession || (session && checkingUser)) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-background">
        <p className="text-sm text-primary">
          Comprobando tu acceso...
        </p>
      </div>
    )
  }

  // 4. Si no hay sesión, volver al login
  if (!session && !error) {
    return <Navigate to="/login" replace />
  }

  // 5. No mostrar el CRM si falla la validación
  if (error || !usuario) {
    return (
      <div className="flex min-h-screen flex-col items-center justify-center gap-4 bg-background px-5 text-center">
        <h1 className="text-xl font-semibold text-primary">
          No se ha podido verificar tu acceso
        </h1>

        <p className="max-w-sm text-sm text-muted">
          Comprueba tu conexión o contacta con el administrador
          si tu cuenta todavía no tiene acceso a Presupfy.
        </p>

        <button
          type="button"
          onClick={() => window.location.reload()}
          className="cursor-pointer rounded-lg bg-accent px-5 py-3 text-sm font-medium text-white"
        >
          Reintentar
        </button>
      </div>
    )
  }

  // 6. Controlar las dos áreas de la aplicación
  if (onlySuperadmin && usuario.rol !== 'SUPERADMIN') {
    return <Navigate to="/" replace />
  }

  if (!onlySuperadmin && usuario.rol === 'SUPERADMIN') {
    return <Navigate to="/admin/empresas" replace />
  }

  return <Outlet />
}

export default ProtectedRoute
