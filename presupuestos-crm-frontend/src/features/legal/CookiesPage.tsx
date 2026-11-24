import LegalLayout from './LegalLayout'

function CookiesPage() {
  return (
    <LegalLayout
      title="Política de cookies"
      description="Información sobre cookies y tecnologías de almacenamiento utilizadas por Presupfy."
    >
      <section>
        <h2 className="text-lg font-semibold text-primary">
          1. ¿Qué son las cookies?
        </h2>
        <p>
          Las cookies son pequeños archivos que los sitios
          web pueden almacenar en el navegador.
          Algunas aplicaciones también utilizan tecnologías
          similares, como el almacenamiento local.
        </p>
      </section>

      <section>
        <h2 className="text-lg font-semibold text-primary">
          2. Tecnologías utilizadas
        </h2>
        <p>
          Presupfy utiliza Supabase Auth para gestionar
          la autenticación de los usuarios.
        </p>
        <p>
          La implementación actual debe revisarse para
          identificar las cookies y demás mecanismos
          de almacenamiento que se utilicen realmente.
        </p>
        <p>
          [PENDIENTE: inventario de tecnologías,
          finalidad, proveedor y duración.]
        </p>
      </section>

      <section>
        <h2 className="text-lg font-semibold text-primary">
          3. Tecnologías necesarias
        </h2>
        <p>
          Determinadas tecnologías pueden resultar
          necesarias para mantener la sesión de usuario,
          proteger el acceso o prestar una funcionalidad
          expresamente solicitada.
        </p>
        <p>
          Su clasificación y la aplicabilidad de una exención
          del consentimiento deberán comprobarse
          caso por caso.
        </p>
      </section>

      <section>
        <h2 className="text-lg font-semibold text-primary">
          4. Cookies opcionales
        </h2>
        <p>
          [PENDIENTE: confirmar si se incorporarán
          herramientas de analítica, publicidad
          u otras tecnologías no necesarias.]
        </p>
        <p>
          Cuando sea obligatorio obtener consentimiento,
          estas tecnologías no se activarán antes
          de que el usuario lo otorgue.
        </p>
      </section>

      <section>
        <h2 className="text-lg font-semibold text-primary">
          5. Gestión de preferencias
        </h2>
        <p>
          [PENDIENTE: definir el mecanismo de configuración,
          rechazo y retirada del consentimiento si
          se utilizan tecnologías que lo requieran.]
        </p>
      </section>
    </LegalLayout>
  )
}

export default CookiesPage
