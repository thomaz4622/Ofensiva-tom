package com.thomaz.ofensiva

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val Fundo = Color(0xFF09090B)
val Grafite = Color(0xFF18181B)
val Laranja = Color(0xFFF97316)
val Branco = Color(0xFFFAFAFA)
val Cinza = Color(0xFFA1A1AA)

@Composable
fun Chama(cor: Color, tamanho: Dp = 64.dp) {
    Canvas(modifier = Modifier.size(tamanho)) {
        val w = size.width
        val h = size.height
        val externa = Path().apply {
            moveTo(w * 0.5f, 0f)
            cubicTo(w * 0.55f, h * 0.25f, w * 0.95f, h * 0.4f, w * 0.85f, h * 0.7f)
            cubicTo(w * 0.8f, h * 0.92f, w * 0.65f, h, w * 0.5f, h)
            cubicTo(w * 0.35f, h, w * 0.2f, h * 0.92f, w * 0.15f, h * 0.7f)
            cubicTo(w * 0.1f, h * 0.5f, w * 0.3f, h * 0.4f, w * 0.38f, h * 0.2f)
            cubicTo(w * 0.42f, h * 0.12f, w * 0.46f, h * 0.05f, w * 0.5f, 0f)
            close()
        }
        drawPath(externa, Brush.verticalGradient(listOf(cor, cor.copy(alpha = 0.6f))))
        val interna = Path().apply {
            moveTo(w * 0.5f, h * 0.45f)
            cubicTo(w * 0.6f, h * 0.6f, w * 0.7f, h * 0.7f, w * 0.65f, h * 0.85f)
            cubicTo(w * 0.6f, h * 0.97f, w * 0.4f, h * 0.97f, w * 0.35f, h * 0.85f)
            cubicTo(w * 0.3f, h * 0.7f, w * 0.45f, h * 0.6f, w * 0.5f, h * 0.45f)
            close()
        }
        drawPath(interna, Color.White.copy(alpha = 0.35f))
    }
}

@Composable
fun Cartao(
    modifier: Modifier = Modifier,
    conteudo: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Grafite, RoundedCornerShape(16.dp))
            .padding(16.dp),
        content = conteudo
    )
}
