package org.example

import java.time.LocalDateTime

class Ingreso(
    var descripcion: String,
    var monto: Double,
    var fecha: LocalDateTime = LocalDateTime.now()
)