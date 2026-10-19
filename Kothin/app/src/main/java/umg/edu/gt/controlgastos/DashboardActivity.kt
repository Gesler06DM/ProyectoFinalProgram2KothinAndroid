package umg.edu.gt.controlgastos

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val txtBienvenida = findViewById<TextView>(R.id.txtBienvenida)
        val txtSaldo = findViewById<TextView>(R.id.txtSaldo)
        val txtIngresos = findViewById<TextView>(R.id.txtIngresos)
        val txtGastos = findViewById<TextView>(R.id.txtGastos)

        val nombre = intent.getStringExtra("nombre") ?: "Usuario"
        val salario = intent.getDoubleExtra("salario", 0.0)

        txtBienvenida.text = "Hola, $nombre"
        txtSaldo.text = "Q %.2f".format(salario)
        txtIngresos.text = "Q %.2f".format(salario)
        txtGastos.text = "Q 0.00"
    }
}

