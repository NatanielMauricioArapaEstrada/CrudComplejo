package com.example.crudnumcomplejo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.max

/**
 * View personalizada basada en la lógica de MyGraph del proyecto grafico.
 * Dibuja el plano cartesiano y dos números complejos como puntos y vectores.
 */
class MiGraficoComplejo(
    context: Context,
    private val pr: Double,
    private val pi: Double,
    private val prReflejada: Double,
    private val piReflejada: Double
) : View(context) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.BLACK)

        val ancho = canvas.width.toFloat()
        val alto = canvas.height.toFloat()

        // Se elige el límite según el mayor valor para que los dos puntos sean visibles.
        val mayorValor = max(
            max(abs(pr), abs(pi)),
            max(abs(prReflejada), abs(piReflejada))
        )
        val limite = max(5.0, ceil(mayorValor + 2.0)).toFloat()
        val limSupX = limite
        val limInfX = -limite
        val limSupY = limite
        val limInfY = -limite

        // Misma conversión matemática a píxeles usada en MyGraph.
        val centroX = ((0f - limInfX) / (limSupX - limInfX)) * ancho
        val centroY = alto - ((0f - limInfY) / (limSupY - limInfY)) * alto

        dibujarEjes(canvas, ancho, alto, centroX, centroY, limSupX, limInfX, limSupY, limInfY)

        dibujarComplejo(
            canvas,
            ancho,
            alto,
            centroX,
            centroY,
            limSupX,
            limInfX,
            limSupY,
            limInfY,
            pr,
            pi,
            Color.CYAN,
            "z"
        )

        dibujarComplejo(
            canvas,
            ancho,
            alto,
            centroX,
            centroY,
            limSupX,
            limInfX,
            limSupY,
            limInfY,
            prReflejada,
            piReflejada,
            Color.YELLOW,
            "z reflejado"
        )

        paint.style = Paint.Style.FILL
        paint.textSize = 28f
        paint.textAlign = Paint.Align.LEFT
        paint.color = Color.CYAN
        canvas.drawText("Original: ${formatoComplejo(pr, pi)}", 28f, 48f, paint)
        paint.color = Color.YELLOW
        canvas.drawText("Reflejo: ${formatoComplejo(prReflejada, piReflejada)}", 28f, 84f, paint)
    }

    private fun dibujarEjes(
        canvas: Canvas,
        ancho: Float,
        alto: Float,
        centroX: Float,
        centroY: Float,
        limSupX: Float,
        limInfX: Float,
        limSupY: Float,
        limInfY: Float
    ) {
        paint.color = Color.RED
        paint.strokeWidth = 4f
        paint.style = Paint.Style.STROKE
        canvas.drawLine(0f, centroY, ancho, centroY, paint)
        canvas.drawLine(centroX, 0f, centroX, alto, paint)

        paint.style = Paint.Style.FILL
        paint.textSize = 24f
        val paso = max(1, ceil(limSupX / 4f).toInt()).toFloat()

        var valorX = limInfX
        while (valorX <= limSupX) {
            val posX = ((valorX - limInfX) / (limSupX - limInfX)) * ancho
            paint.color = Color.RED
            paint.strokeWidth = 3f
            canvas.drawLine(posX, centroY - 8f, posX, centroY + 8f, paint)

            if (valorX != 0f) {
                paint.color = Color.WHITE
                paint.textAlign = Paint.Align.CENTER
                canvas.drawText(valorX.toInt().toString(), posX, centroY + 30f, paint)
            }
            valorX += paso
        }

        var valorY = limInfY
        while (valorY <= limSupY) {
            val posY = alto - ((valorY - limInfY) / (limSupY - limInfY)) * alto
            paint.color = Color.RED
            paint.strokeWidth = 3f
            canvas.drawLine(centroX - 8f, posY, centroX + 8f, posY, paint)

            if (valorY != 0f) {
                paint.color = Color.WHITE
                paint.textAlign = Paint.Align.RIGHT
                canvas.drawText(valorY.toInt().toString(), centroX - 14f, posY + 9f, paint)
            }
            valorY += paso
        }

        paint.color = Color.RED
        paint.textSize = 30f
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText("Real", ancho - 20f, centroY - 12f, paint)
        paint.textAlign = Paint.Align.LEFT
        canvas.drawText("Imaginario", centroX + 12f, 120f, paint)
    }

    private fun dibujarComplejo(
        canvas: Canvas,
        ancho: Float,
        alto: Float,
        centroX: Float,
        centroY: Float,
        limSupX: Float,
        limInfX: Float,
        limSupY: Float,
        limInfY: Float,
        parteReal: Double,
        parteImaginaria: Double,
        color: Int,
        etiqueta: String
    ) {
        val x = ((parteReal.toFloat() - limInfX) / (limSupX - limInfX)) * ancho
        val y = alto - ((parteImaginaria.toFloat() - limInfY) / (limSupY - limInfY)) * alto

        paint.color = color
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE
        canvas.drawLine(centroX, centroY, x, y, paint)

        paint.style = Paint.Style.FILL
        canvas.drawCircle(x, y, 12f, paint)
        paint.textSize = 26f
        paint.textAlign = Paint.Align.LEFT
        canvas.drawText(etiqueta, x + 16f, y - 14f, paint)
    }

    private fun formatoComplejo(real: Double, imaginaria: Double): String {
        val signo = if (imaginaria >= 0) "+" else "-"
        return "$real $signo ${abs(imaginaria)}i"
    }
}
