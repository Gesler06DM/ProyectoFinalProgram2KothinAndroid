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
            val movimientos = movimientoDao.obtenerTodos()

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
                        campoConcepto.setText(texto.text.toString().substringBefore("\n"))
                        val campoMonto = android.widget.EditText(this@HistorialActivity)
                        campoMonto.hint = "Nuevo monto en quetzales"
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
                                    val primeraLinea = "${movimiento.tipo}: ${movimiento.concepto} - Q ${movimiento.monto}"

                                    val patronMonto = Regex("""Q\s*(\d+(?:\.\d+)?)""")
                                    val montoAnterior = patronMonto.find(primeraLinea)
                                        ?.groupValues?.get(1)?.toDoubleOrNull()

                                    val nuevoMonto = if (montoIngresado.isBlank()) {
                                        montoAnterior
                                    } else {
                                        montoIngresado.toDoubleOrNull()
                                    }

                                    if (montoAnterior == null ||
                                        nuevoMonto == null ||
                                        !nuevoMonto.isFinite() ||
                                        nuevoMonto <= 0.0
                                    ) {
                                        android.widget.Toast.makeText(
                                            this@HistorialActivity,
                                            "Ingresa un monto válido",
                                            android.widget.Toast.LENGTH_SHORT
                                        ).show()
                                    } else {

                                        val lineaActualizada = patronMonto.replaceFirst(
                                            nuevoTexto,
                                            String.format(
                                                java.util.Locale.US,
                                                "Q %.2f",
                                                nuevoMonto
                                            )
                                        )

                                        // Si el texto editado ya no contiene el monto,
                                        // se agrega al final.
                                        val lineaFinal =
                                            if (patronMonto.containsMatchIn(nuevoTexto)) {
                                                lineaActualizada
                                            } else {
                                                "$nuevoTexto - Q ${
                                                    String.format(
                                                        java.util.Locale.US,
                                                        "%.2f",
                                                        nuevoMonto
                                                    )
                                                }"
                                            }

                                        val movimientoActualizado = lineaFinal
                                        texto.text = movimientoActualizado

                                        val movimientoEditado = movimiento.copy(
                                            concepto = nuevoTexto,
                                            monto = nuevoMonto
                                        )

                                        lifecycleScope.launch {
                                            movimientoDao.actualizar(movimientoEditado)

                                            texto.text = movimientoEditado.toString()

                                            android.widget.Toast.makeText(
                                                this@HistorialActivity,
                                                "Movimiento actualizado correctamente",
                                                android.widget.Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            }

                            .show()
                    }

                    contenedor.addView(texto)
                }
            }
        }
    }
}
