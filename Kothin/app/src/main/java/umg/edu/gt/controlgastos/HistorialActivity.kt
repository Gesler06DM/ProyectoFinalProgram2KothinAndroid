package umg.edu.gt.controlgastos

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout

class HistorialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        val contenedor = findViewById<LinearLayout>(
            R.id.contenedorMovimientos
        )

        val movimientos = intent.getStringArrayListExtra("movimientos")
            ?: arrayListOf()

        if (movimientos.isNullOrEmpty()) {

            val mensaje = TextView(this)
            mensaje.text = "Todavía no hay movimientos registrados."
            mensaje.textSize = 16f


            contenedor.addView(mensaje)

        } else {

            movimientos.forEach { movimiento ->

                val texto = TextView(this)

                texto.text = movimiento
                texto.textSize = 16f
                texto.setTextColor(
                    android.graphics.Color.parseColor("#333333")
                )

                texto.setPadding(0, 16, 0, 24)

                texto.setOnClickListener {

                    val campoConcepto = android.widget.EditText(this)
                    campoConcepto.setText(texto.text.toString().substringBefore("\n"))
                    val campoMonto = android.widget.EditText(this)
                    campoMonto.hint = "Nuevo monto en quetzales"
                    campoMonto.inputType =
                        android.text.InputType.TYPE_CLASS_NUMBER or
                                android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL

                    val contenedorEdicion = android.widget.LinearLayout(this)
                    contenedorEdicion.orientation =
                        android.widget.LinearLayout.VERTICAL

                    contenedorEdicion.setPadding(40, 10, 40, 10)
                    contenedorEdicion.addView(campoConcepto)
                    contenedorEdicion.addView(campoMonto)

                    androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Editar movimiento")
                        .setMessage("Modifica la descripción del movimiento:")
                        .setView(contenedorEdicion)

                        .setPositiveButton("Guardar") { _, _ ->
                            val nuevoTexto = campoConcepto.text.toString().trim()
                            val montoIngresado = campoMonto.text.toString().trim()

                            val preferencias = getSharedPreferences(
                                "movimientos_financieros",
                                MODE_PRIVATE
                            )

                            val indice = movimientos.indexOf(texto.text.toString())

                            if (indice != -1 && nuevoTexto.isNotEmpty()) {
                                val primeraLinea = movimiento.substringBefore("\n")
                                val fechaOriginal = movimiento.substringAfter("\n", "")

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
                                        this,
                                        "Ingresa un monto válido",
                                        android.widget.Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    val diferencia = nuevoMonto - montoAnterior
                                    val esIngreso = primeraLinea.startsWith("Ingreso:")

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
                                    val lineaFinal = if (patronMonto.containsMatchIn(nuevoTexto)) {
                                        lineaActualizada
                                    } else {
                                        "$nuevoTexto - Q ${
                                            String.format(java.util.Locale.US, "%.2f", nuevoMonto)
                                        }"
                                    }

                                    val movimientoActualizado =
                                        if (fechaOriginal.isNotEmpty()) {
                                            "$lineaFinal\n$fechaOriginal"
                                        } else {
                                            lineaFinal
                                        }

                                    movimientos[indice] = movimientoActualizado
                                    texto.text = movimientoActualizado

                                    val clave = if (esIngreso) {
                                        "ingresosNuevos"
                                    } else {
                                        "gastosNuevos"
                                    }

                                    val totalAnterior = Double.fromBits(
                                        preferencias.getLong(clave, 0L)
                                    )

                                    preferencias.edit()
                                        .putLong(
                                            clave,
                                            (totalAnterior + diferencia).toBits()
                                        )
                                        .putString(
                                            "movimientos",
                                            org.json.JSONArray(movimientos).toString()
                                        )
                                        .apply()
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
