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

    @Query("""
        SELECT * FROM movimientos
        WHERE idCliente = :idCliente
        ORDER BY fechaHora DESC, id DESC
    """)
    suspend fun obtenerTodos(
        idCliente: String
    ): List<MovimientoEntity>

    @Query("""
        SELECT * FROM movimientos
        WHERE id = :id AND idCliente = :idCliente
        LIMIT 1
    """)
    suspend fun obtenerPorId(
        id: Long,
        idCliente: String
    ): MovimientoEntity?

    @Query("""
        SELECT * FROM movimientos
        WHERE idCliente = :idCliente AND tipo = :tipo
        ORDER BY fechaHora DESC, id DESC
    """)
    suspend fun obtenerPorTipo(
        idCliente: String,
        tipo: String
    ): List<MovimientoEntity>

    @Query("""
        SELECT * FROM movimientos
        WHERE idCliente = :idCliente
        AND fechaHora BETWEEN :inicio AND :fin
        ORDER BY fechaHora DESC, id DESC
    """)
    suspend fun obtenerPorFechas(
        idCliente: String,
        inicio: Long,
        fin: Long
    ): List<MovimientoEntity>
}

