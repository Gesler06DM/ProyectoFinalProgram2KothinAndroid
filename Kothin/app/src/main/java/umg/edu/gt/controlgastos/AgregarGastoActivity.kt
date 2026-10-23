package umg.edu.gt.controlgastos

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AgregarGastoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_gasto)

        val concepto = findViewById<EditText>(R.id.etConcepto)
        val monto = findViewById<EditText>(R.id.etMonto)
        val categoria = findViewById<Spinner>(R.id.spCategoria)
        val guardar = findViewById<Button>(R.id.btnGuardarGasto)

        val categorias = listOf(
            "Alimentación",
            "Transporte",
            "Servicios",
            "Educación",
            "Salud",
            "Otros"
        )

        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categorias
        )

        adaptador.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        categoria.adapter = adaptador

        guardar.setOnClickListener {

            val descripcion = concepto.text.toString().trim()
            val cantidad = monto.text.toString()
                .toDoubleOrNull()

            if (descripcion.isBlank()) {
                concepto.error = "Ingresa una descripción"
                return@setOnClickListener
            }

            if (!descripcion.any { it.isLetter() }) {
                concepto.error = "El concepto debe contener letras"
                return@setOnClickListener
            }

            if (cantidad == null || !cantidad.isFinite() || cantidad <= 0.0) {
                monto.error = "Ingresa un monto válido"
                return@setOnClickListener
            }

            val resultado = Intent().apply {
                putExtra("concepto", descripcion)
                putExtra("monto", cantidad)
                putExtra(
                    "categoria",
                    categoria.selectedItem.toString()
                )
            }

            setResult(Activity.RESULT_OK, resultado)

            Toast.makeText(
                this,
                "Gasto registrado correctamente",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }
}
