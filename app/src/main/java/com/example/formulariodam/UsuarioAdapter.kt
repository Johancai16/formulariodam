package com.example.formulariodam

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class UsuarioAdapter(private val usuarios: List<Map<String, Any>>) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_usuario, parent, false)
        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = usuarios[position]

        holder.tvNombre.text = "${usuario["nombres"]} ${usuario["apellidos"]}"
        holder.tvEdad.text = calcularEdad(usuario["fechaNacimiento"] as String).toString()
        holder.tvSexo.text = usuario["sexo"] as String
        holder.tvCorreo.text = usuario["email"] as String
    }

    override fun getItemCount(): Int {
        return usuarios.size
    }

    inner class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvEdad: TextView = itemView.findViewById(R.id.tvEdad)
        val tvSexo: TextView = itemView.findViewById(R.id.tvSexo)
        val tvCorreo: TextView = itemView.findViewById(R.id.tvCorreo)
    }

    private fun calcularEdad(fechaNacimiento: String): Int {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fechaNac = sdf.parse(fechaNacimiento)
        val calendar = Calendar.getInstance()
        calendar.time = fechaNac
        val birthYear = calendar.get(Calendar.YEAR)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return currentYear - birthYear
    }
}
