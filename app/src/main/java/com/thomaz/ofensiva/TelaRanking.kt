package com.thomaz.ofensiva

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TelaRanking(store: Store) {
    val lista = store.ofensivas.sortedByDescending { ofensivaAtual(it) }
    val medalhas = listOf("🥇", "🥈", "🥉")

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "Ranking",
                color = Branco,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text("Suas ofensivas, da maior para a menor", color = Cinza, fontSize = 14.sp)
        }

        if (lista.isEmpty()) {
            item {
                Cartao {
                    Text("Crie ofensivas para ver o ranking.", color = Cinza)
                }
            }
        }

        itemsIndexed(lista) { i, o ->
            val atual = ofensivaAtual(o)
            Cartao {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        if (i < 3) medalhas[i] else "${i + 1}º",
                        color = Branco,
                        fontSize = 24.sp,
                        modifier = Modifier.width(44.dp)
                    )
                    Chama(corDoMarco(atual), 40.dp)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            "${o.emoji} ${o.nome}",
                            color = Branco,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            "Recorde: ${maiorOfensiva(o)} dias",
                            color = Cinza,
                            fontSize = 12.sp
                        )
                    }
                    Text(
                        "$atual",
                        color = corDoMarco(atual),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
