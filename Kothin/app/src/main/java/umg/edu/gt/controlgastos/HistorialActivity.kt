package umg.edu.gt.controlgastos

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class HistorialActivity : AppCompatActivity() {

    private val movimientoDao by lazy {
        AppDatabase.obtenerBaseDeDatos(this).movimientoDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        val contenedor = findViewById<LinearLayout>(
            R.id.contenedorMovimientos
        )

        lifecycleScope.launch {
            val preferencias = getSharedPreferences(
                "perfil_usuario",
                MODE_PRIVATE
            )

            val idCliente = preferencias.getString("idCliente", null)

            val movimientos = if (idCliente != null) {
                movimientoDao.obtenerTodos(idCliente)
            } else {
                emptyList<MovimientoEntity>()
            }

            if (movimientos.isNullOrEmpty()) {

                val mensaje = TextView(this@HistorialActivity)
                mensaje.text = "Todavía no hay movimientos registrados."
                mensaje.textSize = 16f


                contenedor.addView(mensaje)

            } else {

                movimientos.forEach { movimiento ->

                    val texto = TextView(this@HistorialActivity)

                    texto.text = "${movimiento.tipo}: ${movimiento.concepto} - Q ${String.format(java.util.Locale.US, "%.2f", movimiento.monto)}"
                    texto.textSize = 16f
                    texto.setTextColor(
                        android.graphics.Color.parseColor("#333333")
                    )

                    texto.setPadding(0, 16, 0, 24)

                    texto.setOnClickListener {

                        val campoConcepto = android.widget.EditText(this@HistorialActivity)
                        campoConcepto.setText(movimiento.concepto)
                        val campoMonto = android.widget.EditText(this@HistorialActivity)
                        campoMonto.hint = "Nuevo monto en quetzales"
                        campoMonto.setText(movimiento.monto.toString())
                        campoMonto.inputType =
                            android.text.InputType.TYPE_CLASS_NUMBER or
                                    android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL

                        val contenedorEdicion = android.widget.LinearLayout(this@HistorialActivity)
                        contenedorEdicion.orientation =
                            android.widget.LinearLayout.VERTICAL

                        contenedorEdicion.setPadding(40, 10, 40, 10)
                        contenedorEdicion.addView(campoConcepto)
                        contenedorEdicion.addView(campoMonto)

                        androidx.appcompat.app.AlertDialog.Builder(this@HistorialActivity)
                            .setTitle("Editar movimiento")
                            .setMessage("Modifica la descripción del movimiento:")
                            .setView(contenedorEdicion)

                            .setPositiveButton("Guardar") { _, _ ->
                                val nuevoTexto = campoConcepto.text.toString().trim()
                                val montoIngresado = campoMonto.text.toString().trim()

                                if (nuevoTexto.isNotEmpty()) {
                                    val nuevoMonto = if (montoIngresado.isBlank()) {
                                        movimiento.monto
                                    } else {
                                        montoIngresado.toDoubleOrNull()
                                    }

                                    if (nuevoMonto == null ||
                                        !nuevoMonto.isFinite() ||
                                        nuevoMonto <= 0.0
                                    ) {
                                        android.widget.Toast.makeText(
                                            this@HistorialActivity,
                                            "Ingresa un monto válido",
                                            android.widget.Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        val movimientoEditado = movimiento.copy(
                                            concepto = nuevoTexto,
                                            monto = nuevoMonto
                                        )

                                        lifecycleScope.launch {
                                            movimientoDao.actualizar(movimientoEditado)

                                            texto.text = "${movimientoEditado.tipo}: ${movimientoEditado.concepto} - Q ${
                                                String.format(
                                                    java.util.Locale.US,
                                                    "%.2f",
                                                    movimientoEditado.monto
                                                )
                                            }"

                                            android.widget.Toast.makeText(
                                                this@HistorialActivity,
                                                "Movimiento actualizado correctamente",
                                                android.widget.Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            }
                            .setNeutralButton("Eliminar") { _, _ ->

                                androidx.appcompat.app.AlertDialog.Builder(this@HistorialActivity)
                                    .setTitle("Eliminar movimiento")
                                    .setMessage("¿Estás seguro de que deseas eliminar este movimiento?")
                                    .setPositiveButton("Sí, eliminar") { _, _ ->

                                        lifecycleScope.launch {
                                            movimientoDao.eliminar(movimiento)

                                            texto.visibility = android.view.View.GONE

                                            android.widget.Toast.makeText(
                                                this@HistorialActivity,
                                                "Movimiento eliminado correctamente",
                                                android.widget.Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                    .setNegativeButton("Cancelar", null)
                                    .show()
                            }
                            .show()
                    }

                    contenedor.addView(texto)
                }
            }
        }
    }
}
