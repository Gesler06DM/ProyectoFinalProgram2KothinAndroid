package umg.edu.gt.controlgastos

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val nombre = findViewById<EditText>(R.id.etNombre)
        val apellido = findViewById<EditText>(R.id.etApellido)
        val edad = findViewById<EditText>(R.id.etEdad)
        val salario = findViewById<EditText>(R.id.etSalario)
        val comenzar = findViewById<Button>(R.id.btnComenzar)

        comenzar.setOnClickListener {
            if (nombre.text.isBlank() ||
                apellido.text.isBlank() ||
                edad.text.isBlank() ||
                salario.text.isBlank()) {

                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()

            } else {
                val salarioMensual = salario.text.toString().toDoubleOrNull()

                if (salarioMensual == null || salarioMensual < 0) {
                    salario.error = "Ingresa un salario válido"
                } else {
                    val intent = Intent(this, DashboardActivity::class.java)

                    intent.putExtra("nombre", nombre.text.toString().trim())
                    intent.putExtra("salario", salarioMensual)

                    startActivity(intent)
                }
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}