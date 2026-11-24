import LegalLayout from './LegalLayout'

function PrivacyPage() {
  return (
    <LegalLayout
      title="Política de privacidad"
      description="Información sobre el tratamiento de datos personales en Presupfy."
    >
      <section>
        <h2 className="text-lg font-semibold text-primary">
          1. Responsable del tratamiento
        </h2>
        <p>
          La identificación del responsable de Presupfy
          está pendiente de completar.
        </p>
        <p>
          <strong>Titular:</strong> [PENDIENTE: nombre o razón social]
          <br />
          <strong>Contacto:</strong> [PENDIENTE: correo de privacidad]
          <br />
          <strong>Domicilio:</strong> [PENDIENTE: datos aplicables]
        </p>
      </section>

      <section>
        <h2 className="text-lg font-semibold text-primary">
          2. Datos tratados
        </h2>
        <p>
          Presupfy está diseñado para gestionar cuentas de usuario,
          empresas, clientes y presupuestos. Según las funcionalidades
          utilizadas, pueden tratarse datos identificativos,
          profesionales y de contacto.
        </p>
        <p>
          Las categorías definitivas deberán confirmarse
          mediante un inventario de los datos realmente recogidos.
        </p>
      </section>

      <section>
        <h2 className="text-lg font-semibold text-primary">
          3. Finalidades y bases jurídicas
        </h2>
        <p>
          Entre las finalidades previstas están gestionar el acceso
          a las cuentas, administrar el servicio y permitir a las
          empresas utilizar las funcionalidades del CRM.
        </p>
        <p>
          [PENDIENTE: concretar las bases jurídicas aplicables
          a cada tratamiento y cualquier finalidad adicional.]
        </p>
      </section>

      <section>
        <h2 className="text-lg font-semibold text-primary">
          4. Datos introducidos por las empresas
        </h2>
        <p>
          Las empresas podrán introducir datos de sus clientes
          y contactos. Antes del lanzamiento deberá determinarse,
          para cada tratamiento, si Presupfy actúa como encargado
          y la empresa usuaria como responsable.
        </p>
        <p>
          Cuando corresponda, se formalizará el acuerdo
          de encargo del tratamiento.
        </p>
      </section>

      <section>
        <h2 className="text-lg font-semibold text-primary">
          5. Proveedores y destinatarios
        </h2>
        <p>
          El proyecto utiliza Supabase para autenticación y
          contempla una API Spring Boot para la gestión empresarial.
        </p>
        <p>
          [PENDIENTE: identificar los proveedores efectivos,
          ubicaciones de tratamiento, destinatarios y posibles
          transferencias internacionales.]
        </p>
      </section>

      <section>
        <h2 className="text-lg font-semibold text-primary">
          6. Conservación
        </h2>
        <p>
          [PENDIENTE: establecer los plazos o criterios de
          conservación, así como los procesos de eliminación
          de cuentas y datos.]
        </p>
      </section>

      <section>
        <h2 className="text-lg font-semibold text-primary">
          7. Derechos de las personas
        </h2>
        <p>
          Las personas interesadas podrán ejercer los derechos
          que les correspondan conforme a la normativa
          de protección de datos, entre ellos acceso,
          rectificación y supresión, así como otros
          derechos en las condiciones legalmente aplicables.
        </p>
        <p>
          [PENDIENTE: incluir el procedimiento y el correo
          de contacto para el ejercicio de derechos.]
        </p>
      </section>
    </LegalLayout>
  )
}

export default PrivacyPage
