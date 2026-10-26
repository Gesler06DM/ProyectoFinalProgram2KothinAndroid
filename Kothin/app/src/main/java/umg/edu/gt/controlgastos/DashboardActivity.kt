package umg.edu.gt.controlgastos

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.Date
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {
    private lateinit var idCliente: String

    private var salario = 0.0
    private val movimientoDao by lazy {
        AppDatabase.obtenerBaseDeDatos(this).movimientoDao()
    }
    private var gastosEstimados = 0.0
    private var gastosNuevos = 0.0

    private var ingresosNuevos = 0.0

    private lateinit var txtSaldo: TextView
    private lateinit var txtGastos: TextView
    private lateinit var txtMovimientos: TextView

    private lateinit var txtIngresos: TextView

    private val movimientos = mutableListOf<String>()

    private val agregarIngresoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { resultado ->

        if (resultado.resultCode == Activity.RESULT_OK) {

            val datos = resultado.data
            val concepto = datos?.getStringExtra("concepto") ?: "Ingreso"
            val monto = datos?.getDoubleExtra("monto", 0.0) ?: 0.0

            if (monto > 0.0 && monto.isFinite()) {

                lifecycleScope.launch {
                    movimientoDao.insertar(
                        MovimientoEntity(
                            idCliente = idCliente,
                            tipo = "Ingreso",
                            concepto = concepto,
                            categoria = "Ingresos",
                            monto = monto,
                            fechaHora = System.currentTimeMillis()
                        )
                    )
                }

                ingresosNuevos += monto

                movimientos.add(
                    0,
                    "Ingreso: $concepto - ${formato(monto)}\nFecha: ${obtenerFechaHora()}"
                )

                guardarMovimientos()
                actualizarResumen()
            }
        }
    }

    private val agregarGastoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { resultado ->

        if (resultado.resultCode == Activity.RESULT_OK) {

            val datos = resultado.data
            val concepto = datos?.getStringExtra("concepto") ?: ""
            val monto = datos?.getDoubleExtra("monto", 0.0) ?: 0.0
            val categoria = datos?.getStringExtra("categoria") ?: ""

            if (monto > 0.0 && monto.isFinite()) {
                lifecycleScope.launch {
                    movimientoDao.insertar(
                        MovimientoEntity(
                            idCliente = idCliente,
                            tipo = "Gasto",
                            concepto = concepto,
                            categoria = categoria,
                            monto = monto,
                            fechaHora = System.currentTimeMillis()
                        )
                    )
                }

                gastosNuevos += monto

                movimientos.add(
                    0,
                    "$concepto - $categoria: ${formato(monto)}\nFecha: ${obtenerFechaHora()}"
                )

                guardarMovimientos()
                actualizarResumen()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val preferenciasUsuario = getSharedPreferences(
            "perfil_usuario",
            MODE_PRIVATE
        )

        idCliente = intent.getStringExtra("idCliente")
            ?: preferenciasUsuario.getString("idCliente", null)
                    ?: ""

        val txtBienvenida =
            findViewById<TextView>(R.id.txtBienvenida)

        txtSaldo = findViewById(R.id.txtSaldo)

        txtIngresos = findViewById(R.id.txtIngresos)

        txtGastos = findViewById(R.id.txtGastos)
        txtMovimientos = findViewById(R.id.txtMovimientos)

        val btnAgregarGasto =
            findViewById<Button>(R.id.btnAgregarGasto)
        val btnAgregarIngreso =
            findViewById<Button>(R.id.btnAgregarIngreso)
        val btnRegresar = findViewById<Button>(R.id.btnRegresar)

        btnRegresar.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
            )
            startActivity(intent)
            finish()
        }

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

        val datosGuardados = getSharedPreferences(
            "movimientos_financieros",
            MODE_PRIVATE
        )

        ingresosNuevos = savedInstanceState?.getDouble(
            "ingresosNuevos"
        ) ?: Double.fromBits(
            datosGuardados.getLong("ingresosNuevos", 0L)
        )

        gastosNuevos = savedInstanceState?.getDouble(
            "gastosNuevos"
        ) ?: Double.fromBits(
            datosGuardados.getLong("gastosNuevos", 0L)
        )

        movimientos.clear()

        val listaGuardada = savedInstanceState
            ?.getStringArrayList("movimientos")

        if (listaGuardada != null) {
            movimientos.addAll(listaGuardada)
        } else {
            val textoGuardado = datosGuardados.getString(
                "movimientos",
                "[]"
            ) ?: "[]"

            val arreglo = org.json.JSONArray(textoGuardado)

            for (i in 0 until arreglo.length()) {
                movimientos.add(arreglo.getString(i))
            }
        }

        txtBienvenida.text = "Hola, $nombre"
        txtIngresos.text = formato(salario + ingresosNuevos)

        guardarMovimientos()
        actualizarResumen()

        btnAgregarGasto.setOnClickListener {

            val intent = Intent(
                this,
                AgregarGastoActivity::class.java
            )

            agregarGastoLauncher.launch(intent)
        }
        btnAgregarIngreso.setOnClickListener {
            val intent = Intent(
                this,
                AgregarIngresoActivity::class.java
            )

            agregarIngresoLauncher.launch(intent)
        }

        val btnVerHistorial = findViewById<Button>(R.id.btnVerHistorial)

        btnVerHistorial.setOnClickListener {
            val intent = Intent(this, HistorialActivity::class.java)

            intent.putStringArrayListExtra(
                "movimientos",
                ArrayList(movimientos)
            )

            startActivity(intent)
        }

    }

    private fun guardarMovimientos() {
        val preferencias = getSharedPreferences(
            "movimientos_financieros",
            MODE_PRIVATE
        )

        preferencias.edit()
            .putLong(
                "ingresosNuevos",
                ingresosNuevos.toBits()
            )
            .putLong(
                "gastosNuevos",
                gastosNuevos.toBits()
            )
            .putString(
                "movimientos",
                org.json.JSONArray(movimientos).toString()
            )
            .apply()
    }
    override fun onResume() {
        super.onResume()

        if (::txtSaldo.isInitialized) {
            actualizarResumen()
        }
    }


    private fun actualizarResumen() {
        lifecycleScope.launch {
            val preferencias = getSharedPreferences("perfil_usuario", MODE_PRIVATE)
            val idClienteActual = preferencias.getString("idCliente", null)

            val registros = if (idClienteActual != null) {
                movimientoDao.obtenerTodos(idClienteActual)
            } else {
                emptyList<MovimientoEntity>()
            }

            val ingresosRegistrados = registros
                .filter { it.tipo.equals("Ingreso", ignoreCase = true) }
                .sumOf { it.monto }

            val gastosRegistrados = registros
                .filter { it.tipo.equals("Gasto", ignoreCase = true) }
                .sumOf { it.monto }

            val totalIngresos = salario + ingresosRegistrados
            val totalGastos = gastosEstimados + gastosRegistrados
            val saldoDisponible = totalIngresos - totalGastos

            txtIngresos.text = formato(totalIngresos)
            txtGastos.text = formato(totalGastos)
            txtSaldo.text = formato(saldoDisponible)

            val lista = mutableListOf<String>()

            if (gastosEstimados > 0.0) {
                lista.add(
                    "Gastos mensuales estimados: ${formato(gastosEstimados)}"
                )
            }

            val formatoFecha = SimpleDateFormat(
                "dd/MM/yyyy hh:mm a",
                Locale("es", "GT")
            )

            registros.forEach { movimiento ->
                val descripcion =
                    if (movimiento.tipo.equals("Ingreso", ignoreCase = true)) {
                        "Ingreso: ${movimiento.concepto}"
                    } else {
                        "${movimiento.concepto} - ${movimiento.categoria}"
                    }

                lista.add(
                    "$descripcion: ${formato(movimiento.monto)}\n" +
                            "Fecha: ${formatoFecha.format(Date(movimiento.fechaHora))}"
                )
            }

            txtMovimientos.text = if (lista.isEmpty()) {
                "Todavía no hay movimientos registrados."
            } else {
                lista.joinToString("\n")
            }
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putDouble("ingresosNuevos", ingresosNuevos)
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
    private fun obtenerFechaHora(): String {
        val formatoFecha = SimpleDateFormat(
            "dd/MM/yyyy hh:mm a",
            Locale("es", "GT")
        )
        return formatoFecha.format(Date())
    }
}
