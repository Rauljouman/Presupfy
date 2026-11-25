
export type UsuarioRol =
  | 'SUPERADMIN'
  | 'PROPIETARIO'
  | 'ADMIN'
  | 'USUARIO'

export type UsuarioActual = {
  id: number
  nombre: string
  primerApellido: string
  segundoApellido: string | null
  email: string
  telefono: string | null
  rol: UsuarioRol
  empresaId: number | null
  empresaNombre?: string | null
}

export const rolLabels: Record<UsuarioRol, string> = {
  SUPERADMIN: 'Superadministrador',
  PROPIETARIO: 'Propietario',
  ADMIN: 'Administrador',
  USUARIO: 'Usuario',
}

export function getNombreCompleto(usuario: UsuarioActual): string {
  return [
    usuario.nombre,
    usuario.primerApellido,
    usuario.segundoApellido,
  ]
    .filter(Boolean)
    .join(' ')
}
