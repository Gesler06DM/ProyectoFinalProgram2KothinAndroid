package umg.edu.gt.controlgastos

data class Movimiento(
    val id: Long,
    var tipo: String,
    var concepto: String,
    var categoria: String,
    var monto: Double,
    val fechaHora: Long
)
