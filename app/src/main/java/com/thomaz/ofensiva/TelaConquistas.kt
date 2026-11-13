package com.thomaz.ofensiva

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable

@Composable
fun TelaConquistas(store: Store) {
    val melhor = maxOf(store.melhorAtual(), store.melhorRecorde())
    val xp = store.xp()
    val proximo = proximoNivel(xp)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "Conquistas",
                color = Branco,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Cartao {
                Text(
                    "${nomeNivel(xp)} · $xp XP",
                    color = Laranja,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(Modifier.height(8.dp))
                val alvo = proximo?.second ?: xp
                val inicio = NIVEIS.last { xp >= it.second }.second
                val progresso = if (proximo == null) 1f
                else (xp - inicio).toFloat() / (alvo - inicio).toFloat()
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color(0xFF27272A))
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth(progresso.coerceIn(0f, 1f))
                            .height(10.dp)
                            .background(Laranja)
                    )
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    if (proximo != null) "Faltam ${proximo.second - xp} XP para ${proximo.first}"
                    else "Nível máximo!",
                    color = Cinza,
                    fontSize = 12.sp
                )
                Text("Cada dia concluído rende 10 XP", color = Cinza, fontSize = 12.sp)
            }
        }

        items(MARCOS) { m ->
            val ganhou = melhor >= m.dias
            Cartao {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Chama(if (ganhou) m.cor else Color(0xFF3F3F46), 44.dp)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            m.nome,
                            color = if (ganhou) m.cor else Cinza,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            "${m.dias} dias seguidos",
                            color = Cinza,
                            fontSize = 13.sp
                        )
                        Text(
                            if (ganhou) "Conquistada ✔" else "Bloqueada",
                            color = if (ganhou) Branco else Color(0xFF71717A),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
