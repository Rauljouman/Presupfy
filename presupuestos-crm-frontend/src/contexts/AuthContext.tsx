
import {
  createContext,
  useContext,
  useEffect,
  useState,
  type ReactNode,
} from 'react'

import { obtenerUsuarioActual } from '../services/usuarioServices'
import type { UsuarioActual } from '../types/auth'

type AuthContextType = {
  usuario: UsuarioActual | null
  loading: boolean
  error: string | null
  recargarUsuario: () => Promise<void>
}

const AuthContext = createContext<AuthContextType | undefined>(
  undefined
)

export function AuthProvider({
  children,
}: {
  children: ReactNode
}) {
  const [usuario, setUsuario] = useState<UsuarioActual | null>(
    null
  )
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  async function recargarUsuario() {
    setLoading(true)
    setError(null)

    try {
      const data = await obtenerUsuarioActual()
      setUsuario(data)
    } catch (err) {
      setUsuario(null)

      setError(
        err instanceof Error
          ? err.message
          : 'No se ha podido cargar el usuario.'
      )
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    void recargarUsuario()
  }, [])

  return (
    <AuthContext.Provider
      value={{
        usuario,
        loading,
        error,
        recargarUsuario,
      }}
    >
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const context = useContext(AuthContext)

  if (!context) {
    throw new Error(
      'useAuth debe utilizarse dentro de AuthProvider.'
    )
  }

  return context
}
