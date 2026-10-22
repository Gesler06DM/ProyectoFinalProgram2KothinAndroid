package umg.edu.gt.controlgastos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query

@Dao
interface MovimientoDao {

    @Insert
    suspend fun insertar(movimiento: MovimientoEntity): Long

    @Update
    suspend fun actualizar(movimiento: MovimientoEntity)

    @Delete
    suspend fun eliminar(movimiento: MovimientoEntity)

    @Query("SELECT * FROM movimientos ORDER BY fechaHora DESC, id DESC")
    suspend fun obtenerTodos(): List<MovimientoEntity>

    @Query("SELECT * FROM movimientos WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Long): MovimientoEntity?

    @Query("SELECT * FROM movimientos WHERE tipo = :tipo ORDER BY fechaHora DESC")
    suspend fun obtenerPorTipo(tipo: String): List<MovimientoEntity>

    @Query("SELECT * FROM movimientos WHERE fechaHora BETWEEN :inicio AND :fin ORDER BY fechaHora DESC")
    suspend fun obtenerPorFechas(
        inicio: Long,
        fin: Long
    ): List<MovimientoEntity>
}
