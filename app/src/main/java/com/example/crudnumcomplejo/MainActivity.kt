package com.example.crudnumcomplejo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.crudnumcomplejo.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var baseDatos: BaseDatosComplejos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(barras.left, barras.top, barras.right, barras.bottom)
            insets
        }

        baseDatos = Room.databaseBuilder(
            applicationContext,
            BaseDatosComplejos::class.java,
            "dbComplejos"
        ).build()

        binding.buttonAgregar.setOnClickListener { agregarNumero() }
        binding.buttonListar.setOnClickListener { listarNumeros() }
        binding.buttonActualizar.setOnClickListener { actualizarNumero() }
        binding.buttonBorrar.setOnClickListener { borrarNumero() }

        listarNumeros()
    }

    private fun agregarNumero() {
        val numero = leerNumero() ?: return

        lifecycleScope.launch {
            baseDatos.daoNumeroComplejo().insertarNumero(numero)
            binding.textViewMensaje.text = "Número guardado."
            listarNumeros()
        }
    }

    private fun actualizarNumero() {
        val numero = leerNumero() ?: return

        if (numero.id == 0) {
            binding.textViewMensaje.text = "Para actualizar escribe un ID mayor que 0."
            return
        }

        lifecycleScope.launch {
            baseDatos.daoNumeroComplejo().actualizarNumero(
                numero.id,
                numero.pr,
                numero.pi,
                numero.pr_r,
                numero.pr_i
            )
            binding.textViewMensaje.text = "Número actualizado."
            listarNumeros()
        }
    }

    private fun borrarNumero() {
        val id = binding.editTextId.text.toString().toIntOrNull()

        if (id == null || id == 0) {
            binding.textViewMensaje.text = "Escribe el ID del número que quieres borrar."
            return
        }

        lifecycleScope.launch {
            baseDatos.daoNumeroComplejo().eliminarNumero(id)
            binding.textViewMensaje.text = "Número borrado."
            listarNumeros()
        }
    }

    private fun leerNumero(): NumeroComplejo? {
        val id = binding.editTextId.text.toString().toIntOrNull() ?: 0
        val pr = binding.editTextParteReal.text.toString().toDoubleOrNull()
        val pi = binding.editTextParteImaginaria.text.toString().toDoubleOrNull()

        if (pr == null || pi == null) {
            binding.textViewMensaje.text = "Escribe la parte real y la parte imaginaria."
            return null
        }

        // Reflejo respecto al eje real: la parte real se conserva y pi cambia de signo.
        return NumeroComplejo(
            id = id,
            pr = pr,
            pi = pi,
            pr_r = pr,
            pr_i = -pi
        )
    }

    private fun listarNumeros() {
        lifecycleScope.launch {
            val numeros = baseDatos.daoNumeroComplejo().obtenerNumeros()
            llenarTabla(numeros)

            if (numeros.isEmpty()) {
                binding.textViewMensaje.text = getString(R.string.empty_table)
            }
        }
    }

    private fun llenarTabla(numeros: List<NumeroComplejo>) {
        binding.tableLayoutComplejos.removeAllViews()
        agregarFilaEncabezado()

        for (numero in numeros) {
            val fila = TableRow(this)
            fila.setPadding(0, 4, 0, 4)

            val id = crearCelda(numero.id.toString())
            val parteReal = crearCelda(numero.pr.toString())
            val parteImaginaria = crearCelda(numero.pi.toString())
            val prReflejada = crearCelda(numero.pr_r.toString())
            val piReflejada = crearCelda(numero.pr_i.toString())

            // Tocar el ID, pr o pi abre la pantalla de gráfica.
            val abrirGrafica = View.OnClickListener { abrirGrafica(numero) }
            id.setOnClickListener(abrirGrafica)
            parteReal.setOnClickListener(abrirGrafica)
            parteImaginaria.setOnClickListener(abrirGrafica)

            fila.addView(id)
            fila.addView(parteReal)
            fila.addView(parteImaginaria)
            fila.addView(prReflejada)
            fila.addView(piReflejada)
            binding.tableLayoutComplejos.addView(fila)
        }
    }

    private fun agregarFilaEncabezado() {
        val fila = TableRow(this)
        fila.setBackgroundColor(Color.rgb(103, 80, 164))

        fila.addView(crearCelda("ID", true))
        fila.addView(crearCelda("pr", true))
        fila.addView(crearCelda("pi", true))
        fila.addView(crearCelda("pr_r", true))
        fila.addView(crearCelda("pr_i", true))

        binding.tableLayoutComplejos.addView(fila)
    }

    private fun crearCelda(texto: String, encabezado: Boolean = false): TextView {
        return TextView(this).apply {
            text = texto
            setPadding(16, 12, 16, 12)
            gravity = Gravity.CENTER
            textSize = 15f
            minWidth = 110

            if (encabezado) {
                setTextColor(Color.WHITE)
                setTypeface(typeface, android.graphics.Typeface.BOLD)
            } else {
                setTextColor(Color.rgb(32, 33, 36))
                setBackgroundColor(Color.WHITE)
            }
        }
    }

    private fun abrirGrafica(numero: NumeroComplejo) {
        val intent = Intent(this, GraficaActivity::class.java).apply {
            putExtra("id", numero.id)
            putExtra("pr", numero.pr)
            putExtra("pi", numero.pi)
            putExtra("pr_r", numero.pr_r)
            putExtra("pr_i", numero.pr_i)
        }
        startActivity(intent)
    }

}
