package org.example

import java.time.LocalDateTime

class Gasto(
    var descripcion: String,
    var categoria: String,
    var monto: Double,
    var fecha: LocalDateTime = LocalDateTime.now()
)