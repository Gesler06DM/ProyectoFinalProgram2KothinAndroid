package umg.edu.gt.controlgastos

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AgregarIngresoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_ingreso)

        val concepto = findViewById<EditText>(
            R.id.etConceptoIngreso
        )

        val monto = findViewById<EditText>(
            R.id.etMontoIngreso
        )

        val guardar = findViewById<Button>(
            R.id.btnGuardarIngreso
        )

        guardar.setOnClickListener {

            val conceptoTexto =
                concepto.text.toString().trim()

            val montoNumero =
                monto.text.toString().toDoubleOrNull()

            if (conceptoTexto.isEmpty() ||
                montoNumero == null ||
                !montoNumero.isFinite() ||
                montoNumero <= 0.0
            ) {
                Toast.makeText(
                    this,
                    "Ingresa un concepto y monto válido",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                val resultado = Intent()

                resultado.putExtra(
                    "concepto",
                    conceptoTexto
                )

                resultado.putExtra(
                    "monto",
                    montoNumero
                )

                setResult(Activity.RESULT_OK, resultado)
                finish()
            }
        }
    }
}
