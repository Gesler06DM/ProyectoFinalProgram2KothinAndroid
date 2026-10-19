
package umg.edu.gt.controlgastos

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class DashboardActivity : AppCompatActivity() {

    private var salario = 0.0
    private var gastosEstimados = 0.0
    private var gastosNuevos = 0.0

    private lateinit var txtSaldo: TextView
    private lateinit var txtGastos: TextView
    private lateinit var txtMovimientos: TextView

    private val movimientos = mutableListOf<String>()

    private val agregarGastoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { resultado ->

        if (resultado.resultCode == Activity.RESULT_OK) {

            val datos = resultado.data
            val concepto = datos?.getStringExtra("concepto") ?: ""
            val monto = datos?.getDoubleExtra("monto", 0.0) ?: 0.0
            val categoria = datos?.getStringExtra("categoria") ?: ""

            if (monto > 0.0 && monto.isFinite()) {

                gastosNuevos += monto

                movimientos.add(
                    0,
                    "$concepto - $categoria: ${formato(monto)}"
                )

                actualizarResumen()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val txtBienvenida =
            findViewById<TextView>(R.id.txtBienvenida)

        txtSaldo = findViewById(R.id.txtSaldo)

        val txtIngresos =
            findViewById<TextView>(R.id.txtIngresos)

        txtGastos = findViewById(R.id.txtGastos)
        txtMovimientos = findViewById(R.id.txtMovimientos)

        val btnAgregarGasto =
            findViewById<Button>(R.id.btnAgregarGasto)

        val preferencias = getSharedPreferences(
            "perfil_usuario",
            MODE_PRIVATE
        )

        val nombre = intent.getStringExtra("nombre")
            ?: preferencias.getString("nombre", "Usuario")
            ?: "Usuario"

        salario = if (intent.hasExtra("salario")) {
            intent.getDoubleExtra("salario", 0.0)
        } else {
            preferencias.getFloat("salario", 0f).toDouble()
        }

        gastosEstimados = intent.getDoubleExtra(
            "gastosEstimados",
            preferencias.getString("gastosEstimados", "0")
                ?.toDoubleOrNull() ?: 0.0
        )

        gastosNuevos = savedInstanceState
            ?.getDouble("gastosNuevos") ?: 0.0

        val listaGuardada = savedInstanceState
            ?.getStringArrayList("movimientos")

        if (listaGuardada != null) {
            movimientos.addAll(listaGuardada)
        }

        txtBienvenida.text = "Hola, $nombre"
        txtIngresos.text = formato(salario)

        actualizarResumen()

        btnAgregarGasto.setOnClickListener {

            val intent = Intent(
                this,
                AgregarGastoActivity::class.java
            )

            agregarGastoLauncher.launch(intent)
        }
    }

    private fun actualizarResumen() {

        val totalGastos = gastosEstimados + gastosNuevos
        val saldoDisponible = salario - totalGastos

        txtSaldo.text = formato(saldoDisponible)
        txtGastos.text = formato(totalGastos)

        val lista = mutableListOf<String>()

        if (gastosEstimados > 0.0) {
            lista.add(
                "Gastos mensuales estimados: ${formato(gastosEstimados)}"
            )
        }

        lista.addAll(movimientos)

        txtMovimientos.text =
            if (lista.isEmpty()) {
                "Todavía no hay movimientos registrados."
            } else {
                lista.joinToString("\n")
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putDouble("gastosNuevos", gastosNuevos)
        outState.putStringArrayList(
            "movimientos",
            ArrayList(movimientos)
        )
        super.onSaveInstanceState(outState)
    }

    private fun formato(cantidad: Double): String {
        return String.format(
            Locale.US,
            "Q %.2f",
            cantidad
        )
    }
}
