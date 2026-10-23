package umg.edu.gt.controlgastos

import androidx.room.Entity
import androidx.room.PrimaryKey

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
