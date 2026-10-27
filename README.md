# Proyecto Final de Programación II
## Sistema de Control de Gastos Personales

**Desarrollador:** Gesler Duque  
**Curso:** Programación II  
**Universidad:** Universidad Mariano Gálvez de Guatemala  
**Lenguaje principal:** Kotlin  
**Entorno de desarrollo:** Android Studio  
**Base de datos:** SQLite mediante Room  

---

## 1. Introducción

Para mi proyecto final de Programación II desarrollé una aplicación móvil de control de gastos personales, utilizando Kotlin como lenguaje de programación y Android Studio como entorno de desarrollo.

La idea principal de este proyecto fue crear una herramienta que permitiera a una persona llevar un mejor control de sus finanzas, registrando sus ingresos y gastos, consultando sus movimientos y visualizando cuánto dinero tiene disponible.

Durante el desarrollo busqué aplicar los conocimientos adquiridos en el curso, especialmente la programación orientada a objetos, el manejo de datos, las validaciones, las colecciones y la organización del código.

Además, incorporé una base de datos local mediante Room y SQLite para que los movimientos financieros permanecieran almacenados incluso después de cerrar la aplicación.

## 2. Objetivo general

Desarrollar una aplicación móvil que permita registrar, administrar y consultar los ingresos y gastos personales, utilizando Kotlin, Android Studio y una base de datos local.

## 3. Objetivos específicos

- Aplicar los conocimientos de programación adquiridos durante el curso.
- Implementar un formulario para registrar la información del usuario.
- Permitir el registro de ingresos y gastos.
- Calcular automáticamente el saldo disponible.
- Organizar los movimientos financieros mediante categorías.
- Consultar el historial de movimientos.
- Implementar filtros por día, semana y mes.
- Permitir editar y eliminar movimientos.
- Almacenar los movimientos utilizando Room y SQLite.
- Implementar validaciones para evitar registros incorrectos.
- Diseñar una interfaz que sea sencilla de comprender y utilizar.

## 4. Descripción del proyecto

Mi aplicación funciona como un sistema de control financiero personal.

Al iniciar, el usuario puede registrar sus datos personales, incluyendo nombre, apellido, edad, actividad y salario.

También puede ingresar sus gastos mensuales estimados, como alimentación, transporte, estudios, servicios y otros.

Después del registro, la aplicación muestra un panel principal o Dashboard, donde se presenta un resumen de la situación financiera del usuario.

Desde este panel se pueden agregar nuevos ingresos, registrar gastos y consultar el historial.

Cada movimiento contiene información como su concepto, tipo, categoría, monto y fecha de registro.

## 5. Funcionalidades principales

### Registro del usuario

Implementé un formulario que solicita los datos necesarios para crear un perfil local.

El programa valida que los campos obligatorios estén completos y que los valores numéricos sean correctos.

### Panel financiero

Desarrollé un Dashboard que muestra:

- Saldo disponible.
- Total de ingresos.
- Total de gastos.
- Movimientos registrados.

### Registro de ingresos

El usuario puede agregar ingresos adicionales indicando el concepto y el monto correspondiente.

### Registro de gastos

El usuario puede registrar gastos seleccionando una categoría e ingresando su descripción y cantidad.

### Historial de movimientos

Implementé un historial que permite consultar los ingresos y gastos registrados.

Los movimientos se presentan con su información correspondiente y pueden seleccionarse para editarlos o eliminarlos.

### Filtros por período

Agregué opciones para consultar movimientos de:

- Todos los períodos.
- Hoy.
- Semana actual.
- Mes actual.

El historial también presenta un resumen de ingresos, gastos y balance del período seleccionado.

### Persistencia de datos

Utilicé Room con SQLite para almacenar los movimientos financieros de manera local.

Esto permite conservar los registros después de cerrar y volver a abrir la aplicación.

## 6. Cálculos financieros

Para calcular el saldo disponible utilicé la siguiente lógica:

**Total de ingresos = Salario + Ingresos registrados**

**Total de gastos = Gastos mensuales estimados + Gastos registrados**

**Saldo disponible = Total de ingresos - Total de gastos**

Estos cálculos permiten que el usuario conozca su situación financiera de forma sencilla.

En el historial, el balance del período se calcula utilizando únicamente los movimientos correspondientes al filtro seleccionado.

## 7. Tecnologías utilizadas

### Kotlin

Lo utilicé para desarrollar la lógica del programa, validar los datos, realizar los cálculos y controlar el funcionamiento de las pantallas.

### Android Studio

Fue el entorno de desarrollo que utilicé para crear, organizar, ejecutar y probar la aplicación Android.

### XML

Lo utilicé para definir la estructura visual de las pantallas, incluyendo botones, campos de texto y otros componentes.

### Room y SQLite

Los utilicé para almacenar y administrar los movimientos financieros dentro del dispositivo.

### SharedPreferences

Lo utilicé para conservar información del perfil local del usuario y recuperar determinados datos al abrir la aplicación.

### Git y GitHub

Los utilicé para llevar el control de versiones, registrar los cambios realizados y mantener una copia del proyecto en un repositorio remoto.

## 8. Organización del proyecto

El proyecto está organizado en archivos que cumplen diferentes responsabilidades.

Entre los principales se encuentran:

- `LoginActivity.kt`: pantalla inicial.
- `RegistroActivity.kt`: registro de datos del usuario.
- `DashboardActivity.kt`: panel financiero.
- `AgregarIngresoActivity.kt`: registro de ingresos.
- `AgregarGastoActivity.kt`: registro de gastos.
- `HistorialActivity.kt`: consulta, edición y eliminación de movimientos.
- `MovimientoEntity.kt`: estructura de los movimientos almacenados.
- `MovimientoDao.kt`: operaciones de acceso a la base de datos.
- `AppDatabase.kt`: configuración de Room.

También utilicé archivos XML para definir el diseño de las pantallas.

## 9. Pruebas realizadas

Durante el desarrollo ejecuté la aplicación en un emulador Android para comprobar su funcionamiento.

Realicé pruebas de registro, validación de campos, almacenamiento de ingresos y gastos, consultas del historial, edición, eliminación y filtros por fecha.

También comprobé que los movimientos permanecieran almacenados después de cerrar y abrir nuevamente la aplicación.

Utilicé Database Inspector de Android Studio para observar los registros guardados en SQLite.

## 10. Conclusión

Con este proyecto logré aplicar diferentes conocimientos de Programación II en una aplicación móvil funcional.

Una de las partes más importantes fue aprender a conectar la lógica desarrollada en Kotlin con las pantallas de Android y el almacenamiento de información mediante Room.

También reforcé mis conocimientos sobre validaciones, cálculos, manejo de eventos, organización del código y control de versiones.

Considero que este proyecto me permitió comprender mejor cómo se desarrolla una aplicación que resuelve una necesidad cotidiana, como administrar los ingresos y gastos personales.