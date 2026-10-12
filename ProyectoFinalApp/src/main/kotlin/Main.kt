package org.example
data class Gasto(
    var descripcion: String,
    var categoria: String,
    var monto: Double
)
fun main() {
    val listaGastos = mutableListOf<Gasto>()
    println("=================================")
    println("     CONTROL DE GASTOS")
    println("=================================")

    print("Ingrese tu nombre: ")
    val nombre = readln()
    print("Ingresa tu apellido: ")
    val apellido = readln()
    print("Ingresa tu edad:")
    val edad = readln().toInt()

    print("Ingrese su salario mensual: Q")
    val salario = readln().toDouble()

    println()
    println("Bienvenido/a, $nombre $apellido")
    println("Edad: $edad años")
    println("Su ingreso mensual es: Q$salario")
    println()
    println("¿Qué actividades realiza?")
    println("1. Estudia")
    println("2. Trabaja")
    println("3. Estudia y trabaja")
    println("4. Otra actividad")

    print("Seleccione una opción: ")
    val opcionActividad = readln().toInt()

    var gastoActividad = 0.0

    when (opcionActividad) {

        1 -> {
            println("Actividad seleccionada: Estudia")
            print("Ingrese cuánto gasta al mes en sus estudios: Q")
            gastoActividad = readln().toDouble()
        }

        2 -> {
            println("Actividad seleccionada: Trabaja")
            print("Ingrese cuánto gasta al mes relacionado con su trabajo: Q")
            gastoActividad = readln().toDouble()
        }

        3 -> {
            println("Actividad seleccionada: Estudia y trabaja")

            print("Ingrese cuánto gasta al mes en sus estudios: Q")
            val gastoEstudio = readln().toDouble()

            print("Ingrese cuánto gasta al mes relacionado con su trabajo: Q")
            val gastoTrabajo = readln().toDouble()

            gastoActividad = gastoEstudio + gastoTrabajo
        }

        4 -> {
            print("Ingrese su actividad: ")
            val otraActividad = readln()

            println("Actividad seleccionada: $otraActividad")

            print("Ingrese cuánto gasta al mes en esta actividad: Q")
            gastoActividad = readln().toDouble()
        }

        else -> println("Opción no válida")
    }

    println()
    println("Gasto mensual de su actividad: Q$gastoActividad")
    println()
    println("===== GASTOS MENSUALES =====")

    print("Ingrese cuánto gasta al mes en alimentación: Q")
    val alimentacion = readln().toDouble()

    print("Ingrese cuánto gasta al mes en transporte: Q")
    val transporte = readln().toDouble()

    print("Ingrese cuánto gasta al mes en servicios (luz, agua, internet, etc.): Q")
    val servicios = readln().toDouble()

    print("Ingrese cuánto gasta al mes en otros gastos: Q")
    val otrosGastos = readln().toDouble()

    val totalGastos = gastoActividad + alimentacion + transporte + servicios + otrosGastos
    var dineroDisponible = salario - totalGastos

    println()
    println("===== RESUMEN =====")
    println("Ingreso mensual: Q$salario")
    println("Gastos de actividad: Q$gastoActividad")
    println("Alimentación: Q$alimentacion")
    println("Transporte: Q$transporte")
    println("Servicios: Q$servicios")
    println("Otros gastos: Q$otrosGastos")
    println("Total de gastos: Q$totalGastos")
    println("Dinero disponible: Q$dineroDisponible")

    var opcionMenu: Int

    do {
        println()
        println("===== MENÚ =====")
        println("1. Registrar un gasto extra")
        println("2. Registrar un ingreso extra")
        println("3. Ver dinero disponible")
        println("4. Salir")
        print("Seleccione una opción: ")

        opcionMenu = readln().toInt()
        when (opcionMenu) {

            1 -> {
                print("Ingrese el gasto extra: Q")
                val gastoExtra = readln().toDouble()

                dineroDisponible -= gastoExtra

                println("Gasto registrado correctamente.")
                println("Dinero disponible actualizado: Q$dineroDisponible")
            }

            2 -> {
                print("Ingrese el ingreso extra: Q")
                val ingresoExtra = readln().toDouble()

                dineroDisponible += ingresoExtra

                println("Ingreso registrado correctamente.")
                println("Dinero disponible actualizado: Q$dineroDisponible")
            }

            3 -> {
                println("Dinero disponible actualmente: Q$dineroDisponible")
            }

            4 -> {
                println("Saliendo del sistema...")
            }

            else -> {
                println("Opción no válida.")
            }
        }

    } while (opcionMenu != 4)

}