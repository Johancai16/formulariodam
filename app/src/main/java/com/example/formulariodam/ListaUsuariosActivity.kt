package com.example.formulariodam

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ListaUsuariosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var usuarioAdapter: UsuarioAdapter
    private lateinit var usuariosList: MutableList<Map<String, Any>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_usuarios)

        recyclerView = findViewById(R.id.recyclerViewUsuarios)
        recyclerView.layoutManager = LinearLayoutManager(this)

        usuariosList = mutableListOf()

        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("usuarios")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val usuario = document.data
                    usuariosList.add(usuario)
                }
                usuarioAdapter = UsuarioAdapter(usuariosList)
                recyclerView.adapter = usuarioAdapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar los datos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        val btnNuevo: Button = findViewById(R.id.btnNuevo)
        btnNuevo.setOnClickListener {
            val intentNuevo = Intent(this, form2::class.java) // Va al formulario
            startActivity(intentNuevo)
        }
        val btnHome: Button = findViewById(R.id.btnHome)
        btnHome.setOnClickListener {
            val intentHome = Intent(this, MainActivity::class.java)
            startActivity(intentHome)
            finish()
        }
    }
}
