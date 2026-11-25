
import { supabase } from '../lib/supabase'
import type { UsuarioActual } from '../types/auth'

const API_URL =
  import.meta.env.VITE_API_URL ?? 'http://localhost:8080'

export async function obtenerUsuarioActual(): Promise<UsuarioActual> {
  // Obtenemos la sesión de Supabase.
  const { data, error } = await supabase.auth.getSession()

  if (error || !data.session) {
    throw new Error('No hay una sesión activa.')
  }

  // Enviamos el JWT a nuestro backend.
  const response = await fetch(`${API_URL}/api/usuarios/me`, {
    method: 'GET',
    headers: {
      Authorization: `Bearer ${data.session.access_token}`,
      Accept: 'application/json',
    },
  })

  if (!response.ok) {
    if (response.status === 401) {
      throw new Error('La sesión no es válida.')
    }

    if (response.status === 403) {
      throw new Error('No tienes permisos para acceder.')
    }

    throw new Error(
      `Error al obtener el usuario: ${response.status}`
    )
  }

  return (await response.json()) as UsuarioActual
}
