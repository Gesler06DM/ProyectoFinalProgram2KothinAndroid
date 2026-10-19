package umg.edu.gt.controlgastos

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class RegistroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val nombre = findViewById<EditText>(R.id.etNombreRegistro)
        val apellido = findViewById<EditText>(R.id.etApellidoRegistro)
        val edad = findViewById<EditText>(R.id.etEdadRegistro)
        val actividad = findViewById<Spinner>(R.id.spActividad)
        val salario = findViewById<EditText>(R.id.etSalarioRegistro)

        val alimentacion = findViewById<EditText>(R.id.etAlimentacion)
        val transporte = findViewById<EditText>(R.id.etTransporte)
        val estudios = findViewById<EditText>(R.id.etEstudios)
        val servicios = findViewById<EditText>(R.id.etServicios)
        val otros = findViewById<EditText>(R.id.etOtros)

        val fecha = findViewById<TextView>(R.id.txtFechaRegistro)
        val crearCuenta = findViewById<Button>(R.id.btnCrearCuenta)

        val opciones = listOf(
            "Selecciona tu actividad",
            "Estudiante",
            "Trabajador",
            "Estudia y trabaja",
            "Otra"
        )

        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            opciones
        )

        adaptador.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        actividad.adapter = adaptador

        val formato = SimpleDateFormat(
            "dd/MM/yyyy HH:mm:ss",
            Locale.getDefault()
        )

        fecha.text = "Fecha y hora: ${formato.format(Date())}"

        crearCuenta.setOnClickListener {

            val nombreTexto = nombre.text.toString().trim()
            val apellidoTexto = apellido.text.toString().trim()
            val edadNumero = edad.text.toString().toIntOrNull()
            val salarioNumero = salario.text.toString().toDoubleOrNull()

            if (nombreTexto.isEmpty() ||
                apellidoTexto.isEmpty() ||
                edadNumero == null ||
                edadNumero <= 0 ||
                actividad.selectedItemPosition == 0 ||
                salarioNumero == null ||
                salarioNumero < 0
            ) {
                Toast.makeText(
                    this,
                    "Completa correctamente tus datos",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                fun leerGasto(campo: EditText): Double? {
                    val texto = campo.text.toString().trim()
                    return if (texto.isEmpty()) 0.0
                    else texto.toDoubleOrNull()
                }

                val gastos = listOf(
                    leerGasto(alimentacion),
                    leerGasto(transporte),
                    leerGasto(estudios),
                    leerGasto(servicios),
                    leerGasto(otros)
                )

                if (gastos.any { it == null || !it.isFinite() || it < 0 } ||
                    !salarioNumero.isFinite()
                ) {
                    Toast.makeText(
                        this,
                        "Ingresa montos válidos y no negativos",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    val preferencias = getSharedPreferences(
                        "perfil_usuario",
                        MODE_PRIVATE
                    )
                    val idCliente = UUID.randomUUID().toString()
                    val totalGastos = gastos.sumOf { it ?: 0.0 }
                    preferencias.edit()
                        .putString("idCliente", idCliente)
                        .putString("nombre", nombreTexto)
                        .putString("apellido", apellidoTexto)
                        .putInt("edad", edadNumero)
                        .putString(
                            "actividad",
                            actividad.selectedItem.toString()
                        )
                        .putFloat("salario", salarioNumero.toFloat())
                        .putString(
                            "fechaRegistro",
                            formato.format(Date())
                        )
                        .putString(
                            "gastosEstimados",
                            totalGastos.toString()
                        )
                        .apply()

                    val intent = Intent(
                        this,
                        DashboardActivity::class.java
                    )

                    intent.putExtra("idCliente", idCliente)
                    intent.putExtra("nombre", nombreTexto)
                    intent.putExtra("salario", salarioNumero)
                    intent.putExtra("gastosEstimados", totalGastos)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}
