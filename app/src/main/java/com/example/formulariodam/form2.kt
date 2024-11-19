package com.example.formulariodam

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast
import java.io.ByteArrayOutputStream
import android.util.Base64
import android.util.Patterns

class form2 : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore // Inicializamos Firestore
    private val PICK_IMAGE_REQUEST = 1
    private var base64Image: String? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data

            try {

                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                val ivFotoPerfil: ImageView = findViewById(R.id.ivFotoPerfil)
                ivFotoPerfil.setImageBitmap(bitmap)
                base64Image = encodeImageToBase64(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun encodeImageToBase64(bitmap: Bitmap): String {

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form2)

        firestore = FirebaseFirestore.getInstance()

        val btnGuardar: Button = findViewById(R.id.btnGuardar)
        val etNombres: EditText = findViewById(R.id.etNombres)
        val etApellidos: EditText = findViewById(R.id.etApellidos)
        val tvFechaNacimiento: TextView = findViewById(R.id.tvFechaNacimiento)
        val autoCompleteTextViewSexo: AutoCompleteTextView = findViewById(R.id.autoCompleteTextViewSexo)
        val etEmail: EditText = findViewById(R.id.etEmail)
        val items = listOf("Masculino", "Femenino", "Otro")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)
        autoCompleteTextViewSexo.setAdapter(adapter)

        tvFechaNacimiento.setOnClickListener {
            val calendario = Calendar.getInstance()
            val anio = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val fechaSeleccionada = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                    tvFechaNacimiento.text = fechaSeleccionada
                },
                anio, mes, dia
            )
            datePickerDialog.show()
        }

        val btnSeleccionarFoto: Button = findViewById(R.id.btnSeleccionarFoto)
        val ivFotoPerfil: ImageView = findViewById(R.id.ivFotoPerfil)

        btnSeleccionarFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*" // Solo permite imágenes
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        val btnHome: Button = findViewById(R.id.btnHome)
        btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java) //
            startActivity(intent)
            finish()
        }


        btnGuardar.setOnClickListener {

            val nombres = etNombres.text.toString().trim()
            val apellidos = etApellidos.text.toString().trim()
            val fechaNacimiento = tvFechaNacimiento.text.toString()
            val sexo = autoCompleteTextViewSexo.text.toString()
            val email = etEmail.text.toString().trim()


            if (nombres.isEmpty() || apellidos.isEmpty() || fechaNacimiento == "DD/MM/AAAA" || email.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos obligatorios.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Por favor, ingresa un email válido.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (base64Image == null) {
                Toast.makeText(this, "Por favor, selecciona una foto de perfil.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val usuario = hashMapOf(
                "nombres" to nombres,
                "apellidos" to apellidos,
                "fechaNacimiento" to fechaNacimiento,
                "sexo" to sexo,
                "email" to email,
                "fotoPerfil" to base64Image
            )

            val intent = Intent(this, DatosGuardadosActivity::class.java)
            intent.putExtra("nombres", nombres)
            intent.putExtra("apellidos", apellidos)
            intent.putExtra("fechaNacimiento", fechaNacimiento)
            intent.putExtra("sexo", sexo)
            intent.putExtra("email", email)
            intent.putExtra("fotoPerfil", base64Image)

            startActivity(intent)

            firestore.collection("usuarios")
                .add(usuario)
                .addOnSuccessListener {
                    Toast.makeText(this, "Usuario registrado exitosamente.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al guardar: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
