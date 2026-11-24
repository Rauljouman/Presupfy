import LegalLayout from './LegalLayout'

function TermsPage() {
  return (
    <LegalLayout
      title="Términos y condiciones"
      description="Condiciones previstas para el acceso y utilización de Presupfy."
    >
      <section>
        <h2 className="text-lg font-semibold text-primary">
          1. Descripción del servicio
        </h2>
        <p>
          Presupfy es una aplicación de gestión de presupuestos,
          clientes y productos o servicios para empresas.
          Su objetivo es facilitar la organización comercial.
        </p>
      </section>

      <section>
        <h2 className="text-lg font-semibold text-primary">
          2. Cuentas y acceso
        </h2>
        <p>
          El acceso a las áreas privadas se realiza mediante
          autenticación. Las funcionalidades disponibles
          dependen de los permisos asociados a cada cuenta.
        </p>
        <p>
          Los usuarios deberán utilizar sus credenciales
          de manera responsable.
        </p>
      </section>

      <section>
        <h2 className="text-lg font-semibold text-primary">
          3. Empresas y usuarios
        </h2>
        <p>
          Presupfy está diseñado para que cada empresa
          gestione su información en un espacio diferenciado.
        </p>
        <p>
          [PENDIENTE: definir el procedimiento de alta,
          aprobación, suspensión y administración de cuentas.]
        </p>
      </section>

      <section>
        <h2 className="text-lg font-semibold text-primary">
          4. Uso permitido
        </h2>
        <p>
          Los usuarios deberán utilizar el servicio de manera
          lícita y respetar los derechos de terceros.
          No deberán intentar acceder a datos de otras
          empresas ni eludir los mecanismos de seguridad.
        </p>
      </section>

      <section>
        <h2 className="text-lg font-semibold text-primary">
          5. Datos y presupuestos
        </h2>
        <p>
          Las empresas serán responsables de revisar
          la información comercial que introduzcan y
          los documentos que generen mediante la plataforma,
          conforme a las responsabilidades que finalmente
          se establezcan en las condiciones del servicio.
        </p>
      </section>

      <section>
        <h2 className="text-lg font-semibold text-primary">
          6. Disponibilidad y responsabilidades
        </h2>
        <p>
          [PENDIENTE: definir disponibilidad, soporte,
          mantenimiento, responsabilidades y limitaciones
          legalmente admisibles.]
        </p>
      </section>

      <section>
        <h2 className="text-lg font-semibold text-primary">
          7. Condiciones económicas y baja
        </h2>
        <p>
          [PENDIENTE: confirmar si Presupfy será gratuito,
          de pago o tendrá diferentes planes, así como
          las condiciones de cancelación y eliminación de datos.]
        </p>
      </section>
    </LegalLayout>
  )
}

export default TermsPage
