
import { useState, type ReactNode } from 'react'
import { Link } from 'react-router'
import {
  ArrowLeft,
  Building2,
  ChevronDown,
  LockKeyhole,
  UserRound,
} from 'lucide-react'

import AuthLayout from '../../components/auth/AuthLayout'

type SectionId = 'empresa' | 'propietario' | 'acceso'

type SectionProps = {
  id: SectionId
  title: string
  icon: ReactNode
  open: boolean
  onToggle: () => void
  children: ReactNode
}

function FormSection({
  id,
  title,
  icon,
  open,
  onToggle,
  children,
}: SectionProps) {
  return (
    <section className="overflow-hidden rounded-xl border border-primary/15 bg-white/65">
      <h2>
        <button
          type="button"
          id={`section-${id}`}
          aria-expanded={open}
          aria-controls={`panel-${id}`}
          onClick={onToggle}
          className="
            flex min-h-14 w-full cursor-pointer select-none
            items-center gap-3 px-4 py-3 text-left
            text-sm font-semibold text-primary
            transition-colors hover:bg-white/70
            focus-visible:outline-2 focus-visible:outline-accent
          "
        >
          <span className="text-accent">{icon}</span>
          <span className="flex-1">{title}</span>
          <ChevronDown
            size={18}
            className={`shrink-0 transition-transform duration-200 ${
              open ? 'rotate-180' : ''
            }`}
          />
        </button>
      </h2>

      <div
        id={`panel-${id}`}
        aria-labelledby={`section-${id}`}
        className={`
            gap-4 border-t border-primary/10 px-4 py-4
            ${open ? 'grid' : 'hidden'}
        `}
        >
        {children}
        </div>
    </section>
  )
}

type FieldProps = {
  label: string
  name: string
  type?: string
  placeholder?: string
  autoComplete?: string
  required?: boolean
  minLength?: number
  maxLength?: number
}

function Field({
  label,
  name,
  type = 'text',
  placeholder,
  autoComplete,
  required = true,
  minLength,
  maxLength,
}: FieldProps) {
  return (
    <div className="min-w-0">
      <label
        htmlFor={name}
        className="mb-1.5 block cursor-pointer select-none text-sm font-medium text-primary"
      >
        {label}
      </label>

      <input
        id={name}
        name={name}
        type={type}
        placeholder={placeholder}
        autoComplete={autoComplete}
        required={required}
        minLength={minLength}
        maxLength={maxLength}
        className="
          h-11 w-full min-w-0 cursor-text select-text
          rounded-lg border border-border bg-white/95
          px-3 text-sm text-primary caret-accent
          outline-none transition-all duration-200
          placeholder:text-muted/60
          hover:border-primary/30
          focus:border-accent focus:ring-4 focus:ring-accent/10
        "
      />
    </div>
  )
}

function RegisterCompanyPage() {
  const [openSections, setOpenSections] = useState<SectionId[]>([
    'empresa',
  ])

  function toggleSection(id: SectionId) {
    setOpenSections((current) =>
      current.includes(id)
        ? current.filter((section) => section !== id)
        : [...current, id]
    )
  }

  const isOpen = (id: SectionId) => openSections.includes(id)

  return (
    <AuthLayout wide>
      <header className="mb-5 cursor-default select-none">
        <h1 className="text-[clamp(1.65rem,3vw,2rem)] font-semibold leading-tight tracking-tight text-primary">
          Registra tu empresa
        </h1>

        <p className="mt-1.5 text-sm leading-5 text-muted">
          Prepara tu espacio de trabajo para gestionar
          clientes y presupuestos con Presupfy.
        </p>
      </header>

      <form className="space-y-3">
        {/* DATOS DE LA EMPRESA */}
        <FormSection
          id="empresa"
          title="Datos de la empresa"
          icon={<Building2 size={20} />}
          open={isOpen('empresa')}
          onToggle={() => toggleSection('empresa')}
        >
          <Field
            label="Nombre de la empresa"
            name="nombreEmpresa"
            placeholder="Ej. Acme Soluciones, S.L."
            autoComplete="organization"
          />

          <div className="grid gap-4 sm:grid-cols-2">
            <Field
              label="NIF / CIF"
              name="nif"
              placeholder="Identificación fiscal"
              maxLength={20}
            />

            <Field
              label="Correo de contacto"
              name="emailEmpresa"
              type="email"
              placeholder="empresa@ejemplo.com"
            />
          </div>
        </FormSection>

        {/* DATOS DEL PROPIETARIO */}
        <FormSection
          id="propietario"
          title="Datos del propietario"
          icon={<UserRound size={20} />}
          open={isOpen('propietario')}
          onToggle={() => toggleSection('propietario')}
        >
          <div className="grid gap-4 sm:grid-cols-2">
            <Field
              label="Nombre"
              name="nombrePropietario"
              placeholder="Tu nombre"
              autoComplete="given-name"
            />

            <Field
              label="Apellidos"
              name="apellidosPropietario"
              placeholder="Tus apellidos"
              autoComplete="family-name"
            />
          </div>

          <Field
            label="Teléfono"
            name="telefono"
            type="tel"
            placeholder="Teléfono de contacto"
            autoComplete="tel"
          />
        </FormSection>

        {/* ACCESO A LA CUENTA */}
        <FormSection
          id="acceso"
          title="Acceso a la cuenta"
          icon={<LockKeyhole size={20} />}
          open={isOpen('acceso')}
          onToggle={() => toggleSection('acceso')}
        >
          <Field
            label="Correo electrónico de acceso"
            name="emailUsuario"
            type="email"
            placeholder="tu@empresa.com"
            autoComplete="email"
          />

          <div className="grid gap-4 sm:grid-cols-2">
            <Field
              label="Contraseña"
              name="password"
              type="password"
              placeholder="Mínimo 8 caracteres"
              autoComplete="new-password"
              minLength={8}
            />

            <Field
              label="Confirmar contraseña"
              name="confirmPassword"
              type="password"
              placeholder="Repite tu contraseña"
              autoComplete="new-password"
              minLength={8}
            />
          </div>
        </FormSection>

        {/* CONSENTIMIENTO */}
        <label className="flex cursor-pointer items-start gap-3 px-1 pt-3 text-sm leading-5 text-primary">
          <input
            type="checkbox"
            name="aceptaTerminos"
            className="mt-1 h-4 w-4 shrink-0 cursor-pointer accent-[#D56B49]"
          />

          <span>
            Acepto los términos de uso y la política de privacidad
            de Presupfy.
          </span>
        </label>

        {/* ENVÍO PENDIENTE DE BACKEND */}
        <div className="pt-3">
          <button
            type="button"
            disabled
            aria-describedby="registro-pendiente"
            className="
              flex h-12 w-full cursor-not-allowed select-none
              items-center justify-center rounded-xl bg-accent
              px-4 text-sm font-semibold text-white opacity-60
            "
          >
            Crear empresa
          </button>

          <p
            id="registro-pendiente"
            className="mt-2 text-center text-xs leading-5 text-muted"
          >
            El alta pública de empresas todavía no está disponible.
          </p>
        </div>
      </form>

      {/* VOLVER */}
      <Link
        to="/login"
        className="
          mt-5 flex cursor-pointer select-none
          items-center justify-center gap-2
          text-sm font-medium text-primary
          transition-colors hover:text-accent
          focus-visible:outline-2 focus-visible:outline-accent
        "
      >
        <ArrowLeft size={17} aria-hidden="true" />
        Volver al inicio de sesión
      </Link>
    </AuthLayout>
  )
}

export default RegisterCompanyPage
