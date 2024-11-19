package com.example.formulariodam

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.EditText

class DatosGuardadosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos_guardados)

        val tvNombresValor: EditText = findViewById(R.id.tvNombresValor)
        val tvApellidosValor: EditText = findViewById(R.id.tvApellidosValor)
        val tvFechaNacimientoValor: EditText = findViewById(R.id.tvFechaNacimientoValor)
        val tvSexoValor: EditText = findViewById(R.id.tvSexoValor)
        val tvEmailValor: EditText = findViewById(R.id.tvEmailValor)
        val ivFotoPerfil: ImageView = findViewById(R.id.ivFotoPerfil)

        val btnNuevo: Button = findViewById(R.id.btnNuevo)
        val btnVerTodos: Button = findViewById(R.id.btnVerTodos)

        val nombres = intent.getStringExtra("nombres")
        val apellidos = intent.getStringExtra("apellidos")
        val fechaNacimiento = intent.getStringExtra("fechaNacimiento")
        val sexo = intent.getStringExtra("sexo")
        val email = intent.getStringExtra("email")
        val fotoBase64 = intent.getStringExtra("fotoPerfil")

        tvNombresValor.setText(nombres)
        tvApellidosValor.setText(apellidos)
        tvFechaNacimientoValor.setText(fechaNacimiento)
        tvSexoValor.setText(sexo)
        tvEmailValor.setText(email)

        if (!fotoBase64.isNullOrEmpty()) {
            val imageBytes = Base64.decode(fotoBase64, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            ivFotoPerfil.setImageBitmap(bitmap)
        }

        btnNuevo.setOnClickListener {
            val intentNuevo = Intent(this, form2::class.java)
            startActivity(intentNuevo)
            finish()
        }

        btnVerTodos.setOnClickListener {
            val intentVerTodos = Intent(this, ListaUsuariosActivity::class.java)
            startActivity(intentVerTodos)
        }
    }
}
