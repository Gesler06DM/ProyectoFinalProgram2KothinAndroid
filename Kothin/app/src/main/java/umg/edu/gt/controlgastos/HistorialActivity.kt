package umg.edu.gt.controlgastos

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HistorialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        val txtHistorial = findViewById<TextView>(R.id.txtHistorial)

        val movimientos = intent.getStringArrayListExtra("movimientos")

        txtHistorial.text = if (movimientos.isNullOrEmpty()) {
            "Todavía no hay movimientos registrados."
        } else {
            movimientos.joinToString("\n\n")
        }
    }
}
