# Documentación del desarrollo en Kotlin
## Proyecto de Control de Gastos Personales

**Desarrollador:** Gesler Duque  
**Curso:** Programación II  
**Lenguaje:** Kotlin  

---

## 1. Introducción

En esta parte de mi proyecto explico cómo utilicé Kotlin para desarrollar el funcionamiento de mi aplicación de control de gastos personales.

Kotlin fue el lenguaje que utilicé para programar los procesos internos, las validaciones, los cálculos financieros, la administración de movimientos y la comunicación con la base de datos.

Durante el desarrollo organicé el código en diferentes clases para que cada una tuviera una responsabilidad específica.

## 2. Organización del código

En lugar de colocar toda la programación en una sola clase, distribuí las funciones entre diferentes archivos.

Esto me permitió separar el registro del usuario, los cálculos, los ingresos, los gastos y el historial.

Entre los archivos más importantes se encuentran:

- `LoginActivity.kt`
- `RegistroActivity.kt`
- `DashboardActivity.kt`
- `AgregarIngresoActivity.kt`
- `AgregarGastoActivity.kt`
- `HistorialActivity.kt`
- `Movimiento.kt`
- `MovimientoEntity.kt`
- `MovimientoDao.kt`
- `AppDatabase.kt`

## 3. Registro y validación de información

En `RegistroActivity.kt` desarrollé el proceso para capturar los datos del usuario.

Para obtener la información de los campos utilicé instrucciones como:

```kotlin
val nombreTexto = nombre.text.toString().trim()
val edadNumero = edad.text.toString().toIntOrNull()
val salarioNumero = salario.text.toString().toDoubleOrNull()
```

Utilicé `trim()` para eliminar espacios innecesarios y las funciones `toIntOrNull()` y `toDoubleOrNull()` para convertir los datos sin provocar una excepción cuando el usuario escribe un valor incorrecto.

También implementé condiciones para comprobar que el nombre y apellido no estén vacíos, que la edad sea mayor que cero y que los montos sean válidos.

## 4. Identificación del usuario

Para identificar el perfil registrado utilicé un identificador generado mediante UUID.

```kotlin
val idCliente = UUID.randomUUID().toString()
```

Este identificador se guarda junto con la información del perfil.

Posteriormente se utiliza para relacionar los movimientos financieros con el usuario correspondiente.

En la versión actual de mi aplicación se maneja un perfil local guardado en el dispositivo.

## 5. Almacenamiento del perfil

Para conservar los datos del usuario utilicé `SharedPreferences`.

```kotlin
val preferencias = getSharedPreferences(
    "perfil_usuario",
    MODE_PRIVATE
)
```

Con este mecanismo almaceno información como el nombre, apellido, edad, actividad, salario, gastos estimados e identificador del cliente.

Esto permite recuperar la información cuando se vuelve a abrir la aplicación.

## 6. Cálculos financieros

Una de las funciones principales del programa es calcular el dinero disponible.

En `DashboardActivity.kt` utilizo los siguientes cálculos:

```kotlin
val totalIngresos = salario + ingresosRegistrados
val totalGastos = gastosEstimados + gastosRegistrados
val saldoDisponible = totalIngresos - totalGastos
```

Primero sumo el salario con los ingresos adicionales registrados.

Después sumo los gastos estimados con los nuevos gastos.

Finalmente resto el total de gastos al total de ingresos.

El resultado se muestra en quetzales dentro del Dashboard.

## 7. Registro de ingresos

En `AgregarIngresoActivity.kt` programé un formulario para capturar el concepto y el monto de un ingreso.

Antes de aceptar la información, verifico que el concepto no esté vacío y que el monto sea un número válido y mayor que cero.

Cuando la información es correcta, la pantalla devuelve los datos al Dashboard mediante un `Intent`.

El Dashboard recibe el resultado y registra el movimiento.

## 8. Registro de gastos

En `AgregarGastoActivity.kt` implementé el registro de gastos.

El usuario debe indicar:

- Concepto del gasto.
- Monto.
- Categoría.

Las categorías disponibles incluyen alimentación, transporte, servicios, educación, salud y otros.

También agregué validaciones para evitar conceptos vacíos y montos incorrectos.

## 9. Uso de clases de datos

Utilicé clases `data class` para representar la información de los movimientos.

Por ejemplo:

```kotlin
data class Movimiento(
    val id: Long,
    var tipo: String,
    var concepto: String,
    var categoria: String,
    var monto: Double,
    val fechaHora: Long
)
```

Esta clase permite agrupar los atributos relacionados con un movimiento financiero.

