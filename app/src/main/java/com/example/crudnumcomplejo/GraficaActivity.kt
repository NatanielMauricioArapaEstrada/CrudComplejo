package com.example.crudnumcomplejo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class GraficaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pr = intent.getDoubleExtra("pr", 0.0)
        val pi = intent.getDoubleExtra("pi", 0.0)
        val prReflejada = intent.getDoubleExtra("pr_r", 0.0)
        val piReflejada = intent.getDoubleExtra("pr_i", 0.0)

        // Igual que en el ejemplo del ing.: la Activity muestra directamente una View personalizada.
        val miGrafico = MiGraficoComplejo(this, pr, pi, prReflejada, piReflejada)
        setContentView(miGrafico)
    }
}
