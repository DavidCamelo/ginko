# Ginko
Ginko Financial Solutions

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
- **Transicionar Estado de Orden de Pago:** Permite cambiar el estado de una orden de pago. Las transiciones validas son:
    - De **BORRADOR** a **APROBADA** o **RECHAZADA**.
    - De **APROBADA** a **PAGADA**.