En el caso de la base de datos, utilicé otra clase llamada `MovimientoEntity`, preparada para trabajar con Room.

## 10. Implementación de Room

Para almacenar los movimientos utilicé Room, una biblioteca de Android que facilita el trabajo con SQLite.

Organicé esta implementación en tres componentes principales.

### MovimientoEntity

Representa la estructura de la tabla `movimientos`.

```kotlin
@Entity(tableName = "movimientos")
data class MovimientoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val idCliente: String,
    val tipo: String,
    val concepto: String,
    val categoria: String,
    val monto: Double,
    val fechaHora: Long
)
```

Cada movimiento contiene un identificador, el identificador del cliente, su tipo, concepto, categoría, monto y fecha.

### MovimientoDao

Es la interfaz que define las operaciones sobre la base de datos.

Utilicé anotaciones como:

```kotlin
@Insert
@Update
@Delete
@Query
```

Estas permiten insertar, actualizar, eliminar y consultar registros.

Por ejemplo:

```kotlin
@Query("""
    SELECT * FROM movimientos
    WHERE idCliente = :idCliente
    ORDER BY fechaHora DESC, id DESC
""")
suspend fun obtenerTodos(
    idCliente: String
): List<MovimientoEntity>
```

Esta consulta recupera los movimientos correspondientes al identificador del cliente y los ordena del más reciente al más antiguo.

### AppDatabase

Es la clase encargada de proporcionar el acceso a la base de datos Room utilizada por la aplicación.

## 11. Uso de corrutinas

Para ejecutar operaciones relacionadas con la base de datos utilicé corrutinas de Kotlin.

Por ejemplo:

```kotlin
lifecycleScope.launch {
    movimientoDao.insertar(
        MovimientoEntity(
            idCliente = idCliente,
            tipo = "Ingreso",
            concepto = concepto,
            categoria = "Ingresos",
            monto = monto,
            fechaHora = System.currentTimeMillis()
        )
    )
}
```

Las corrutinas permiten organizar operaciones asíncronas sin bloquear innecesariamente la interfaz principal.

## 12. Consultas por período

En `HistorialActivity.kt` implementé filtros para consultar movimientos según diferentes períodos.

Utilicé `Calendar` para calcular las fechas de inicio y finalización de cada filtro.

Los períodos disponibles son:

- Hoy.
- Semana actual.
- Mes actual.

También existe la opción Todos, que muestra los movimientos sin limitarse a un período.

Las consultas utilizan el identificador del cliente para recuperar los registros correspondientes.

## 13. Edición y eliminación de movimientos

Implementé la edición de movimientos mediante una ventana emergente.

El usuario puede seleccionar un movimiento, modificar su concepto y monto y guardar los cambios.

Para actualizar el registro utilizo:

```kotlin
movimientoDao.actualizar(movimientoEditado)
```

Para eliminarlo utilizo:

```kotlin
movimientoDao.eliminar(movimiento)
```

Antes de eliminar un movimiento, el programa solicita una confirmación.

## 14. Validaciones

Durante el desarrollo incorporé validaciones para evitar errores al introducir información.

Entre ellas se encuentran:

- Campos obligatorios vacíos.
- Edades no válidas.
- Montos negativos.
- Montos iguales a cero cuando se registra un movimiento.
- Valores que no pueden convertirse correctamente a números.
- Montos no finitos.
- Conceptos incorrectos.

Estas validaciones ayudan a mantener la coherencia de los datos registrados.

## 15. Manejo de fechas

Para almacenar la fecha y hora de cada movimiento utilicé:

```kotlin
System.currentTimeMillis()
```

Este valor representa el momento del registro en milisegundos.

Para mostrar la fecha en un formato comprensible utilicé `SimpleDateFormat`.

Por ejemplo:

```kotlin
SimpleDateFormat(
    "dd/MM/yyyy hh:mm a",
    Locale("es", "GT")
)
```

## 16. Lo que aprendí

Durante esta parte del proyecto reforcé mis conocimientos sobre Kotlin, clases, funciones, condiciones, conversiones de datos, colecciones, manejo de eventos y programación asíncrona.

También aprendí a organizar la información mediante entidades y consultas a una base de datos local.

Una de las partes que más me ayudó fue comprender cómo los datos capturados en una pantalla pueden procesarse, almacenarse y posteriormente recuperarse para mostrarlos al usuario.

## 17. Conclusión

El desarrollo en Kotlin fue una parte fundamental de mi proyecto porque permitió implementar toda la lógica necesaria para el control de gastos.

Considero que logré aplicar de manera práctica los conocimientos adquiridos en Programación II, especialmente la organización del código, el manejo de datos, las validaciones y la persistencia de información.