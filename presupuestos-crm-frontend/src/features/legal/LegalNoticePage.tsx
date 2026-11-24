
import LegalLayout from './LegalLayout'

function LegalNoticePage() {
  return (
    <LegalLayout
      title="Aviso legal"
      description="Información identificativa y condiciones generales del sitio Presupfy."
    >
      <section>
        <h2 className="text-lg font-semibold text-primary">
          1. Titular del sitio
        </h2>
        <p>
          <strong>Nombre comercial:</strong> Presupfy
          <br />
          <strong>Titular legal:</strong> [PENDIENTE]
          <br />
          <strong>NIF:</strong> [PENDIENTE]
          <br />
          <strong>Domicilio:</strong> [PENDIENTE]
          <br />
          <strong>Correo electrónico:</strong> [PENDIENTE]
          <br />
          <strong>Datos registrales:</strong> [SI CORRESPONDE]
        </p>
      </section>

      <section>
        <h2 className="text-lg font-semibold text-primary">
          2. Finalidad del sitio
        </h2>
        <p>
          Presupfy es un proyecto de plataforma
          de gestión empresarial destinado a organizar
          clientes, productos y servicios y presupuestos.
        </p>
      </section>

      <section>
        <h2 className="text-lg font-semibold text-primary">
          3. Propiedad intelectual
        </h2>
        <p>
          [PENDIENTE: identificar los titulares de los derechos
          sobre la aplicación, textos, imagen de fondo,
          elementos gráficos y otros recursos utilizados.]
        </p>
      </section>

      <section>
        <h2 className="text-lg font-semibold text-primary">
          4. Contacto
        </h2>
        <p>
          [PENDIENTE: facilitar una dirección de contacto
          válida para cuestiones relacionadas con el servicio.]
        </p>
      </section>
    </LegalLayout>
  )
}

export default LegalNoticePage
