package umg.edu.gt.controlgastos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kogin)

        val btnIngresar = findViewById<Button>(R.id.btnIngresar)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        btnIngresar.setOnClickListener {
            val preferencias = getSharedPreferences(
                "perfil_usuario",
                MODE_PRIVATE
            )

            val idCliente = preferencias.getString("idCliente", null)

            if (idCliente != null) {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "Primero debes crear una cuenta",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnRegistrar.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }
}
