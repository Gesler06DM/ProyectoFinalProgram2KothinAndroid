# Documentación de la aplicación Android
## Sistema de Control de Gastos Personales

**Desarrollador:** Gesler Duque  
**Curso:** Programación II  
**Entorno de desarrollo:** Android Studio  
**Lenguaje de programación:** Kotlin  
**Diseño de pantallas:** XML  

---

## 1. Introducción

Para desarrollar la parte visual y funcional de mi aplicación utilicé Android Studio.

Mi objetivo fue construir una aplicación móvil que permitiera al usuario registrar y consultar sus movimientos financieros de una manera sencilla.

Durante esta etapa trabajé en el diseño de las pantallas, la navegación, la interacción con los botones, la captura de información y las pruebas de funcionamiento.

## 2. Organización de la aplicación

Organicé la aplicación utilizando diferentes Activities.

Cada Activity representa una pantalla y tiene su propia responsabilidad.

También utilicé archivos XML para definir los elementos visuales que aparecen en cada pantalla.

Esta separación me permitió trabajar de manera más organizada y mantener independiente el diseño de la lógica de programación.

## 3. Pantalla de inicio

**Archivos principales:**

- `LoginActivity.kt`
- `activity_kogin.xml`

La pantalla inicial permite ingresar al perfil local guardado o dirigirse al formulario de registro.

Cuando el usuario selecciona ingresar, la aplicación consulta si existe un identificador de cliente almacenado en `SharedPreferences`.

Si existe, abre el Dashboard.

Si no existe, muestra un mensaje indicando que primero debe crearse una cuenta.

## 4. Pantalla de registro

**Archivos principales:**

- `RegistroActivity.kt`
- `activity_registro.xml`

Esta pantalla permite capturar la información personal y financiera inicial del usuario.

Incluye campos para:

- Nombre.
- Apellido.
- Edad.
- Actividad.
- Salario.
- Alimentación.
- Transporte.
- Estudios.
- Servicios.
- Otros gastos.

También muestra la fecha y hora del registro.

Para desarrollar el formulario utilicé componentes como `EditText`, `TextView`, `Spinner` y `Button`.

Cuando el usuario completa correctamente los datos, se guarda el perfil y se abre el Dashboard.

## 5. Pantalla principal o Dashboard

**Archivos principales:**

- `DashboardActivity.kt`
- `activity_dashboard.xml`

El Dashboard es la pantalla principal de mi aplicación.

Aquí se presenta un resumen de las finanzas del usuario.

Los elementos más importantes son:

- Mensaje de bienvenida.
- Saldo disponible.
- Total de ingresos.
- Total de gastos.
- Botón para agregar ingresos.
- Botón para agregar gastos.
- Botón para consultar el historial.
- Lista de movimientos recientes.

Para organizar los elementos utilicé un `ScrollView` y un `LinearLayout` vertical.

Esto permite desplazarse por la pantalla cuando el contenido supera el espacio disponible.

## 6. Pantalla Agregar ingreso

**Archivos principales:**

- `AgregarIngresoActivity.kt`
- `activity_agregar_ingreso.xml`

Esta pantalla permite registrar ingresos adicionales.

Incluye un campo para escribir el concepto y otro para ingresar el monto.

Cuando el usuario presiona Guardar ingreso, el programa valida los datos.

Si son correctos, devuelve el resultado al Dashboard para que se registre el movimiento.

También agregué un botón Regresar para volver al Dashboard sin guardar un ingreso incompleto.

## 7. Pantalla Agregar gasto

**Archivos principales:**

- `AgregarGastoActivity.kt`
- `activity_agregar_gasto.xml`

Esta pantalla permite registrar nuevos gastos.

El formulario contiene:

- Concepto.
- Monto.
- Categoría.
- Botón Guardar gasto.
- Botón Regresar.

Para seleccionar la categoría utilicé un `Spinner`.

El programa verifica que el concepto y el monto sean válidos antes de aceptar el registro.

Si el usuario presiona Regresar, vuelve al Dashboard sin guardar el gasto incompleto.

## 8. Historial de movimientos

**Archivos principales:**

- `HistorialActivity.kt`
- `activity_historial.xml`

En esta pantalla el usuario puede consultar los ingresos y gastos que ha registrado.

Implementé botones para filtrar la información por:

- Todos.
- Hoy.
- Semana.
- Mes.

También incorporé un resumen financiero que muestra los ingresos, gastos y balance correspondientes al período seleccionado.

Para mostrar los movimientos utilicé un contenedor `LinearLayout` al que se agregan elementos de texto desde Kotlin.

Cada movimiento puede seleccionarse para abrir las opciones de edición y eliminación.

## 9. Edición de movimientos

