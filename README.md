# Ginko - Financial Solutions

## Descripción del Proyecto
Ginko es una aplicación de back-end diseñada para gestionar operaciones financieras, centrándose en el manejo de proveedores y órdenes de pago. Proporciona una API RESTful para crear, consultar y administrar proveedores, así como para gestionar el ciclo de vida completo de las órdenes de pago, desde su creación hasta su liquidación.

---
## Requisitos Previos
- Java 25
- Apache Maven 3.6 o superior
- Una IDE compatible con Spring Boot (ej. IntelliJ IDEA, VSCode)
- Git
---
## Ejecución en Local y pruebas
1.  **Clonar el repositorio:**
    ```bash
    git clone https://github.com/DavidCamelo/ginko.git
    cd ginko
    ```

2.  **Compilar y ejecutar la aplicación con Maven:**
    ```bash
    ./mvnw spring-boot:run
    ```

3.  La aplicación estará disponible en `http://localhost:8080`. 


4.  Para ejecutar el conjunto de pruebas unitarias y de integración, utilice el siguiente comando de Maven:
    ```bash
    ./mvnw test
    ```
---
## Documentación de la API (Swagger)
Una vez que la aplicación esté en ejecución, la documentación de la API de Swagger UI estará disponible en la siguiente URL:
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---
## Colección de Postman
En la raíz del proyecto se encuentra el archivo `Ginko.postman_collection.json`, que contiene una colección de Postman con ejemplos de todas las solicitudes a la API. Puede importarlo en Postman para probar los endpoints de manera sencilla.

---
## Decisiones de Diseño
A continuación se describen algunas de las decisiones clave de diseño tomadas durante el desarrollo del proyecto y su justificación.

### 1. Arquitectura Modular
- **Decisión:** La aplicación se organizó en módulos de negocio claros y desacoplados: `Proveedores`, `Órdenes de Pago` y `Reportes`.
- **Justificación:** Esta separación de conceptos (Separation of Concerns) facilita la mantenibilidad, escalabilidad y comprensión del código. Cada módulo tiene una responsabilidad única, lo que permite desarrollar y probar funcionalidades de forma aislada.

### 2. Idempotencia en la Creación de Órdenes de Pago
- **Decisión:** Se implementó la idempotencia en el endpoint de creación de órdenes de pago (`POST /orden-pago`) mediante el uso del encabezado `Idempotency-Key`.
- **Justificación:** En sistemas financieros, es crucial evitar la duplicación de transacciones. La idempotencia garantiza que si un cliente reintenta una solicitud (por ejemplo, debido a un error de red), no se creará una orden de pago duplicada, proporcionando así una operación más robusta y segura.

### 3. Manejo de Concurrencia con Bloqueo Optimista
- **Decisión:** Se utilizó un mecanismo de bloqueo optimista (`@Version` en la entidad `OrdenPago`) para gestionar las actualizaciones concurrentes en las transiciones de estado.
- **Justificación:** Previene condiciones de carrera (race conditions) cuando varios usuarios intentan modificar la misma orden de pago simultáneamente. A diferencia del bloqueo pesimista, no bloquea el recurso en la base de datos, lo que ofrece un mejor rendimiento en entornos de alta concurrencia. Si se detecta un conflicto, se lanza una excepción (`409 Conflict`), permitiendo que el cliente decida cómo proceder.

### 4. Uso de DTOs (Data Transfer Objects)
- **Decisión:** Se utilizó el patrón DTO para desacoplar la capa de la API de la capa de persistencia (entidades JPA).
- **Justificación:** Esto proporciona una capa de abstracción que permite que la API evolucione de forma independiente a la estructura de la base de datos. Además, mejora la seguridad al evitar la exposición accidental de campos internos de las entidades y permite adaptar la estructura de los datos a las necesidades específicas de los consumidores de la API.

### 5. Cobertura de Pruebas (Unitarias y de Integración)
- **Decisión:** Se crearon pruebas unitarias para los servicios y pruebas de integración para los flujos de API.
- **Justificación:** Asegura la fiabilidad y correctitud del código. Las pruebas unitarias validan la lógica de negocio de forma aislada, mientras que las pruebas de integración garantizan que los diferentes componentes del sistema interactúan correctamente, facilitando futuras refactorizaciones y la adición de nuevas funcionalidades.

## Modulo de Proveedores
Este modulo gestiona la informacion de los proveedores de la empresa.

### Funcionalidades
- **Crear Proveedor:** Permite registrar un nuevo proveedor en el sistema.
- **Obtener Proveedor por ID:** Busca y devuelve un proveedor especifico utilizando su identificador unico.
- **Obtener Lista de Proveedores:** Devuelve una lista paginada de todos los proveedores. Permite filtrar los proveedores por su estado (activo o inactivo).
- **Actualizar Proveedor:** Modifica la informacion de un proveedor existente.
- **Cambiar Estado del Proveedor:** Permite activar o inactivar un proveedor.

## Modulo de Ordenes de Pago
Este modulo se encarga de la creacion y gestion de las ordenes de pago a proveedores.

### Funcionalidades
- **Crear Orden de Pago:** Permite generar una nueva orden de pago asociada a un proveedor activo.
- **Obtener Orden de Pago por ID:** Busca y devuelve una orden de pago especifica a partir de su ID.
- **Obtener Lista de Ordenes de Pago:** Devuelve una lista paginada de ordenes de pago, con la opcion de filtrar por estado y/o proveedor.
- **Obtener Órdenes por Fechas:** Permite consultar las órdenes de pago según su fecha de vencimiento, con la opción de filtrar por un rango de fechas (inicio y fin), solo por fecha de inicio o solo por fecha de fin.
- **Transicionar Estado de Orden de Pago:** Permite cambiar el estado de una orden de pago. Las transiciones validas son:
    - De **BORRADOR** a **APROBADA** o **RECHAZADA**.
    - De **APROBADA** a **PAGADA**.

## Modulo de Reportes
Este modulo proporciona informes y datos consolidados sobre las operaciones del sistema.

### Funcionalidades
- **Obtener Reporte de Pagos por Proveedor:** Genera un reporte que totaliza los pagos realizados a un proveedor especifico, con la opcion de filtrar por un rango de fechas.
- **Obtener Ordenes de Pago Proximas a Vencer:** Devuelve una lista de las ordenes de pago en estado **APROBADA** cuya fecha de vencimiento se encuentre entre 30 días en el pasado y 30 días en el futuro.