package org.example

fun main() {

    println("=================================")
    println("     CONTROL DE GASTOS")
    println("=================================")

    print("Ingrese su nombre: ")
    val nombre = readln()

    print("Ingrese su salario mensual: Q")
    val salario = readln().toDouble()

    println()
    println("Bienvenido/a, $nombre")
    println("Su ingreso mensual es: Q$salario")
    println()
    println("¿Qué actividades realiza?")
    println("1. Estudia")
    println("2. Trabaja")
    println("3. Estudia y trabaja")
    println("4. Otra actividad")

    print("Seleccione una opción: ")
    val opcionActividad = readln().toInt()

    when (opcionActividad) {
        1 -> println("Actividad seleccionada: Estudia")
        2 -> println("Actividad seleccionada: Trabaja")
        3 -> println("Actividad seleccionada: Estudia y trabaja")
        4 -> {
            print("Ingrese su actividad: ")
            val otraActividad = readln()
            println("Actividad seleccionada: $otraActividad")
        }
        else -> println("Opción no válida")
    }
}