Para editar un movimiento utilicé `AlertDialog`.

Cuando el usuario selecciona un registro, aparece una ventana donde puede modificar el concepto y el monto.

Al guardar los cambios, se actualiza el registro en la base de datos.

También agregué una opción para eliminar movimientos, acompañada de una confirmación.

Esto evita que un registro se elimine accidentalmente al seleccionar la opción.

## 10. Navegación entre pantallas

Para comunicar las diferentes Activities utilicé `Intent`.

Por ejemplo:

```kotlin
val intent = Intent(
    this,
    HistorialActivity::class.java
)

startActivity(intent)
```

Este código permite abrir la pantalla del historial desde el Dashboard.

Para registrar ingresos y gastos utilicé `ActivityResultLauncher`, que permite recibir los datos cuando una pantalla termina.

También agregué botones Regresar en las pantallas principales.

En las pantallas secundarias utilicé `finish()` para volver a la Activity anterior sin abrir copias innecesarias.

## 11. Componentes visuales utilizados

Durante el desarrollo utilicé diferentes componentes de Android.

### TextView

Para mostrar títulos, instrucciones, fechas, montos y resúmenes.

### EditText

Para capturar nombres, descripciones, edades y cantidades.

### Button

Para ejecutar acciones como guardar, regresar, agregar ingresos, agregar gastos y consultar el historial.

### Spinner

Para permitir la selección de actividades y categorías.

### LinearLayout

Para organizar los componentes de forma vertical u horizontal.

### ScrollView

Para permitir el desplazamiento por pantallas con varios elementos.

### HorizontalScrollView

Para mostrar los filtros del historial en una fila que puede desplazarse horizontalmente.

### AlertDialog

Para mostrar ventanas de edición y confirmación de eliminación.

## 12. Diseño de la interfaz

Durante el desarrollo procuré que la aplicación fuera fácil de utilizar.

Organicé los formularios de manera que el usuario pudiera identificar rápidamente qué información debía ingresar.

También utilicé títulos, espacios entre componentes y botones con textos claros.

En el Dashboard diferencié visualmente los ingresos, los gastos y el saldo disponible.

Posteriormente incorporé botones de regreso para facilitar la navegación entre las pantallas.

## 13. Integración con la base de datos

La aplicación utiliza Room para trabajar con SQLite.

Los movimientos se almacenan en la tabla `movimientos`.

Cada registro contiene:

- Identificador del movimiento.
- Identificador del cliente.
- Tipo.
- Concepto.
- Categoría.
- Monto.
- Fecha y hora.

Esta información se utiliza para actualizar el Dashboard y mostrar el historial.

## 14. Pruebas realizadas

Para comprobar el funcionamiento utilicé el emulador de Android Studio.

Durante las pruebas verifiqué:

- Apertura de la aplicación.
- Registro de datos.
- Navegación entre pantallas.
- Registro de ingresos.
- Registro de gastos.
- Actualización del saldo.
- Consulta del historial.
- Filtros por fecha.
- Edición de movimientos.
- Eliminación con confirmación.
- Conservación de los datos al cerrar y abrir la aplicación.
- Funcionamiento de los botones Regresar.

También utilicé Database Inspector para comprobar que los movimientos se almacenaran correctamente en SQLite.

## 15. Herramientas utilizadas

### Android Studio

Lo utilicé para desarrollar, ejecutar y depurar la aplicación.

### Emulador Android

Lo utilicé para probar el comportamiento de las pantallas y las diferentes funcionalidades.

### Gradle

Lo utilicé como parte del sistema de compilación del proyecto Android.

### Git y GitHub

Los utilicé para registrar los cambios y mantener actualizado el repositorio del proyecto.

## 16. Dificultades durante el desarrollo

Durante el desarrollo tuve que trabajar en la conexión entre las pantallas, la recepción de información mediante Intents y el almacenamiento de los movimientos.

También fue necesario revisar las validaciones para evitar datos incorrectos y comprobar que el Dashboard reflejara los cambios realizados.

Otra parte importante fue implementar los filtros por fechas y asegurar que los movimientos pudieran recuperarse después de cerrar la aplicación.

Estas actividades me ayudaron a comprender mejor el funcionamiento de Android y su integración con Kotlin.

## 17. Conclusión

La parte de Android me permitió convertir la lógica desarrollada en Kotlin en una aplicación móvil que puede utilizarse mediante una interfaz gráfica.

Aprendí a crear formularios, organizar componentes visuales, comunicar pantallas, manejar eventos y conectar la aplicación con una base de datos local.

Considero que esta etapa fue fundamental porque me permitió integrar los diferentes conocimientos del curso en un proyecto funcional y organizado.