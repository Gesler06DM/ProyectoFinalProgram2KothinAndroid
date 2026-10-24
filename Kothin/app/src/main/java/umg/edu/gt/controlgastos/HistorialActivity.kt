package umg.edu.gt.controlgastos

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.widget.Button
import java.util.Calendar

class HistorialActivity : AppCompatActivity() {

    private val movimientoDao by lazy {
        AppDatabase.obtenerBaseDeDatos(this).movimientoDao()
    }

    private fun obtenerRangoFechas(filtro: String): Pair<Long, Long> {
        val inicio = Calendar.getInstance()

        when (filtro) {
            "Hoy" -> {
                // Se mantiene la fecha actual
            }

            "Semana" -> {
                // La semana comienza el lunes
                inicio.firstDayOfWeek = Calendar.MONDAY
                inicio.set(
                    Calendar.DAY_OF_WEEK,
                    Calendar.MONDAY
                )
            }

            "Mes" -> {
                // Primer día del mes actual
                inicio.set(Calendar.DAY_OF_MONTH, 1)
            }
        }

        inicio.set(Calendar.HOUR_OF_DAY, 0)
        inicio.set(Calendar.MINUTE, 0)
        inicio.set(Calendar.SECOND, 0)
        inicio.set(Calendar.MILLISECOND, 0)

        val fin = Calendar.getInstance().timeInMillis

        return Pair(inicio.timeInMillis, fin)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)
        val btnTodos = findViewById<Button>(R.id.btnTodos)
        val btnHoy = findViewById<Button>(R.id.btnHoy)
        val btnSemana = findViewById<Button>(R.id.btnSemana)
        val btnMes = findViewById<Button>(R.id.btnMes)
        var filtroActual = "Todos"

        val contenedor = findViewById<LinearLayout>(
            R.id.contenedorMovimientos
        )

        fun cargarMovimientos() {
            lifecycleScope.launch {
                contenedor.removeAllViews()
                val preferencias = getSharedPreferences(
                    "perfil_usuario",
                    MODE_PRIVATE
                )

                val idCliente = preferencias.getString("idCliente", null)


                val movimientos = if (idCliente != null) {
                    if (filtroActual == "Todos") {
                        movimientoDao.obtenerTodos(idCliente)
                    } else {
                        val (inicio, fin) = obtenerRangoFechas(filtroActual)

                        movimientoDao.obtenerPorFechas(
                            idCliente,
                            inicio,
                            fin
                        )
                    }
                } else {
                    emptyList<MovimientoEntity>()
                }
                val totalIngresos = movimientos
                    .filter { it.tipo.equals("Ingreso", ignoreCase = true) }
                    .sumOf { it.monto }

                val totalGastos = movimientos
                    .filter { it.tipo.equals("Gasto", ignoreCase = true) }
                    .sumOf { it.monto }

                val balancePeriodo = totalIngresos - totalGastos

                val resumen = TextView(this@HistorialActivity)

                resumen.text = """
    Resumen: $filtroActual

    Ingresos: Q ${String.format(java.util.Locale.US, "%.2f", totalIngresos)}
    Gastos: Q ${String.format(java.util.Locale.US, "%.2f", totalGastos)}
    Balance: Q ${String.format(java.util.Locale.US, "%.2f", balancePeriodo)}
""".trimIndent()

                resumen.textSize = 17f
                resumen.setTextColor(
                    android.graphics.Color.parseColor("#333333")
                )
                resumen.setPadding(0, 20, 0, 30)

                contenedor.addView(resumen)

                if (movimientos.isNullOrEmpty()) {

                    val mensaje = TextView(this@HistorialActivity)
                    mensaje.text = "Todavía no hay movimientos registrados."
                    mensaje.textSize = 16f


                    contenedor.addView(mensaje)

                } else {

                    movimientos.forEach { movimiento ->

                        val texto = TextView(this@HistorialActivity)

                        texto.text = "${movimiento.tipo}: ${movimiento.concepto} - Q ${
                            String.format(
                                java.util.Locale.US,
                                "%.2f",
                                movimiento.monto
                            )
                        }"
                        val formatoFecha = java.text.SimpleDateFormat(
                            "dd/MM/yyyy hh:mm a",
                            java.util.Locale("es", "GT")
                        )

                        texto.text = texto.text.toString() +
                                "\nFecha: ${formatoFecha.format(java.util.Date(movimiento.fechaHora))}"
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

                            val contenedorEdicion =
                                android.widget.LinearLayout(this@HistorialActivity)
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

                                                texto.text =
                                                    "${movimientoEditado.tipo}: ${movimientoEditado.concepto} - Q ${
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

        cargarMovimientos()

        btnTodos.setOnClickListener {
            filtroActual = "Todos"
            contenedor.removeAllViews()
            cargarMovimientos()
        }

        btnHoy.setOnClickListener {
            filtroActual = "Hoy"
            contenedor.removeAllViews()
            cargarMovimientos()
        }

        btnSemana.setOnClickListener {
            filtroActual = "Semana"
            contenedor.removeAllViews()
            cargarMovimientos()
        }

        btnMes.setOnClickListener {
            filtroActual = "Mes"
            contenedor.removeAllViews()
            cargarMovimientos()
        }

    }
}
